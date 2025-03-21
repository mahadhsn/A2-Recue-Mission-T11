package ca.mcmaster.se2aa4.island.team011.Decider;

// Scan action extends Action
public class Scan extends Action {
    
    public Scan() { 
        getAction() // get the action JSON object
        .put("action", "scan"); // put the action as scan
    }
}