package ca.mcmaster.se2aa4.island.team011.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Actions.*;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone {
    private JSONObject decision;
    private JSONObject nextDecision;
    private Position position;
    private Direction direction;
    private final Logger logger = LogManager.getLogger();
    
    public Drone(String headingStr){
        this.nextDecision = new JSONObject();
        this.decision = new JSONObject();
        this.position = new Position(1,1);
        this.direction = Direction.valueOf(headingStr); 
    }

    public JSONObject fly(){ // move drone forward
        position = position.forward(direction); // update to new position

        JSONObject decision = new Fly()
                                .getAction();

        return decision;
    }

    public JSONObject stop() { // stop drone
        JSONObject decision = new Stop()
                                .getAction();
        return decision;
    }

    // refactor heading later
    public JSONObject headingLeft() { // turn left
        position = position.forward(direction); // drone moves forward
        direction = direction.turnLeft(); // then turns left
        position = position.forward(direction); // and moves forward

        JSONObject decision = new Heading()
                                .setParameter(direction)
                                .getAction();

        return decision;
    }

    public JSONObject headingRight() { // turn right
        position = position.forward(direction); // drone moves forward
        direction = direction.turnRight(); // then turns right
        position = position.forward(direction); // and moves forward

        JSONObject decision = new Heading()
                                .setParameter(direction)
                                .getAction();

        return decision;
    }

    // assumes the direction isn't opposite to the drone
    public JSONObject headingOnDirection(Direction direction) {
        position = position.forward(direction);
        this.direction = direction;
        position = position.forward(direction);

        return new Heading()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject echoStraight() {
        JSONObject decision = new Echo()
                                .setParameter(direction)
                                .getAction();

        return decision;

    }

    public JSONObject echoOnDirection(Direction direction) {
        position = position.forward(direction);
        this.direction = direction;
        position = position.forward(direction);

        return new Echo()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject echoLeft() {
        JSONObject decision = new Echo()
                                .setParameter(direction.turnLeft())
                                .getAction();

        return decision;

    }

    public JSONObject echoRight() {
        JSONObject decision = new Echo()
                                .setParameter(direction.turnRight())
                                .getAction();

        return decision;

    }

    public JSONObject scan() {
        JSONObject decision = new Scan()
                                .getAction();
        return decision;
    }

    public void setDecision(JSONObject deci) {
        nextDecision = deci;
        logger.info("prev decision: {}", decision.toString()); 
        logger.info("Next decision set to: {}", nextDecision.toString());
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
}