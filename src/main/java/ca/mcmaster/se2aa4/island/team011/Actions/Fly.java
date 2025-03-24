package ca.mcmaster.se2aa4.island.team011.Actions;

// Sets up JSONObject for Fly action
public class Fly extends Action {
    public Fly() {
        getAction() // get the action JSON object
        .put("action", "fly"); // put the action as fly
    }
}