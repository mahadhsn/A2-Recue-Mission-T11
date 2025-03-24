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

    public InterlacedScanner(Drone drone, Reciever reciever){
        super(drone, reciever);
    }

    @Override
    public void action() {
        logger.debug("(action) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {} | Special Turn Count: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete, specialTurnCount);
        if (state == 1) { // start by turning right as soon as encountering land (minimal difference either way)
            uTurnDirection = drone.getLeftDirection();
            drone.setDecision(drone.headingRight());
        }
        else if (state == 2) { // fly and scan as you go (scan first)
            if (subState == 0) {
                flyAndScanAction();
            }
            else if (subState == 1) { // if encounter water, echo forward in case there is land
                checkForLandAction();
            }
            else if (subState == 2 && !uTurnComplete) { // if no land, pop a U-ie
                uTurnAction();
            }
            else if (subState == 3) { // if land was found, go to it without scanning
                flyToLandAction();
            }
            else if (subState == 4) {
                specialUTurnAction();
            }
        }
        else if (state == 3) {
            drone.setDecision(drone.stop());
        }
    }

    @Override
    public void decision() {
        logger.debug("(decision) State: {} | Sub-State: {} | Fly Counter: {} | Turn Counter: {} | SubCounter: {} | rangeToLand: {} | U-Turn: {} | Special Turn Count: {}", state, subState, flyCounter, turnCounter, subCounter, rangeToLand, uTurnComplete, specialTurnCount);
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
            else if (subState == 4) {
                specialUTurnDecision();
            }
        }
        else if (state == 3) {
            return;
        }
    }

    public void flyAndScanAction() {
        if (flyCounter % 2 == 0) {
            drone.setDecision(drone.scan());
        }
        else {
            drone.setDecision(drone.fly());
        }
    }

    public void flyAndScanDecision() {
        if (flyCounter % 2 == 0) { // if scan was called in action function
            if (reciever.overGround()) {
                if (drone.getSiteFound() && drone.getCreekFound()) {
                    siteFound = true;
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

    public void checkForLandAction() {
        drone.setDecision(drone.echoStraight());
    }

    public void checkForLandDecision() {
        if (reciever.facingGround()) {
            resetCounter();
            subState = 0;
        }
        else {
            uTurnComplete = false;
            subState = 2;
        }
    }

    public void uTurnAction() {
        logger.debug("U turn action called. Current direction: {} | U-Turn Direction: {}", drone.getDirection(), uTurnDirection);
        if (!uTurnComplete) {
            if (lastUTurnWasLeft()) {
                logger.debug("U turn left action called");
                uTurnLeftAction();
            }
            else {
                logger.debug("U turn right action called");
                uTurnRightAction();
            }
        }
    }

    public void uTurnDecision() {
        logger.debug("U turn decision called");
        if (!uTurnComplete) {
            uTurnDirDecision();
        }
        else {
            subState = 2;
        }
    }

    public void uTurnLeftAction() { // echo as you turn for a U-turn to check if drone is stranded

        if (subCounter == 0) {
            drone.setDecision(drone.echoLeft());
        }
        else if (subCounter == 2) {
            drone.setDecision(drone.headingLeft());
        }
        else if (subCounter == 3) {
            drone.setDecision(drone.echoLeft());
        }
        else if (subCounter == 4) {
            drone.setDecision(drone.headingLeft());
        }

    }

    public void uTurnRightAction() { // same as uTurnLeft but opposite

        if (subCounter == 0) {
            drone.setDecision(drone.echoRight());
        }
        else if (subCounter == 2) {
            drone.setDecision(drone.headingRight());
        }
        else if (subCounter == 3) {
            drone.setDecision(drone.echoRight());
        }
        else if (subCounter == 4) {
            drone.setDecision(drone.headingRight());
        }
    }

    public void uTurnDirDecision() {
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

    private void completeUTurn() {
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
        if (subCounter == 0) {
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingRight());
            }
            else {
                drone.setDecision(drone.headingLeft());
            }
        }
        else if (subCounter == 1) {
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 2) {
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 3) {
            drone.setDecision(drone.fly());
        }
        else if (subCounter == 4) {
            if (lastUTurnWasLeftForSpecialUTurn()) {
                drone.setDecision(drone.headingLeft());
            }
            else {
                drone.setDecision(drone.headingRight());
            }
        }
        else if (subCounter == 5) {
            drone.setDecision(drone.fly());
        }
    }

    public void specialUTurnDecision() {
        if (subCounter == 0) {
            subCounter = 1;
        }
        else if (subCounter == 1) {
            subCounter = 2;
            if (specialTurnCount > 0) {
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

    public void abortTurn() {
        logger.warn("TURN ABORTED");
        uTurnComplete = true;
        resetSubCounter();
        resetFlyCounter();
        uTurnDirection = drone.getDirection();
        subState = 4;
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

    public void resetSubState() {
        subState = 0;
    }

    public void resetFlyCounter() {
        flyCounter = 0;
    }

    public void resetSubCounter() {
        subCounter = 0;
    }

    private boolean lastUTurnWasLeft() { // weird name but effective
        return turnCounter % 2 == 0;
    }

    private boolean lastUTurnWasLeftForSpecialUTurn() { // counter gets updated before it can be called in special u turn
        return turnCounter % 2 == 0;
    }

    public void resetOceanCheckers() {
        oceanOnSide = false;
        oceanOnTop = false;
    }

    public boolean isStranded() {
        return oceanOnSide && oceanOnTop;
    }

    public boolean getAbortTurn() {
        return rangeToLand < 4;
    }

    public void updateRangeToLand() {
        rangeToLand = reciever.getRange();
    }

}