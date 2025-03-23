package ca.mcmaster.se2aa4.island.team011.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.StringReader;

public class BatteryTracker {
    private static final Logger logger = LogManager.getLogger(BatteryTracker.class);
    private int batteryLevel; //current battery level of drone
    private BatteryTrackListener listener;

    public BatteryTracker(int batteryLevel){
        if (batteryLevel < 0){
            throw new IllegalArgumentException("Battery cannot be negative.");
        }
        this.batteryLevel = batteryLevel; 
        logger.info("Battery initialized with {} units.", batteryLevel);

    }

    public void setListener(BatteryTrackListener listener){
        this.listener = listener;
    }

    public int getCost (String s){
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        //Integer cost = response.getInt("cost");
        return response.getInt("cost");
    }

    public boolean hasEnoughBattery(int cost){
        return batteryLevel >= cost;
    }

    public void consumeBattery(int cost){
        if (hasEnoughBattery(cost)){
            batteryLevel -= cost;
            logger.info("Battery level reduced by {}. Remaining: {}", cost, batteryLevel);
            if (listener != null){
                listener.onBatteryUpdate(batteryLevel);
            }
        } else{
            logger.info("Not enough battery to perform this action. {} units remaining.", batteryLevel);
        }
        
    }

    public int getBatteryLevel(){
        return batteryLevel; 
    }

    public boolean isDepleted(){
        return batteryLevel <= 40;
    }

}
