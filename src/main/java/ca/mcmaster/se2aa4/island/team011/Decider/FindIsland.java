package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// FindIsland class completes the first stage of looking for the emergency site: to find the island
public class FindIsland extends Decider {

    private boolean groundFound = false;
    private boolean atGround = false;

    private boolean madeFirstMove = false;

    private int state = 1;
    private int subState = 0;
    private int counter = 0;

    private int rangeToLand = 0;
    private int furthestRangeToOutOfBounds = 0;
    private Direction directionToChange;
    private Direction directionToEcho;
    private Direction groundDirection;
    private Logger logger = LogManager.getLogger();

    public FindIsland(Drone drone, Reciever reciever) {
        super(drone, reciever);
    }

    // handles all actions
    @Override
    public void action() {
        logger.debug("(action) State: {} | Sub-State: {} | Current Counter: {}", state, subState, counter);
        if (state == 1) {
            logger.debug("initEchoAction called");
            initEchoAction();
        }
        // state 2 could be skipped if land is found instantly
        else if (state == 2 && !groundFound) {
            logger.debug("findLandAction called");
            findLandAction();
        }
        else if (state == 3) {
            logger.debug("goToLandAction called");
            goToLandAction();
        }
        else if (state == 4) {
            drone.setDecision(drone.stop());
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
        if (subState == 0) {
            if (drone.getDirection() != directionToChange) {
                drone.setDecision(drone.headingOnDirection(directionToChange));
            }
        }
        else if (subState == 1) {
            drone.setDecision(drone.echoLeft());

        }
        else if (subState == 2) {
            drone.setDecision(drone.echoRight());
        }
        else if (subState == 3) {
            flyAndEchoAction();
        }
    }

    public void findLandDecision() {
        if (subState == 0) {
            subState = 1;
            logger.debug("Going to subState 1");
        }
        else if (subState == 1) {
            furthestRangeToOutOfBounds = reciever.getRange();
            directionToEcho = drone.getLeftDirection();
            subState = 2;
            logger.debug("Going to subState 2");
        }

        else if (subState == 2) {
            if (reciever.getRange() > furthestRangeToOutOfBounds) {
                furthestRangeToOutOfBounds = reciever.getRange();
                directionToEcho = drone.getRightDirection();
            }
            subState = 3;
            logger.debug("Going to subState 3");
        }
        else if (subState == 3) {
            flyAndEchoDecision();
            if (foundLand) {
                resetSubState();
            }
        }
    }

    // sub-method to be called within the findLand() method
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
        if (counter % 2 == 0) {
            logger.debug("Counter: {}", counter);
            counter++;
            if (reciever.facingGround()) {
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

    public void goToLandAction() {
        if (drone.getDirection() != groundDirection) {
            drone.setDecision(drone.headingOnDirection(groundDirection));
            logger.debug("Turning to ground");
        }
        else if (subState == 0) {
            if (rangeToLand + 1 > 0) {
                drone.setDecision(drone.fly());
                rangeToLand--;
                logger.debug("rangeToLand: {}", rangeToLand);
            }
        }
        else if (subState == 1) {
            drone.setDecision(drone.scan());
        }
    }

    public void goToLandDecision() {
        if (subState == 0) {
            if (rangeToLand + 1 < 1) {
                subState = 1;
            }
        }
        else if (subState == 1) {
            resetSubState();
            state = 4;
            logger.debug("Going to state 4");
        }
    }

    public void resetSubState() {
        subState = 0;
    }

    public void resetCounter() {
        counter = 0;
    }

    public Drone getDroneInstance() {
        return drone;
    }

    public Reciever getRecieverInstance() {
        return reciever;
    }
}