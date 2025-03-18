package ca.mcmaster.se2aa4.island.team011;

import org.json.JSONObject;

public class Drone {
    private Position position;
    private Direction heading;
    
    public Drone(){
        position = new Position(0,0);
        this.heading = Direction.EAST; // random starting dir for now
    }

    public String fly(){ // move drone forward
        position.move(heading);

        return new JSONObject()
                .put("cost", 1)
                .put("status", "OK")
                .put("extras", new JSONObject())
                .toString();
    }

}
