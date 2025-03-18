package ca.mcmaster.se2aa4.island.team011;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    int i = 0;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        this.drone = new Drone();
    }

    @Override
    public String takeDecision() {

        JSONObject decision = new JSONObject();
        // stop after 100 actions
        if (i == 100) {
            decision.put("action", "stop");
        }
        else if (i % 2 == 0) { // fly when i is even
            decision.put("action", "fly");

            String droneResponse = drone.fly();
            acknowledgeResults(droneResponse);
        }
        else { // scan when i is odd
            decision.put("action", "scan");
        }

        logger.info("** Decision: {}",decision.toString());

        i++;  // increment the action counter

        return decision.toString(); // i think touching this would break everything
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found"; // should be identifier of the creek (inlet) where to send the rescue boat
    }

}
