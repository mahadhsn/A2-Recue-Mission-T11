package ca.mcmaster.se2aa4.island.team011.Actions;

// Sets up JSONObject for Stop action
public class Stop extends Action {
    public Stop() {
        getAction() // get the action JSON object
        .put("action", "stop"); // put the action as stop
    }
}