package ca.mcmaster.se2aa4.island.team011.Decider;

// Fly action extends Action
public class Fly extends Action {
    
    public Fly() {
        getAction() // get the action JSON object
        .put("action", "fly"); // put the action as fly
    }
}