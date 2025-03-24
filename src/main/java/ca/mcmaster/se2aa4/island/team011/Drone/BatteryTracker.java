package ca.mcmaster.se2aa4.island.team011.Drone;


import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BatteryTracker {
    private static final Logger logger = LogManager.getLogger(BatteryTracker.class);
    private int batteryLevel; //Current battery level of drone
    private BatteryTrackListener listener;


    
    public BatteryTracker(int batteryLevel){
        //If the battery level is less than zero, throw exception
        if (batteryLevel < 0){ 
            throw new IllegalArgumentException("Battery cannot be negative.");
        }
        this.batteryLevel = batteryLevel; 
        logger.debug("Battery initialized with {} units.", batteryLevel);

    }

    public void setListener(BatteryTrackListener listener){
        this.listener = listener;
    }

    public int getCost (String s){
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        return response.getInt("cost");
    }

    //Ensuring that the drone has enough battery for next action
    public boolean hasEnoughBattery(int cost){
        return batteryLevel >= cost;
    }

    private boolean isDepleted = false;

    public void consumeBattery(int cost){
        if (hasEnoughBattery(cost)){
            batteryLevel -= cost;

            if (batteryLevel < 50 && !isDepleted){
                isDepleted = true;
                logger.debug("Battery is too low. Returning to base.");
            }

            //Updating battery level after every action
            if (listener != null){
                listener.onBatteryUpdate(batteryLevel);
                if (isDepleted){
                    listener.onBatteryDepleted();
                }
            }

        } else{
            logger.debug("Not enough battery to perform this action. {} units remaining.", batteryLevel);
        }
        
    }

    public int getBatteryLevel(){
        return batteryLevel; 
    }
}
