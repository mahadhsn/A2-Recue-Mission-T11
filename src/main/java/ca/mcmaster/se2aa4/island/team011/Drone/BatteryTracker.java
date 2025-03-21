package ca.mcmaster.se2aa4.island.team011.Drone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BatteryTracker {
    private static final Logger logger = LogManager.getLogger(BatteryTracker.class);
    private int batteryLevel; //current battery level of drone

    public BatteryTracker(int batteryLevel){
        if (batteryLevel < 0){
            throw new IllegalArgumentException("Battery cannot be negative.");
        }
        this.batteryLevel = batteryLevel; 
        logger.info("Battery initialized with {} units.", batteryLevel);

    }

    public boolean hasEnoughBattery(int cost){
        return batteryLevel >= cost;
    }

    public void consumeBattery(int cost){
        if (hasEnoughBattery(cost)){
            batteryLevel -= cost;
            logger.info("Battery level reduced by {}. Remaining: {}", cost, batteryLevel);
        } else{
            logger.info("Not enough battery to perform this action. {} units remaining.", batteryLevel);
        }
        
    }

    public int getBatteryLevel(){
        return batteryLevel; 
    }

    public boolean isDepleted(){
        return batteryLevel == 0;
    }

}
