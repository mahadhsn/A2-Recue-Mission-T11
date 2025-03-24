package ca.mcmaster.se2aa4.island.team011.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Actions.Echo;
import ca.mcmaster.se2aa4.island.team011.Actions.Fly;
import ca.mcmaster.se2aa4.island.team011.Actions.Heading;
import ca.mcmaster.se2aa4.island.team011.Actions.Scan;
import ca.mcmaster.se2aa4.island.team011.Actions.Stop;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;

public class Drone {
    private JSONObject decision;
    private JSONObject nextDecision;
    private Position position;
    private Direction direction;
    private final Logger logger = LogManager.getLogger();

    private boolean allCreeksFound = false;
    private boolean creekFound = false;
    private boolean siteFound = false;
    
    public Drone(String headingStr){
        this.nextDecision = new JSONObject();
        this.decision = new JSONObject();
        this.position = new Position(1, 1);
        this.direction = Direction.valueOf(headingStr); 
    }

    public JSONObject fly(){ //Move drone forward
        logger.debug("Drone fly");
        position = position.forward(direction); //Update to new position

        return new Fly()
                .getAction();
    }

    public JSONObject stop() { //Stopping drone
        logger.debug("Drone stop");
        return new Stop()
                .getAction();
    }

    public JSONObject headingLeft() { //Turn left
        logger.debug("Drone headingLeft");
        position = position.forward(direction); //Drone moves forward
        direction = direction.turnLeft(); //Then turns left
        position = position.forward(direction); //Then moves forward

        return new Heading()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject headingRight() { //Turn right
        logger.debug("Drone headingRight");
        position = position.forward(direction); //Drone moves forward
        direction = direction.turnRight(); //Then turns right
        position = position.forward(direction); //Then moves forward

        return new Heading()
                .setParameter(direction)
                .getAction();
    }

    //Assumes the direction isn't opposite to the drone
    public JSONObject headingOnDirection(Direction direction) {
        logger.debug("Drone headingOnDirection");
        position = position.forward(direction);
        this.direction = direction;
        position = position.forward(direction);

        return new Heading()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject echoStraight() {
        logger.debug("Drone echoStraight");
        return new Echo()
                .setParameter(direction)
                .getAction();

    }

    public JSONObject echoOnDirection(Direction direction) {
        logger.debug("Drone echoOnDirection");
        return new Echo()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject echoLeft() {
        logger.debug("Drone echoLeft");
        return new Echo()
                .setParameter(direction.turnLeft())
                .getAction();

    }

    public JSONObject echoRight() {
        logger.debug("Drone echoRight");
        return new Echo()
                .setParameter(direction.turnRight())
                .getAction();

    }

    public JSONObject scan() {
        logger.debug("Drone scan");
        return new Scan()
                .getAction();
    }

    public void setDecision(JSONObject deci) {
        nextDecision = deci;
        logger.debug("Prev decision: {}", decision.toString());
        logger.debug("Next decision set to: {}", nextDecision.toString());
    }

    public JSONObject getDecision() {
        decision = nextDecision;
        return nextDecision;
    }

    public String getPrevDecision() {
        return decision.optString("action", "");
    }

    public String getHeading() {
        return direction.toString();
    }

    public String getCoords(){
        return position.toString();
    }

    public int getX(){
        return position.getX();
    }

    public int getY(){
        return position.getY();
    }

    public Position getPosition(){
        return position;
    }

    public Direction getDirection(){
        return direction;
    }

    public Direction getRightDirection() {
        return direction.turnRight();
    }

    public Direction getLeftDirection() {
        return direction.turnLeft();
    }

    public Direction getUTurnDirection() {
        return direction.uTurn();
    }

    public void setCreekFound(boolean creekFound) {
        this.creekFound = creekFound;
    }

    public void setSiteFound(boolean siteFound) {
        this.siteFound = siteFound;
    }

    public void setAllCreeksFound(boolean allCreeksFound) {
        this.allCreeksFound = allCreeksFound;
    }

    public boolean getCreekFound() {
        return creekFound;
    }
    public boolean getSiteFound() {
        return siteFound;
    }
    public boolean getAllCreeksFound() {
        return allCreeksFound;
    }
}