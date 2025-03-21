package ca.mcmaster.se2aa4.island.team011.Drone;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;
import ca.mcmaster.se2aa4.island.team011.Decider.*;

import org.json.JSONObject;


public class Drone {
    private JSONObject decision;
    private JSONObject nextDecision;
    private Position position;
    private Direction direction;
    
    public Drone(String headingStr){
        this.nextDecision = new JSONObject();
        nextDecision.put("action", "scan");
        this.decision = nextDecision;
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

    public JSONObject echoStraight() {
        JSONObject decision = new Echo()
                                .setParameter(direction)
                                .getAction();

        return decision;

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

    public JSONObject scan(){
        JSONObject decision = new Scan()
                                .getAction();
        return decision;
    }

    public void setDecision(JSONObject decision) {
        this.nextDecision = decision;
    }

    public JSONObject getDecision() {
        return nextDecision;
    }

    public String getPrevDecision() {
        return decision.optString("action", "");
    }

    public String getHeading() {
        return direction.toString();
    }
}