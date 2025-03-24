package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// InterlacedScanner finds site and creeks
public class InterlacedScanner extends Decider {

    private Logger logger = LogManager.getLogger();

    private boolean uTurnComplete = false;

    private boolean oceanOnSide = false;
    private boolean oceanOnTop = false;

    private Direction uTurnDirection = null; // initialize

    private int state = 1;
    private int subState = 0;
    private int turnCounter = 0;
    private int flyCounter = 0;
    private int subCounter = 0;
    private int rangeToLand = 0;
    private int specialTurnCount = 0;
    private int specialFlyCount = 0;

    public InterlacedScanner(Drone drone, Reciever reciever) { // initialize with drone and reciever
        super(drone, reciever);
    }

    // handles all actions
    @Override
    public void action() {
        logger.debug("(action) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {} | Special Turn Count: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete, specialTurnCount);

        if (state == 1) { // State 1: start by turning right as soon as encountering land (minimal difference either way)
            uTurnDirection = drone.getLeftDirection();
            drone.setDecision(drone.headingRight());
        }
        else if (state == 2) { // State 2: fly and scan as you go (scan first)
            if (subState == 0) {
                flyAndScanAction();
            }
            else if (subState == 1) { // subState 1: if encounter water, echo forward in case there is land
                checkForLandAction();
            }
            else if (subState == 2 && !uTurnComplete) { // subState 2: if no land, do a U-turn
                uTurnAction();
            }
            else if (subState == 3) { // subState 3: if land was found, go to it without scanning
                flyToLandAction();
            }
            else if (subState == 4) { // subState 4: if drone is stranded, do a special U-turn
                specialUTurnAction();
            }
        }
        else if (state == 3) { // state 3: if site and creek found, stop
            drone.setDecision(drone.stop());
        }
    }

    // handles all decisions
    @Override
    public void decision() {
        logger.debug("(decision) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {} | Special Turn Count: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete, specialTurnCount);
        if (state == 1) { // if state 1, go to state 2
            state = 2;
        }
        else if (state == 2) { // if state 2, go to subState 0
            if (subState == 0) {
                flyAndScanDecision();
            }
            else if (subState == 1) { // if subState 1, check for land
                checkForLandDecision();
            }
            else if (subState == 2 && !uTurnComplete) { // if subState 2, do a U-turn
                uTurnDecision();
            }
            else if (subState == 3) { // if subState 3, fly to land
                flyToLandDecision();
            }
            else if (subState == 4) { // if subState 4, do a special U-turn
                specialUTurnDecision();
            }
        }
        else if (state == 3) { // if state 3, stop
            return;
        }
    }

    public void flyAndScanAction() { // fly and scan as you go
        if (flyCounter % 2 == 0) {
            drone.setDecision(drone.scan());
        }
        else {
            drone.setDecision(drone.fly());
        }
    }

    public void flyAndScanDecision() { //
        if (flyCounter % 2 == 0) { // if scan was called in action function
            if (reciever.overGround()) {
                if (drone.getSiteFound() && drone.getCreekFound()) {
                    state = 3;
                    resetCounter();
                    resetSubState();
                }
            }
            else {
                subState = 1;
            }
        }
        flyCounter++;
    }

    public void checkForLandAction() { // check for land after encountering water
        drone.setDecision(drone.echoStraight());
    }

    public void checkForLandDecision() { 
        if (reciever.facingGround()) { // if land is found, go to it without scanning
            resetCounter();
            subState = 0;
        }
        else { // else do a U-turn
            uTurnComplete = false;
            subState = 2;
        }
    }

    public void uTurnAction() { // u turn action
        logger.debug("U turn action called. Current direction: {} | U-Turn Direction: {}", drone.getDirection(), uTurnDirection);

        if (!uTurnComplete) { // if U-turn is not complete, do a U-turn in the opposite direction of the last one
            if (lastUTurnWasLeft()) { // if last U-turn was left, do a Right U-turn
                logger.debug("U turn left action called");
                uTurnRightAction();
            }
            else { // else do a Left U-turn
                logger.debug("U turn right action called");
                uTurnLeftAction();
            }
        }
    }

    public void uTurnDecision() {
        logger.debug("U turn decision called");

        if (!uTurnComplete) { // if U-turn not complete go to sub method
            uTurnDirDecision();
        }
        else { // else go to subState 2
            subState = 2;
        }
    }

    public void uTurnLeftAction() { // echo as you turn for a U-turn to check if drone is stranded

        if (subCounter == 0) { // subCounter 0: echo left
            drone.setDecision(drone.echoLeft());
        }
        else if (subCounter == 2) { // subCounter 2: heading left
            drone.setDecision(drone.headingLeft());
        }
        else if (subCounter == 3) { // subCounter 3: echo left
            drone.setDecision(drone.echoLeft());
        }
        else if (subCounter == 4) { // subCounter 4: heading left
            drone.setDecision(drone.headingLeft());
        }

    }

    public void uTurnRightAction() { // same as uTurnLeft but opposite

        if (subCounter == 0) { // subCounter 0: echo right
            drone.setDecision(drone.echoRight());
        }
        else if (subCounter == 2) { // subCounter 2: heading right
            drone.setDecision(drone.headingRight());
        }
        else if (subCounter == 3) { // subCounter 3: echo right
            drone.setDecision(drone.echoRight());
        }
        else if (subCounter == 4) { // subCounter 4: heading right
            drone.setDecision(drone.headingRight());
        }
    }

    public void uTurnDirDecision() { // U-turn decision
        if (subCounter == 0) {
            if (!reciever.facingGround()) { // will likely be true if previous one is true
                oceanOnSide = true;
                updateRangeToLand();
                if (getAbortTurn()) { // gets range
                    abortTurn();
                    return;
                }
            }
            else {
                resetOceanCheckers();
            }
            subCounter = 2;
        }
        else if (subCounter == 2) {
            subCounter = 3;
        }
        else if (subCounter == 3) {
            if (!reciever.facingGround()) {
                oceanOnTop = true;
            }
            else {
                resetOceanCheckers();
            }
            subCounter = 4;
        }
        else if (subCounter == 4) {
            completeUTurn();
        }
    }

    private void completeUTurn() { // complete U-turn
        uTurnComplete = true;
        resetSubCounter();
        resetFlyCounter();
        uTurnDirection = drone.getUTurnDirection();
        subState = 0;
        turnCounter++;
        if (isStranded()) {
            logger.warn("Drone Stranded! Going to Sub State 4");
            subState = 4;
            if (specialTurnCount > 2) {
                specialFlyCount = specialTurnCount/2;
            }
        }
    }

    public void specialUTurnAction() { // begin special U Turn
        logger.warn("Special U turn action called");

        if (subCounter == 0) { // subCounter 0: turn opposite
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingRight());
            }
            else {
                drone.setDecision(drone.headingLeft());
            }
        }
        else if (subCounter == 1) { // subCounter 1: turn back
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 2) { // subCounter 2: turn back
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 3) { // subCounter 3: fly
            drone.setDecision(drone.fly());
        }
        else if (subCounter == 4) { // subCounter 4: turn
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 5) { // subCounter 5: fly for a certain amount of time depending on how many special u turns were called
            drone.setDecision(drone.fly());
        }
    }

    public void specialUTurnDecision() {
        if (subCounter == 0) {
            subCounter = 1;
        }
        else if (subCounter == 1) {
            subCounter = 2;
            if (specialTurnCount > 0) { // extend the special u turn depending on what the special u turn count is
                subCounter = 5;
            }
        }
        else if (subCounter == 2) {
            subCounter = 3;
        }
        else if (subCounter == 3) {
            subCounter = 4;
        }
        else if (subCounter == 4) {
            resetSubCounter();
            resetFlyCounter();
            resetOceanCheckers();
            subState = 0;
            specialTurnCount++;
        }
        else if (subCounter == 5) {
            if (specialFlyCount > 0) {
                specialFlyCount--;
            }
            else {
                subCounter = 2;
            }
        }
    }

    public void abortTurn() { // edge case where drone is turning out of bounds
        logger.warn("TURN ABORTED");
        uTurnComplete = true;
        resetSubCounter();
        resetFlyCounter();
        uTurnDirection = drone.getDirection();
        subState = 4;
    }

    public void flyToLandAction() { // fly to land without scanning if land is found using an echo
        if (rangeToLand + 1 > 0) {
            drone.setDecision(drone.fly());
            rangeToLand--;
            logger.debug("rangeToLand: {}", rangeToLand);
        }
        else {
            drone.setDecision(drone.scan());
        }
    }

    public void flyToLandDecision() { // fly to land decision
        if (rangeToLand + 1 <= 0) {
            subState = 0;
        }
    }

    public void resetSubState() { // reset subState to 0
        subState = 0;
    }

    public void resetFlyCounter() { // reset flyCounter to 0
        flyCounter = 0;
    }

    public void resetSubCounter() { // reset subCounter to 0
        subCounter = 0;
    }

    private boolean lastUTurnWasLeft() { // weird name but effective
        return turnCounter % 2 == 0;
    }

    private boolean lastUTurnWasLeftForSpecialUTurn() { // counter gets updated before it can be called in special u turn
        return turnCounter % 2 == 0;
    }

    public void resetOceanCheckers() { // reset ocean checkers
        oceanOnSide = false;
        oceanOnTop = false;
    }

    public boolean isStranded() { // check if drone is stranded
        return oceanOnSide && oceanOnTop;
    }

    public boolean getAbortTurn() { // get abort turn
        return rangeToLand < 4;
    }

    public void updateRangeToLand() { // update range to land
        rangeToLand = reciever.getRange();
    }

}