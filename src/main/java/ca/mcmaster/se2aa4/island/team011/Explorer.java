package ca.mcmaster.se2aa4.island.team011;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team011.Decider.Decider;
import ca.mcmaster.se2aa4.island.team011.Drone.BatteryTrackListener;
import ca.mcmaster.se2aa4.island.team011.Drone.BatteryTracker;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Map.POI;
import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid, BatteryTrackListener {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    private JSONObject decision;
    private Reciever reciever;
    private Decider decider;
    private POI pois;
    private BatteryTracker batteryTracker;
    private boolean batteryDepleted = false;

    private int counter = 0;

    public Explorer() {
    }

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        
        String direction = info.getString("heading");
        int batteryLevel = info.getInt("budget");
        
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        this.drone = new Drone(direction);
        this.reciever = new Reciever();
        this.decider = new Decider(drone, reciever);
        this.pois = new POI();

        this.batteryTracker = new BatteryTracker(batteryLevel);
        batteryTracker.setListener(this);
    }

    @Override
    public String takeDecision() { // determines next action drone should take and returns it
        if (batteryDepleted){
            logger.info("Battery is depleted.");
            return new JSONObject().put("action", "stop").toString();
        }

        decider.action();
        decision = drone.getDecision();
        counter++;

        if (counter == 1) {
            drone.setDecision(drone.scan());
            decision = drone.getDecision();
        }

        logger.info("Position before decision: {}", drone.getPosition());
        logger.info("** Decision: {}", decision);

        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) { // gets response after the decision action is executed
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        
        reciever.intakeResponse(response, drone, pois);

        logger.info("** Response received:\n"+response.toString(2));

        int cost = batteryTracker.getCost(s);
        batteryTracker.consumeBattery(cost);
        
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        if (counter > 1) {
            decider.decision();
        }
    }

    @Override
    public void onBatteryUpdate(int newBatteryLevel){
        logger.info("Battery updated: {} remaining.", newBatteryLevel);
        if (newBatteryLevel < 50 && !batteryDepleted) {
            batteryDepleted = true;
            onBatteryDepleted();
        }
    }


    @Override
    public void onBatteryDepleted(){
        logger.info("Battery too low. Returning to base.");
    }

    @Override
    public String deliverFinalReport() {
        String report = pois.getResult();
        logger.info("Final report:\n{}", report);
        return report;
    }

}