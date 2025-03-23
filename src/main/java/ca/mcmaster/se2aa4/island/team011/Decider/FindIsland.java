package ca.mcmaster.se2aa4.island.team011.Decider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;

// FindIsland class completes the first stage of looking for the emergency site: to find the island
public class FindIsland extends Decider {

    private boolean groundFound = false;
    private boolean atGround = false;

    private int state = 1;
    private int subState = 0;
    private int counter = 0;

    private int rangeToLand = 0;
    private int furthestRangeToOutOfBounds = 0;
    private Direction directionToChange;
    private Direction directionToEcho;
    private Direction groundDirection;
    private final Logger logger = LogManager.getLogger(FindIsland.class);

    public FindIsland(Drone drone, Reciever reciever) {
        super(drone, reciever);
    }

    @Override
    public void decide() {

        if (state == 1) {
            initEcho();
        }
        // state 2 could be skipped if land is found instantly
        else if (state == 2 && !groundFound) {
            findLand();
        }
        else if (state == 3) {
            goToLand();
        }
        else if (state == 4) {
            drone.setDecision(drone.stop());
        }
        logger.info("Current State: {} \nCurrent sub state: {}\n Current Counter: {}", state, subState, counter);
    }

    // initial echo when drone is spawned
    // 3 states, each for an echo in each direction
    // if groundFound, store the rangeToLand
    // else, store the furthest distance to Out of Bounds
    public void initEcho() {
        if (subState == 0) {
            directionToChange = drone.getDirection();
            drone.setDecision(drone.echoStraight());

            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
                state = 3;
                return;
            }
            else {
                int furthestRangeToOutOfBounds = reciever.getRange();
            }
            subState = 1;
        }
        else if (subState == 1) {
            directionToChange = drone.getRightDirection();
            drone.setDecision(drone.echoRight());

            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
                state = 3;
                return;
            }
            else {
                if (reciever.getRange() >= furthestRangeToOutOfBounds) {
                    furthestRangeToOutOfBounds = reciever.getRange();
                    directionToChange = drone.getDirection();
                }
            }
            subState = 2;
        }
        else if (subState == 2) {
            directionToChange = drone.getLeftDirection();
            drone.setDecision(drone.echoLeft());

            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
                state = 3;
                return;
            }
            else {
                if (reciever.getRange() >= furthestRangeToOutOfBounds) {
                    furthestRangeToOutOfBounds = reciever.getRange();
                    directionToChange = drone.getDirection();
                }
            }
            // move to state 2 if land not found
            state = 2;
            resetSubState();
        }
    }

    // used if isLand is not found instantly
    // echos left and right to see which side the island is on by only scanning in the further direction
    // used to get past having to echo in both directions
    public void findLand() {
        if (subState == 0) {
            drone.setDecision(drone.headingOnDirection(directionToChange));
            subState = 1;
        }
        else if (subState == 1) {
            drone.setDecision(drone.echoLeft());
            subState = 2;
            furthestRangeToOutOfBounds = reciever.getRange();
            directionToEcho = drone.getLeftDirection();
        }
        else if (subState == 2) {
            drone.setDecision(drone.echoRight());
            subState = 3;
            if (reciever.getRange() >= furthestRangeToOutOfBounds) {
                furthestRangeToOutOfBounds = reciever.getRange();
                directionToEcho = drone.getRightDirection();
            }
        }
        else if (subState == 3) {
            flyAndEcho();

            if (foundLand) {
                resetSubState();
            }
        }
    }

    // sub-method to be called within the findLand() method
    public void flyAndEcho() {
        if (counter % 2 == 0) {
            counter++;
            drone.setDecision(drone.echoOnDirection(directionToEcho));
            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
                state = 3;
                resetCounter();
                return;
            }
        }
        else {
            counter++;
            drone.setDecision(drone.fly());
        }
    }

    public void goToLand() {
        if (subState == 0) {
            drone.setDecision(drone.headingOnDirection(groundDirection));
            subState = 1;
        }
        else if (subState == 1) {
            if (rangeToLand >= 1) {
                rangeToLand--;
                drone.setDecision(drone.fly());
            }
            else {
                drone.setDecision(drone.scan());
                resetSubState();
                state = 5;
            }
        }
    }
}