package ca.mcmaster.se2aa4.island.team011;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Map.POI;

// Reciever breaks down info returned from decision action
public class Reciever{
    private static final Logger logger = LogManager.getLogger(Reciever.class);
    private JSONObject extras; // extras read from response
    private JSONArray biomes;
    private JSONArray creeks;
    private JSONArray sites;
    private POI pois;

    public Reciever(){
        this.extras = new JSONObject();
        this.biomes = new JSONArray();
        this.creeks = new JSONArray(); 
        this.sites = new JSONArray();
        this.pois = new POI();
    }

    // parse response for all actions
    public void intakeResponse(JSONObject response, Drone drone){
        this.extras = response.getJSONObject("extras");
        parseScan(drone);
    }

    // parse response for ECHO
    public boolean facingGround(){ // returns whether drone is directly facing ground
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

    public int getRange(){ // range of echo scan - distance away from ground
        int range = extras.getInt("range");
        return range;
    }

    // parse response for SCAN
    public void parseScan(Drone drone){
        if (extras.has("biomes")) { 
            this.biomes = extras.getJSONArray("biomes");
            this.creeks = extras.getJSONArray("creeks");
            this.sites = extras.getJSONArray("sites");
            pois.update(extras, drone);
        }
    }

    public boolean creekFound(){
        if (extras.has("creeks")) { 
            return creeks.length() > 0;
        }
        return false;
    }

    public boolean siteFound(){
        if (extras.has("sites")) { 
            return sites.length() > 0;
        }
        return false;
    }

}
