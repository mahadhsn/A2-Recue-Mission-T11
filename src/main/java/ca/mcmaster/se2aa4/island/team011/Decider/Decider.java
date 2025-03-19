package ca.mcmaster.se2aa4.island.team011.Decider;

import org.json.JSONObject;

// Decider determines next action drone should take and returns it
public class Decider {

    private JSONObject decision;
    private Stop stop;
    private String lastDecision;

    public Decider() {
        this.decision = new JSONObject();
    }

    public String getDecision() {

        decision.put("action", "fly"); // TEMPORARY

        setDecision(decision.toString());

        return decision.toString();
    }

    public void setDecision(String decision) {
        lastDecision = decision;
    }

}