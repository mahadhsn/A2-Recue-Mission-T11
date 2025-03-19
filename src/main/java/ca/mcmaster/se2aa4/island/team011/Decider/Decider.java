package ca.mcmaster.se2aa4.island.team011.Decider;

import org.json.JSONObject;


// Decider determines next action drone should take and returns it
public class Decider {
    private JSONObject decision;

    public Decider(){
        this.decision = new JSONObject();
    }

    public JSONObject getDecision(){

        decision.put("action", "fly"); // TEMPORARY

        return decision;

    }

}