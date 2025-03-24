package ca.mcmaster.se2aa4.island.team011.Map;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;

public class POI {
    private static final Logger logger = LogManager.getLogger(POI.class);
    // map to store creek IDs and their position
    private Map<String, Position> creeks;
    // emergency site ID and position
    private String siteID = "";
    private Position siteCoord;

    public POI(){
        creeks = new HashMap<>();
    }

    // update called after every response recieved for now
    public void update(JSONObject extras, Drone drone){
        updateCreeks(extras.getJSONArray("creeks"), drone);
        updateSite(extras.getJSONArray("sites"), drone);
    }

    public void updateCreeks(JSONArray creekList, Drone drone){ // updating creek list
        for(int i=0; i<creekList.length(); i++){
            String creekID = creekList.getString(i);
            if(creeks.containsKey(creekID)){
                logger.debug("Creek with ID {} is already found", creekID);
            }
            else{
                logger.debug("Creek ID found", creekID);
                // drone pos is pos of creek
                Position creekCoord = new Position(drone.getX(), drone.getY());
                creeks.put(creekID, creekCoord);
            }
        }
    }

    public boolean creekFound() {
        return !creeks.isEmpty();
    }

    public boolean siteFound() {
        return !siteID.isEmpty();
    }

    public void updateSite(JSONArray siteList, Drone drone){
        if(!siteList.isEmpty()){ // only one emergency site per map
            logger.debug("Site ID found", siteID);
            siteID = siteList.getString(0);
            siteCoord = new Position(drone.getX(), drone.getY());
        }
    }

    public String getCreeks(){ // return creeks as string
        return String.join("\n", creeks.keySet());
    }

    public String getSite(){ // return site as string
        return siteID + " at " + siteCoord.toString();
    }

    public String getResult(){
        return new StringBuilder()
            .append("Creeks found: ")
            .append(getCreeks())
            .append("\nEmergency site found: ")
            .append(getSite())
            .append("\nClosest creek to the site: ")
            .append(findClosestCreek())
            .toString();
    }

    public String findClosestCreek(){
        double shortestDistance = Double.MAX_VALUE;
        String closestCreek = "";
        int x1 = siteCoord.getX();
        int y1 = siteCoord.getY();
        for(Map.Entry<String,Position> list : creeks.entrySet()) {
            int x2 = list.getValue().getX();
            int y2 = list.getValue().getY();
            double distance = Math.hypot(x1-x2, y1-y2);

            if(distance < shortestDistance){
                shortestDistance = distance;
                closestCreek = list.getKey();
            }
        }
        return closestCreek;
    }

}
