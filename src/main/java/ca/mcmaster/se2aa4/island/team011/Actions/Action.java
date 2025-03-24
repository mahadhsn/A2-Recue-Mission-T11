package ca.mcmaster.se2aa4.island.team011.Actions;

import org.json.JSONObject;

// Action abstract class sets the JSONObject skeleton for all actions. All actions extend it
public abstract class Action {
    // action is the JSON object that will be sent to the child class
    private final JSONObject action = new JSONObject();
    
    public Action() { } // default constructor

    public JSONObject getAction() { // getter for action
        return action;
    }
}