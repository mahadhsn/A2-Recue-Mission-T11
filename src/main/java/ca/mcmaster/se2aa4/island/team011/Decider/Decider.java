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

    private FindIsland findIsland = null;
    private InterlacedScanner interlacedScanner = null;

    public Decider(Drone drone, Reciever reciever) { // initialize with drone and reciever
        this.drone = drone;
        this.reciever = reciever;
    }

    // handles all actions
    public void action() {
        if (findIsland == null) { // instantiate findIsland if null
            findIsland = new FindIsland(drone, reciever);
        }

        if (state == 0) { // State 0: Finds island | Uses FindIsland class
            findIsland.action();
        }
        else if (state == 1) { // State 1: Finds site and creeks | Uses InterlacedScanner class
            interlacedScanner.action();
        }
    }

    // handles all decisions and state changes for the actions
    public void decision() {
        if (state == 0) { // State 0: Finds island | Uses FindIsland class
            findIsland.decision();

            if (findIsland.getIslandFound()) { // if isLand found, go to State 1
                state = 1;
                createInterlacedScanner(); // instantiate interlacedScanner
            }
        }
        else if (state == 1) { // State 1: Finds site and creeks | Uses InterlacedScanner class
            interlacedScanner.decision();
        }
    }

    // interlacedScanner instance creation
    public void createInterlacedScanner() {
        interlacedScanner = new InterlacedScanner(drone, reciever);
        logger.debug("Interlaced Scanner created");
    }
    // reset subState to 0
    public void resetSubState() {
        subState = 0;
    }
    // reset counter to 0
    public void resetCounter() {
        counter = 0;
    }
}