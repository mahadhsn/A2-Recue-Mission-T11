package ca.mcmaster.se2aa4.island.team011;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team011.Decider.Decider;
import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    private Decider decider = new Decider();
    private JSONObject decision;
    private Reciever reciever = new Reciever();


    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        this.drone = new Drone(direction);
    }

    @Override
    public String takeDecision() { // determines next action drone should take and returns it
        decision = decider.getDecision();

        logger.info("** Decision: {}",decision.toString());

        return decision.toString(); 
    }

    @Override
    public void acknowledgeResults(String s) { // gets response after the decision action is executed
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        reciever.intakeResponse(response);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found"; // should be identifier of the creek (inlet) where to send the rescue boat
    }

}
