package ca.mcmaster.se2aa4.island.team011.Decider;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;


// Decider determines next action drone should take and returns it
public class Decider {

    private final Logger logger = LogManager.getLogger(Decider.class);

    protected Drone drone;
    protected Reciever reciever;

    protected int state = 0;
    protected int subState = 0;
    protected int counter = 0;

    protected boolean foundLand = false;
    protected boolean onLand = false;
    protected boolean foundSite = false;

    // Flag to track if decision is already made
    protected boolean decisionMade = false;

    private FindIsland findIsland;

    public Decider(Drone drone, Reciever reciever) {
        this.drone = drone;
        this.reciever = reciever;
        findIsland = new FindIsland(drone, reciever);
    }

    public void decide() {
        logger.info("Deciding island");
        findIsland.decide();
    }

    public void resetSubState() {
        subState = 0;
    }

    public void resetCounter() {
        counter = 0;
    }

    public Drone getDroneInstance() {
        return drone;
    }

    public Reciever getRecieverInstance() {
        return reciever;
    }
}