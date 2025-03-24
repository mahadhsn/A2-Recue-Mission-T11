package ca.mcmaster.se2aa4.island.team011.Actions;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import org.json.JSONObject;

// Sets up JSONObject for Heading action depending on direction
public class Heading extends Action {
    private final JSONObject parameter; // parameter for the heading action

    public Heading() {
        parameter = new JSONObject(); // create a new JSON object for the parameter
        getAction().put("action", "heading"); // put the action as heading
    }

    public Heading setParameter(Direction dir) { // return Heading for fluent
        parameter.put("direction", dir); // put the direction in the parameter JSON object

        getAction()
        .put("parameters", parameter); // put the parameter in the action JSON object
        return this;
    }
}