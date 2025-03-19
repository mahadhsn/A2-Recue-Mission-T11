package ca.mcmaster.se2aa4.island.team011.Decider;

import org.json.JSONObject;

// does not need to be abstract as it can only fly in one direction
public class Fly {
    JSONObject action;
    
    public Fly() {
        action = new JSONObject();
        action.put("action", "fly");
    }

    public JSONObject getAction() {
        return action;
    }

    public String actionToString(JSONObject action) {
        return action.toString();
    }
}
