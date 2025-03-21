package ca.mcmaster.se2aa4.island.team011.Decider;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import org.json.JSONObject;

// Echo action extends Action
public class Echo extends Action {
    JSONObject parameter; // parameter for the echo action
    
    public Echo() {
        parameter = new JSONObject(); // create a new JSON object for the parameter
        getAction().put("action", "echo"); // put the action as echo
    }

    public Echo setParameter(Direction dir) { // return Echo for fluent
        parameter.put("direction", dir); // put the direction in the parameter JSON object

        getAction()
        .put("parameters", parameter); // put the parameter in the action JSON object
        return this;
    }
}
