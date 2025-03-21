package ca.mcmaster.se2aa4.island.team011.Map;
import ca.mcmaster.se2aa4.island.team011.*;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


public class POI {
    private static final Logger logger = LogManager.getLogger(POI.class);
    // map of creek ID and position
    private Map<String, Position> creeks;
    private String siteID;
    private Position siteCoord;

    public POI(){
        creeks = new HashMap<>();
    }

    public void update(JSONObject extras, Drone drone){
        updateCreeks(extras.getJSONArray("creeks"), drone);
        updateSite(extras.getJSONArray("sites"), drone);
    }

    public void updateCreeks(JSONArray creekList, Drone drone){ // updating creek list
        for(int i=0; i<creekList.length(); i++){
            String creekID = creekList.getString(i);
            if(creeks.containsValue(creekID)){
                logger.info("Creek with ID {} is already found", creekID);
            }
            else{
                // drone pos is pos of creek
                Position creekCoord = new Position(drone.getX(), drone.getY());
                creeks.put(creekID, creekCoord);
            }
        }
    }

    public void updateSite(JSONArray siteList, Drone drone){
        if(siteList.length() > 0){
            siteID = siteList.getString(0);
            siteCoord = new Position(drone.getX(), drone.getY());
        }
    }

    public String getCreeks(){ // return creeks as string
        return String.join(", ", creeks.keySet());
    }

    public String getSite(){ // return creeks as string
        return siteID + siteCoord.toString();
    }

}
