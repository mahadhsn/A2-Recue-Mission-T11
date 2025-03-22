package ca.mcmaster.se2aa4.island.team011.Decider;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

// Reciever breaks down info returned from decision action
public class Reciever{
    private static final Logger logger = LogManager.getLogger(Reciever.class);


    public Reciever(){


    }
    /*
     *  public void intakeResponse(JSONObject response, Drone drone){
        JSONObject extras = response.getJSONObject("extras");
        if (extras.has("found")) { // bc found is only returned for scan, this is like if last action was scan
            int range = extras.getInt("range");
            String found = extras.getString("found");

            logger.info("Range: {}", range);
            logger.info("Found: {}", found);

            if(found.equals("GROUND")){ // stop drone is ground is found
                drone.setDecision(drone.stop());
            }

            else{ // move drone towards bottom right
                if(drone.getHeading().equals("S")){
                    drone.setDecision(drone.headingLeft());
                }
                else{
                    drone.setDecision(drone.headingRight());
                }
                
            }
        }


        // no ground found, alternate between scanning for ground and moving
        else if (drone.getPrevDecision().equals("heading")) {
            logger.info("Prev decision: {}", drone.getPrevDecision());
            drone.setDecision(drone.echoStraight());
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
     */
   
}
