package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// FindIsland class completes the first stage of looking for the emergency site: to find the island
public class FindIsland extends Decider {

    private final Logger logger = LogManager.getLogger();

    private boolean groundFound = false;
    private boolean foundLand = false;

    private int state = 1;
    private int subState = 0;
    private int counter = 0;

    private int rangeToLand = 0;
    private int furthestRangeToOutOfBounds = 0;
    private Direction directionToChange;
    private Direction directionToEcho;
    private Direction groundDirection;

    // constructor
    public FindIsland(Drone drone, Reciever reciever) {
        super(drone, reciever);
    }

    // handles all actions
    @Override
    public void action() {
        logger.debug("(action) State: {} | Sub-State: {} | Current Counter: {}", state, subState, counter);

        // State 1: Initial Echo to find land direction
        if (state == 1) {
            logger.debug("initEchoAction called");
            initEchoAction();
        }
        // State 2: Fly towards furthest border while echoing in the direction of land (could be skipped if land is found instantly)
        else if (state == 2 && !groundFound) {
            logger.debug("findLandAction called");
            findLandAction();
        }
        // State 3: Fly towards land
        else if (state == 3) {
            logger.debug("goToLandAction called");
            goToLandAction();
        }
        // State 4: Land found, end
        else if (state == 4) {
            foundLand = true;
            return;
        }
    }

    // handles all decisions and state changes
    @Override
    public void decision() {
        logger.debug("(decision) State: {} | Sub-State: {} | Current Counter: {}", state, subState, counter);

        if (state == 1) {
            logger.debug("initEchoDecision called");
            initEchoDecision();
        }
        else if (state == 2 && !groundFound) {
            logger.debug("findLandDecision called");
            findLandDecision();
        }
        else if (state == 3) {
            logger.debug("goToLand called");
            goToLandDecision();
        }
        else if (state == 4) {
            foundLand = true;
            return;
        }
    }

    // initial echo when drone is spawned
    // 3 states, each for an echo in each direction
    // if groundFound, store the rangeToLand
    // else, store the furthest distance to Out of Bounds
    public void initEchoAction() {
        if (subState == 0) {
            drone.setDecision(drone.echoStraight());
        }

        else if (subState == 1) {
            drone.setDecision(drone.echoRight());

        }
        else if (subState == 2) {
            drone.setDecision(drone.echoLeft());
        }
    }

    public void initEchoDecision() {
        if (subState == 0) {
            if (reciever.facingGround()) {
                rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
                state = 3;
                logger.debug("Going to state 3");
                resetSubState();
            }
            else {
                furthestRangeToOutOfBounds = reciever.getRange(); // initialize this variable
                directionToChange = drone.getDirection();
                subState = 1;
                logger.warn("Ground not found. Furthest range found: {} | Direction: {}", furthestRangeToOutOfBounds, directionToChange);
                logger.debug("Going to subState 1");
            }
        }
        else if (subState == 1) {
            if (reciever.facingGround()) {
                rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getRightDirection();
                state = 3;
                logger.debug("Going to state 3");
                resetSubState();
                return;
            }
            else {
                if (reciever.getRange() > furthestRangeToOutOfBounds) { // from now only change if new variable is bigger
                    furthestRangeToOutOfBounds = reciever.getRange();
                    directionToChange = drone.getRightDirection();
                    logger.warn("Ground not found. Furthest range found: {} | Direction: {}", furthestRangeToOutOfBounds, directionToChange);
                }
            }
            subState = 2;
            logger.debug("Going to subState 2");
        }
        else if (subState == 2) {
            if (reciever.facingGround()) {
                rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getLeftDirection();
                state = 3;
                logger.debug("Going to state 3");
                resetSubState();
                return;
            }
            else {
                if (reciever.getRange() > furthestRangeToOutOfBounds) {
                    furthestRangeToOutOfBounds = reciever.getRange();
                    directionToChange = drone.getLeftDirection();
                    logger.warn("Ground not found. Furthest range found: {} | Direction: {}", furthestRangeToOutOfBounds, directionToChange);
                }
            }
            // move to state 2 if land not found
            state = 2;
            logger.debug("Going to subState 2");
            resetSubState();
        }
    }

    // used if isLand is not found instantly
    // echos left and right to see which side the island is on by only scanning in the further direction
    // used to get past having to echo in both directions
    public void findLandAction() {
        if (subState == 0) { // subState 0: change direction to the direction with the furthest range
            if (drone.getDirection() != directionToChange) {
                drone.setDecision(drone.headingOnDirection(directionToChange));
            }
        }
        else if (subState == 1) { // subState 1: echo in the left direction
            drone.setDecision(drone.echoLeft());

        }
        else if (subState == 2) { // subState 2: echo in the right direction
            drone.setDecision(drone.echoRight());
        }
        else if (subState == 3) { // subState 3: fly towards the land
            flyAndEchoAction();
        }
    }

    // decision for previous method
    public void findLandDecision() {
        if (subState == 0) { // subState 0: go to subState 1
            subState = 1;
            logger.debug("Going to subState 1");
        }
        else if (subState == 1) { // subState 1: check if the range is bigger than the previous range
            furthestRangeToOutOfBounds = reciever.getRange();
            directionToEcho = drone.getLeftDirection();
            subState = 2;
            logger.debug("Going to subState 2");
        }

        else if (subState == 2) { // subState 2: check if the range is bigger than the previous range and echo in bigger direcition
            if (reciever.getRange() > furthestRangeToOutOfBounds) {
                furthestRangeToOutOfBounds = reciever.getRange();
                directionToEcho = drone.getRightDirection();
            }
            subState = 3;
            logger.debug("Going to subState 3");
        }
        else if (subState == 3) { // subState 3: fly and echo until land is found
            flyAndEchoDecision();
            if (foundLand) {
                resetSubState();
            }
        }
    }

    // sub-method to be called within the findLand() method
    // echos in the direction of the land while flying
    public void flyAndEchoAction() {
        if (counter % 2 == 0) {
            drone.setDecision(drone.echoOnDirection(directionToEcho));
            groundDirection = directionToEcho;
        }
        else {
            drone.setDecision(drone.fly());
        }
    }

    public void flyAndEchoDecision() {
        if (counter % 2 == 0) { // if counter is even, echo
            logger.debug("Counter: {}", counter);
            counter++;
            if (reciever.facingGround()) { // if ground is found, store the range and go to state 3
                rangeToLand = reciever.getRange();
                groundFound = true;
                state = 3;
                resetSubState();
                resetCounter();
                return;
            }
        }
        else {
            logger.debug("Counter: {}", counter);
            counter++;
        }
    }

    // fly towards the land
    public void goToLandAction() {
        if (drone.getDirection() != groundDirection) { // if not facing the land, turn to face the land
            drone.setDecision(drone.headingOnDirection(groundDirection));
            logger.debug("Turning to ground");
        }
        else if (subState == 0) { // if facing the land, fly towards the land
            if (rangeToLand + 1 > 0) {
                drone.setDecision(drone.fly());
                rangeToLand--;
                logger.debug("rangeToLand: {}", rangeToLand);
            }
        }
        else if (subState == 1) { // if land is found, scan
            drone.setDecision(drone.scan());
        }
    }

    public void goToLandDecision() {
        if (subState == 0) { // subState 0: fly towards the land
            if (rangeToLand + 1 < 1) {
                subState = 1;
            }
        }
        else if (subState == 1) { // subState 1: scan
            resetSubState();
            state = 4;
            logger.debug("Going to state 4");
        }
    }

    public void resetSubState() { // reset subState to 0
        subState = 0;
    }

    public void resetCounter() { // reset counter to 0
        counter = 0;
    }

    public boolean getIslandFound() { // return if land is found
        return foundLand;
    }
}