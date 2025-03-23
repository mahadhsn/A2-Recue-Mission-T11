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
    
    public Drone(String headingStr){
        this.nextDecision = new JSONObject();
        this.decision = new JSONObject();
        this.position = new Position(1, 1);
        this.direction = Direction.valueOf(headingStr); 
    }

    public JSONObject fly(){ // move drone forward
        position = position.forward(direction); // update to new position

        return new Fly()
                .getAction();
    }

    public JSONObject stop() { // stop drone
        return new Stop()
                .getAction();
    }

    // refactor heading later
    public JSONObject headingLeft() { // turn left
        position = position.forward(direction); // drone moves forward
        direction = direction.turnLeft(); // then turns left
        position = position.forward(direction); // and moves forward

        return new Heading()
                .setParameter(direction)
                .getAction();
    }

    public JSONObject headingRight() { // turn right
        position = position.forward(direction); // drone moves forward
        direction = direction.turnRight(); // then turns right
        position = position.forward(direction); // and moves forward

        return new Heading()
                .setParameter(direction)
                .getAction();
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

        return new Echo()
                .setParameter(direction)
                .getAction();

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

        return new Echo()
                .setParameter(direction.turnLeft())
                .getAction();

    }

    public JSONObject echoRight() {

        return new Echo()
                .setParameter(direction.turnRight())
                .getAction();

    }

    public JSONObject scan() {
        return new Scan()
                .getAction();
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