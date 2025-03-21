package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import org.json.JSONObject;


// Decider determines next action drone should take and returns it
public class Decider {

    private JSONObject decision;
    private JSONObject lastDecision;
    private Drone drone;

    public Decider() {
        this.decision = new JSONObject();
        this.drone = drone;
    }

    public String getDecision() {

        decision.put("action", "fly"); // TEMPORARY

        setDecision(decision);

        return decision.toString();
    }

    public void setDecision(JSONObject decision) {
        lastDecision = decision;
    }

}