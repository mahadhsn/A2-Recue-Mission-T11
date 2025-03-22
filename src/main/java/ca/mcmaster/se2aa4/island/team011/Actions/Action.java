package ca.mcmaster.se2aa4.island.team011.Actions;

import org.json.JSONObject;

// must be abstract to be extended for each action
public abstract class Action {
    // action is the JSON object that will be sent to the child class
    private JSONObject action = new JSONObject();
    
    public Action() { } // default constructor

    public JSONObject getAction() { // getter for action
        return action;
    }

    public String actionToString(JSONObject action) { // getter for the action as a string
        return action.toString();
    }
}
