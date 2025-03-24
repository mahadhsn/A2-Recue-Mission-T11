package ca.mcmaster.se2aa4.island.team011;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Map.POI;

// Reciever breaks down info returned from decision action
public class Reciever {
    private static final Logger logger = LogManager.getLogger(Reciever.class);
    private JSONObject extras; // extras read from response
    private JSONArray biomes;
    private JSONArray creeks;
    private JSONArray sites;

    public Reciever(){
        this.extras = new JSONObject();
        this.biomes = new JSONArray();
        this.creeks = new JSONArray(); 
        this.sites = new JSONArray();
    }

    // parse response for all actions
    public void intakeResponse(JSONObject response, Drone drone, POI poi){
        if(response.has("extras")){
            this.extras = response.getJSONObject("extras");
        }
        else{
            logger.info("No extras recieved");
        }
        parseScan(drone, poi);
    }

    // parse response for ECHO
    // CHANGED THIS TO ONLY CHECK IF FACING GROUND AND NOT WORRY ABOUT DISTANCE
    public boolean facingGround() { // returns whether drone is facing ground
        if (extras.has("found")) {
            if (extras.has("range")) {
                int range = extras.getInt("range");
            }
            else {
                logger.warn("Range: 0");
            }

            String found = extras.getString("found");

            // return true if ground is found
            if (found.equals("GROUND")) {
                logger.debug("Found GROUND");
                return true;
            }
        }
        return false;
    }

    public boolean overGround() {
        if (extras.has("biomes")) {
            JSONArray biomes = extras.getJSONArray("biomes");

            return !(biomes.getString(0).equals("OCEAN"));
        }
        return false;
    }

    public int getRange(){ // range of echo scan - distance away from ground
        if(extras.has("range")){
            int range = extras.getInt("range");
            return range;
        }
        return 0; // ERROR?
    }

    // parse response for SCAN
    public void parseScan(Drone drone, POI pois){
        if (extras.has("biomes")) { 
            this.biomes = extras.getJSONArray("biomes");
            this.creeks = extras.getJSONArray("creeks");
            this.sites = extras.getJSONArray("sites");
            pois.update(extras, drone);
        }
    }

    public boolean creekFound(){
        if (extras.has("creeks")) { 
            return !creeks.isEmpty();
        }
        return false;
    }

    public boolean siteFound(){
        if (extras.has("sites")) { 
            return !sites.isEmpty();
        }
        return false;
    }

}