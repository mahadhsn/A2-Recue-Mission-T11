package ca.mcmaster.se2aa4.island.team011.Decider;

import org.json.JSONObject;

// only 1 stop action
public class Stop {

    JSONObject action;
    
    public Stop() {
        action = new JSONObject();
        action.put("action", "stop");
    }

    public JSONObject getAction() {
        return action;
    }

    public String actionToString(JSONObject action) {
        return action.toString();
    }
}
