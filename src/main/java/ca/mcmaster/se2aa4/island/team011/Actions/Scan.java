package ca.mcmaster.se2aa4.island.team011.Actions;

// Sets up JSONObject for Scan action
public class Scan extends Action {
    public Scan() { 
        getAction() // get the action JSON object
        .put("action", "scan"); // put the action as scan
    }
}