package ca.mcmaster.se2aa4.island.team011.Decider;


import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;


// Decider determines next action drone should take and returns it
public class Decider {

    private final Logger logger = LogManager.getLogger();

    protected Drone drone;
    protected Reciever reciever;

    private int verticalRangeToLand = 0;

    // Stage flags
    private boolean stage1 = true;
    private boolean stage11 = true;
    private boolean stage12 = false;
    private boolean stage13 = false;
    private boolean stage14 = false;
    private boolean stage15 = false;

    private boolean stage2 = false;

    private boolean stage21 = false;
    private boolean stage22 = false;
    private boolean stage23 = false;
    private boolean stage24 = false;
    private boolean stage25 = false;

    private boolean stage3 = false;

    protected boolean foundLand = false;
    protected boolean onLand = false;
    protected boolean foundSite = false;

    // Flag to track if decision is already made
    protected boolean decisionMade = false;

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
                return;
            } else if (stage12) {
                logger.info("Currently in stage 12");
                logger.info("currently at position: " + drone.getCoords());
                findRightSideSt12();
                return;
            } else if (stage13) {
                logger.info("Currently in stage 13");
                logger.info("currently at position: " + drone.getCoords());
                findShoreSt13();
                return;
            } else if (stage14) {
                logger.info("Currently in stage 14");
                logger.info("currently at position: " + drone.getCoords());
                faceNorthSt14(); 
                return;
            }
            else if (stage15) {
                logger.info("Currently in stage 15");
                logger.info("currently at position: " + drone.getCoords());
                findLandSt15();
                return;
            }
            
        } 
        else if (stage2) {  // Stage 2: Find site
            if (stage21) {
                logger.info("Currently in stage 21");
                logger.info("currently at position: " + drone.getCoords());
                faceNorthSt21();
                return;
            }
            else if (stage22) {
                logger.info("Currently in stage 22");
                logger.info("currently at position: " + drone.getCoords());
                scanColSt22();
                return;
            }
            else if (stage23) {
                logger.info("Currently in stage 23");
                logger.info("currently at position: " + drone.getCoords());
                faceSouthSt23();
                return;
            }
            else if (stage24) {
                logger.info("Currently in stage 24");
                logger.info("currently at position: " + drone.getCoords());
                scanColSt24();
                return;
            }

        }

        else if (stage3) {
            drone.setDecision(drone.stop());
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

    public void findShoreSt13() { // Find land vertically
        if (verticalRangeToLand == 0) {
            stage13 = false;
            stage14 = true;
            foundLand = true;
            drone.setDecision(drone.headingRight());
            decisionMade = true; // Mark decision as made
            return;
        } 
        else {
            drone.setDecision(drone.fly());
            verticalRangeToLand--;
            decisionMade = true; // Mark decision as made
            logger.info("Vertical range to land: " + verticalRangeToLand);
        }
    }

    public void faceNorthSt14() {
        drone.setDecision(drone.headingRight());
        decisionMade = true; // Mark decision as made
        stage14 = false;
        stage15 = true;

    }

    public void findLandSt15() { // Find site
        if (reciever.overGround() && drone.getPrevDecision().equals("scan")) { // move to stage 3 if no site found
            foundLand = true;
            stage1 = false;
            stage15 = false;
            stage2 = true;
            stage21 = true;
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

    public void faceNorthSt21() {
        drone.setDecision(drone.headingRight());
        decisionMade = true; // Mark decision as made
        stage21 = false;
        stage22 = true;

    }

    public void scanColSt22() {
        if (!reciever.overGround() && drone.getPrevDecision().equals("scan")) {
            stage22 = false;
            stage23 = true;
            drone.setDecision(drone.headingLeft());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.scan());
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

    public void faceSouthSt23() {
            drone.setDecision(drone.headingLeft());
            decisionMade = true; // Mark decision as made
            stage23 = false;
            stage24 = true;
    }

    public void scanColSt24() {
        if (reciever.overGround() && drone.getPrevDecision().equals("scan")) {
            stage24 = false;
            stage2 = false;
            stage3 = true;

            drone.setDecision(drone.headingRight());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.scan());
            decisionMade = true; // Mark decision as made
        }

        if (drone.getPrevDecision().equals("scan")) {
            drone.setDecision(drone.fly());
            decisionMade = true; // Mark decision as made
        } 
        else {
            drone.setDecision(drone.scan());
            decisionMade = true; // Mark decision as made
        }
    }

    public void scanCol() {

    }

    // Reset decisionMade flag for the next decision cycle
    public void resetDecisionFlag() {
        decisionMade = false;
    }
}