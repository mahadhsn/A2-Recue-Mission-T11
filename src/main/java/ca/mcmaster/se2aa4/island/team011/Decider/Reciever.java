package ca.mcmaster.se2aa4.island.team011.Decider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;

// Reciever breaks down info returned from decision action
public class Reciever{
    private static final Logger logger = LogManager.getLogger(Reciever.class);
    private JSONObject extras; // extras read from response

    public Reciever(){
        this.extras = new JSONObject();
    }

    public boolean aboveGround(){ // returns whether drone is directly over ground
        if (extras.has("found")) { 
            int range = extras.getInt("range");
            String found = extras.getString("found");

            logger.info("Range: {}", range);
            logger.info("Found: {}", found);

            if(found.equals("GROUND") && range==0){ // stop drone if drone is directly next to ground
                return true;
            }
        }
    
        return false;
    }

    public int getRange(JSONObject extras){ // range of echo scan - distance away from ground
        int range = extras.getInt("range");
        return range;
    }

    public void intakeResponse(JSONObject response, Drone drone){
        boolean groundFound = false;
        logger.info("Drone is at position {}.", drone.getCoords());
        JSONObject extras = response.getJSONObject("extras");
        
        if (aboveGround(extras) && groundFound) { // bc found is only returned for scan, this is like if last action was scan
            logger.info("Over ground");
            drone.setDecision(drone.stop());
        }
        else if(aboveGround(extras) && groundFound==false){
            logger.info("Found ground, scanning");
            drone.setDecision(drone.scan());
        }
        // no ground found, alternate between scanning for ground and moving
        else if (drone.getPrevDecision().equals("heading")) {
            logger.info("Prev decision: {}", drone.getPrevDecision());
            drone.setDecision(drone.echo());
        }
        else{ // move drone towards bottom right
            logger.info("Prev decision: {}", drone.getPrevDecision());
            logger.info("Drone direction rn is {}", drone.getHeading());
            if(drone.getHeading().equals("S")){
                drone.setDecision(drone.headingLeft());
            }
            else{
                drone.setDecision(drone.headingRight());
            }
        } 
    }
}
