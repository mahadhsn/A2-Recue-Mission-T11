package ca.mcmaster.se2aa4.island.team011.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BatteryTracker {
    private static final Logger logger = LogManager.getLogger(BatteryTracker.class);
    private int batteryLevel; //current battery level of drone

    public BatteryTracker(int batteryLevel){
        this.batteryLevel = batteryLevel;
        logger.info("Battery initialized with {} units.", batteryLevel);

    }

    public void consumeBattery(int amountUsed){
        batteryLevel -= amountUsed;  //reduces battery by a certain amount depending on the drone action
        logger.info("Battery level reduced by {}. Remaining: {}", amountUsed, batteryLevel);
    }

    public int getBatteryLevel(){
        return batteryLevel; 
    }

}
