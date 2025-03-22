package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;

// FindIsland class completes the first stage of looking for the emergency site: to find the island
public class FindIsland extends Decider {

    private boolean groundFound = false;
    private boolean atGround = false;

    private int state = 1;
    private int subCounter = 0;

    private int rangeToLand = 0;
    private int furthestRangeToOutOfBounds = 0;
    private Direction direction;
    private Direction groundDirection;



    public FindIsland(Drone drone, Reciever reciever) {
        super(drone, reciever);
    }

    @Override
    public void decide() {

        if (state == 1) {
            initEcho();
        }
        else if (state == 2) {

        }



    }

    // initial echo when drone is spawned
    // 3 states, each for an echo in each direction
    // if groundFound, store the rangeToLand
    // else, store the furthest distance to Out of Bounds
    public void initEcho() {

        if (subCounter == 0) {
            direction = drone.getDirection();
            drone.setDecision(drone.echoStraight());
            subCounter = 1;
            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
            }
            else {
                int furthestRangeToOutOfBounds = reciever.getRange();
            }
        }
        else if (subCounter == 1) {
            direction = drone.getRightDirection();
            subCounter = 2;
            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
            }
            else {
                if (reciever.getRange() > furthestRangeToOutOfBounds) {
                    furthestRangeToOutOfBounds = reciever.getRange();
                    direction = drone.getDirection();
                }
            }
        }
        else if (subCounter == 2) {
            direction = drone.getLeftDirection();
            drone.setDecision(drone.echoLeft());
            subCounter = 3;
            if (reciever.facingGround()) {
                int rangeToLand = reciever.getRange();
                groundFound = true;
                groundDirection = drone.getDirection();
            }
            else {
                if (reciever.getRange() > furthestRangeToOutOfBounds) {
                    furthestRangeToOutOfBounds = reciever.getRange();
                    direction = drone.getDirection();
                }
            }
            state = 2;
            resetSubCounter();
        }

    }
    
    public void findLand() {

    }

    public void goToLand() {

    }

    public void resetSubCounter() {
        subCounter = 0;
    }

}