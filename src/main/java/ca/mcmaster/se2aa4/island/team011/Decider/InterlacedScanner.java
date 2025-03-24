package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// InterlacedScanner is the decider that takes over when scanning for site
public class InterlacedScanner extends Decider{

    private Logger logger = LogManager.getLogger();

    private boolean siteFound = false;
    private boolean uTurnComplete = false;

    private Direction uTurnDirection = null; // initialize

    private FindIsland findIsland = null;

    private int state = 1;
    private int subState = 0;
    private int turnCounter = 0;
    private int flyCounter = 0;
    private int subCounter = 0;
    private int rangeToLand = 0;

    public InterlacedScanner(Drone drone, Reciever reciever){
        super(drone, reciever);
    }

    @Override
    public void action() {
        logger.debug("(action) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete);
        if (state == 1) {
            uTurnDirection = drone.getLeftDirection();
            turnRightAction();
        }
        else if (state == 2) {
            if (subState == 0) {
                flyAndScanAction();
            }
            else if (subState == 1) {
                checkForLandAction();
            }
            else if (subState == 2 && !uTurnComplete) {
                uTurnAction();
            }
            else if (subState == 3) {
                flyToLandAction();
            }
        }
        else if (state == 3) {
            drone.setDecision(drone.stop());
        }
    }

    @Override
    public void decision() {
        logger.debug("(decision) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete);
        if (state == 1) {
            state = 2;
        }
        else if (state == 2) {
            if (subState == 0) {
                flyAndScanDecision();
            }
            else if (subState == 1) {
                checkForLandDecision();
            }
            else if (subState == 2 && !uTurnComplete) {
                uTurnDecision();
            }
            else if (subState == 3) {
                flyToLandDecision();
            }
        }
        else if (state == 3) {
            return;
        }
    }

    public void flyAndScanAction() {
        if (flyCounter % 2 == 0) {
            drone.setDecision(drone.fly());
        }
        else {
            drone.setDecision(drone.scan());
        }
    }

    public void flyAndScanDecision() {

        if (flyCounter % 2 != 0) { // if scan was called in action function

            if (reciever.overGround()) {

                if (reciever.siteFound()) {
                    siteFound = true;
                    state = 3;
                    resetCounter();
                    resetFlyCounter();
                    resetSubState();
                    resetTurnCounter();
                }
            }
            else {
                subState = 1;
            }
        }

        flyCounter++;
    }

    public void checkForLandAction() {
        drone.setDecision(drone.echoStraight());
    }

    public void checkForLandDecision() {
        resetFlyCounter();
        if (reciever.facingGround()) {
            subState = 3;
            rangeToLand = reciever.getRange();
            resetTurnCounter();
        }
        else {
            uTurnComplete = false;
            subState = 2;
        }
    }

    public void uTurnAction() {
        logger.debug("U turn action called. Current direction: {} | U-Turn Direction: {}", drone.getDirection(), uTurnDirection);
        if (!uTurnComplete) {
            if (turnCounter % 2 == 0) {
                uTurnLeftAction();
            }
            else {
                uTurnRightAction();
            }
        }
    }

    public void uTurnDecision() {
        logger.debug("U turn decision called");
        if (!uTurnComplete) {
            if (turnCounter % 2 == 0) {
                uTurnLeftDecision();
                return;
            }
            else {
                uTurnRightDecision();
            }
        }
        else {
            subState = 2;
        }
    }

    public void uTurnLeftAction() {
        logger.debug("U turn left action called");
        drone.setDecision(drone.headingLeft());
    }

    public void uTurnLeftDecision() {
        if (subCounter == 0) {
            subCounter = 1;
        }
        else {
            logger.debug("U turn left complete");
            if (drone.getDirection() == uTurnDirection) {
                uTurnComplete = true;
                resetSubCounter();
                uTurnDirection = drone.getUTurnDirection();
                turnCounter++;
                subState = 0;
                return;
            }
            else {
                logger.warn("U Turn Left Failed! Current direction: {} | U-Turn Direction: {}", drone.getDirection(), uTurnDirection);
            }
        }
    }

    public void uTurnRightAction() {
        logger.debug("U turn right action called");
        drone.setDecision(drone.headingRight());
    }

    public void uTurnRightDecision() {
        logger.debug("U turn right decision called");
        if (subCounter == 0) {
            subCounter = 1;
        }
        else {
            logger.debug("U turn right complete");
            if (drone.getDirection() == uTurnDirection) {
                uTurnComplete = true;
                resetSubCounter();
                uTurnDirection = drone.getUTurnDirection();
                turnCounter++;
                subState = 0;
                return;
            }
            else {
                logger.warn("U Turn Right Failed! Current direction: {} | U-Turn Direction: {}", drone.getDirection(), uTurnDirection);
            }
        }
    }

    public void flyToLandAction() {
        if (rangeToLand + 1 > 0) {
            drone.setDecision(drone.fly());
            rangeToLand--;
            logger.debug("rangeToLand: {}", rangeToLand);
        }
        else {
            drone.setDecision(drone.scan());
        }
    }

    public void flyToLandDecision() {
        if (rangeToLand + 1 <= 0) {
            subState = 0;
        }
    }

    public void turnRightAction() {
        drone.setDecision(drone.headingRight());
    }

    public void turnLeftAction() {
        drone.setDecision(drone.headingLeft());
    }

    public void resetSubState() {
        subState = 0;
    }

    public void resetFlyCounter() {
        flyCounter = 0;
    }

    public void resetTurnCounter() {
        turnCounter = 0;
    }

    public void resetSubCounter() {
        subCounter = 0;
    }
}