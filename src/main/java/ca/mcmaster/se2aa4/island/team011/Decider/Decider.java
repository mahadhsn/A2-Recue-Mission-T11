package ca.mcmaster.se2aa4.island.team011.Decider;


import ca.mcmaster.se2aa4.island.team011.Drone.*;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// Decider determines next action drone should take and returns it
public class Decider {

    private final Logger logger = LogManager.getLogger();

    private Drone drone;
    private Reciever reciever;

    private int verticalRangeToLand = 0;

    // Stage flags
    private boolean stage1 = true;
    private boolean stage11 = true;
    private boolean stage12 = false;
    private boolean stage13 = false;

    private boolean stage2 = false;
    private boolean stage21 = false;

    private boolean stage3 = false;

    private boolean foundLand = false;
    private boolean onLand = false;
    private boolean foundSite = false;

    // Flag to track if decision is already made
    private boolean decisionMade = false;

    public Decider(Drone drone, Reciever reciever) {
        this.drone = drone;
        this.reciever = reciever;
    }

    public void decide() {
        if (decisionMade) {
            return;  // Skip if decision already made
        }
        
        logger.info("Deciding action");
        
        if (stage1) { // Stage 1: Find land vertically
            if (stage11) {
                logger.info("Currently in stage 11");
                logger.info("currently at position: " + drone.getCoords());
                findLandSt11();
            } else if (stage12) {
                logger.info("Currently in stage 12");
                logger.info("currently at position: " + drone.getCoords());
                findRightSideSt12();
            } else if (stage13) {
                logger.info("Currently in stage 13");
                logger.info("currently at position: " + drone.getCoords());
                findLandSt13();
            }
        } else if (stage2) {  // Stage 2: Look for site
            if (stage21) {
                logger.info("Currently in stage 21");
                logger.info("currently at position: " + drone.getCoords());
                findSiteSt2();
            }
        } else if (stage3) {  // Stage 3: Stop actions
            logger.info("Currently in stage 3");
            logger.info("currently at position: " + drone.getCoords());
            drone.setDecision(drone.stop());
            decisionMade = true; // Ensure only one decision is made
        }

        // Mark decision as made after processing a stage
        decisionMade = true;
    }

    public void findLandSt11() { // Find first piece of land vertically by flying and echos
        if (reciever.facingGround()) { // move to stage 12 if land found
            foundLand = true;
            stage11 = false;
            stage12 = true;
            drone.setDecision(drone.fly());
            verticalRangeToLand = reciever.getRange();
            decisionMade = true; // Mark decision as made
            return;
        } 
        if (drone.getPrevDecision().equals("fly")) {
            drone.setDecision(drone.echoRight());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.fly());
            decisionMade = true; // Mark decision as made
        }
    }

    public void findRightSideSt12() { // Find right side of land by flying and echos
        if (drone.getPrevDecision().equals("fly")) {
            drone.setDecision(drone.echoRight());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.fly());
            decisionMade = true; // Mark decision as made
        }
        if (!reciever.facingGround() && drone.getPrevDecision().equals("echo")) { // move to stage 13 if no land found
            foundLand = false;
            stage12 = false;
            stage13 = true;
            drone.setDecision(drone.headingRight());
            decisionMade = true; // Mark decision as made
            return;
        } 
    }

    public void findLandSt13() { // Find land vertically
        if (verticalRangeToLand == 0) {
            stage1 = false;
            stage13 = false;
            stage2 = true;
            stage21 = true;
            foundLand = true;
            drone.setDecision(drone.headingRight());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.fly());
            verticalRangeToLand--;
            decisionMade = true; // Mark decision as made
            logger.info("Vertical range to land: " + verticalRangeToLand);
        }
    }

    public void findSiteSt2() { // Find site
        if (!reciever.overGround() && drone.getPrevDecision().equals("scan")) { // move to stage 3 if no site found
            foundSite = true;
            stage21 = false;
            stage2 = false;
            stage3 = true;
            drone.setDecision(drone.stop());
            decisionMade = true; // Mark decision as made
        } 
        if (drone.getPrevDecision().equals("fly")) {
            drone.setDecision(drone.scan());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.fly());
            decisionMade = true; // Mark decision as made
        }
    }

    // Reset decisionMade flag for the next decision cycle
    public void resetDecisionFlag() {
        decisionMade = false;
    }

}