package ca.mcmaster.se2aa4.island.team011.Drone;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;

public class Drone {
    private JSONObject decision;
    private JSONObject nextDecision;
    private JSONObject parameter;
    private Position position;
    private Direction heading;
    
    public Drone(String headingStr){
        this.nextDecision = new JSONObject(); // temp - scan first
        nextDecision.put("action", "scan");
        this.decision = nextDecision;
        this.position = new Position(0,0);
        this.heading = Direction.valueOf(headingStr); 
    }

    public JSONObject fly(){ // move drone forward
        decision = new JSONObject();
        decision.put("action", "fly");

        position = position.forward(heading); // update to new position

        return decision;
    }

    public JSONObject stop() {
        decision = new JSONObject();
        decision.put("action", "stop");
        return decision;
    }

    // refactor heading later
    public JSONObject headingLeft() { // turn left
        decision = new JSONObject();
        parameter = new JSONObject();

        position = position.forward(heading); // drone moves forward
        heading = heading.turnLeft(); // then turns left
        position = position.forward(heading); // and moves forward

        decision.put("action", "heading");
        parameter.put("direction", heading);
        decision.put("parameters", parameter);

        return decision;
    }

    public JSONObject headingRight() { // turn right
        decision = new JSONObject();
        parameter = new JSONObject();

        position = position.forward(heading); // drone moves forward
        heading = heading.turnRight(); // then turns right
        position = position.forward(heading); // and moves forward

        decision.put("action", "heading");
        parameter.put("direction", heading);
        decision.put("parameters", parameter);

        return decision;
    }

    public JSONObject echo() {
        decision.put("action", "echo");
        parameter.put("direction", heading);
        decision.put("parameters", parameter);
        return decision;

    }

    public JSONObject scan(){
        decision = new JSONObject();
        decision.put("action", "scan");
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
        return heading.toString();
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

    

}
