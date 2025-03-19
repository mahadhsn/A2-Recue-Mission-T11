package ca.mcmaster.se2aa4.island.team011;

import org.json.JSONObject;

public class Drone {
    private JSONObject decision;
    private Position position;
    private Direction heading;
    
    public Drone(String headingStr){
        position = new Position(0,0);
        this.heading = Direction.valueOf(headingStr); 
    }

    public JSONObject fly(){ // move drone forward
        decision = new JSONObject();
        decision.put("action", "fly");

        position = position.move(heading); // update position

        return decision;
    }

    public JSONObject scan(){
        decision = new JSONObject();
        decision.put("action", "scan");

        return decision;
    }

}
