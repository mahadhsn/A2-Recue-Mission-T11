package ca.mcmaster.se2aa4.island.team011.Decider;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Actions.Action;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;

// keeps track of incoming actions/decisions with a queue and sends them one by one to drone
public class DecisionManager{
    private final Logger logger = LogManager.getLogger(DecisionManager.class);
    private Drone drone;
    private Queue<Action> decisions; // actions to be sent to drone

    public DecisionManager(Drone drone){
        this.drone = drone;
        this.decisions = new LinkedList<>();
    }

    public void addDecision(Action action){
        this.decisions.add(action);
    }

    
    public void sendDecision(){
        Action nextAction = decisions.remove(); // getting next action from queue
        drone.setDecision(nextAction.getAction()); // sending the action to drone
    }




    



}