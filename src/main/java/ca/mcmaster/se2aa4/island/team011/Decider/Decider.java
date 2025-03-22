package ca.mcmaster.se2aa4.island.team011.Decider;


import ca.mcmaster.se2aa4.island.team011.Coordinates.*;
import ca.mcmaster.se2aa4.island.team011.Drone.*;
import ca.mcmaster.se2aa4.island.team011.Reciever;
import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// Decider determines next action drone should take and returns it
public class Decider {

    private JSONObject decision;
    private JSONObject lastDecision;

    private final Logger logger = LogManager.getLogger();

    private Drone drone;
    private Position position;
    private Direction direction;
    private Reciever reciever;

    private int actionCount = 0;
    private int verticalRangeToLand = 0;

    private boolean stage1 = true;

    private boolean stage11 = true;
    private boolean stage12 = false;
    private boolean stage13 = false;


    private boolean stage2 = false;

    private boolean stage21 = false;
    private boolean stage22 = false;

    private boolean stage3 = false;

    private boolean foundLand = false;
    private boolean onLand = false;
    private boolean foundSite = false;

    public Decider(Drone drone, Reciever reciever) {
        this.decision = new JSONObject();
        this.drone = drone;
        this.position = drone.getPosition();
        this.direction = drone.getDirection();
        this.reciever = reciever;
    }

    public void decide() {
        logger.info("Deciding action");
        if (stage1) { // find land vertically first
            if (stage11) {
                logger.info("Currently in stage 11");
                logger.info("currently at position: " + drone.getCoords());
                findLandSt11();
            }
            else if (stage12) {
                logger.info("Currently in stage 12");
                logger.info("currently at position: " + drone.getCoords());
                findRightSideSt12();
            }
            else if (stage13) {
                logger.info("Currently in stage 13");
                logger.info("currently at position: " + drone.getCoords());
                findLandSt13();
            }
        }
        else if (stage2) {
            if (stage21) {
                logger.info("Currently in stage 21");
                logger.info("currently at position: " + drone.getCoords());
                findSiteSt2();
            }
        }
        else if (stage3) {
            logger.info("Currently in stage 3");
            logger.info("currently at position: " + drone.getCoords());
            drone.setDecision(drone.stop());
        }

    }

    public void findLandSt11() { // find land vertically
    if (reciever.facingGround()) {
        foundLand = true;
        stage11 = false;
        stage12 = true;

        if (!drone.getPrevDecision().equals("fly")) {
            drone.setDecision(drone.fly());
        }
    } else if (drone.getPrevDecision().equals("fly")) {
        drone.setDecision(drone.echoRight());
    } else {
        drone.setDecision(drone.fly());
    }
}

public void findRightSideSt12() { // find right side of island
    if (!reciever.facingGround()) {
        foundLand = false;
        stage12 = false;
        stage13 = true;

        if (!drone.getPrevDecision().equals("headingRight")) {
            drone.setDecision(drone.headingRight());
        }
    } else if (drone.getPrevDecision().equals("fly")) {
        drone.setDecision(drone.echoRight());
    } else {
        drone.setDecision(drone.fly());
    }
}

    public void findLandSt13() { // get to the rightmost point of the island
        if (verticalRangeToLand == 0) {
            stage1 = false;
            stage13 = false;

            stage2 = true;
            stage21 = true;
            foundLand = true;
            drone.setDecision(drone.headingRight());
        }
        else {
            drone.setDecision(drone.fly());
            verticalRangeToLand--;
        }
    }

    public void findSiteSt2() { // start looking for site
        if (reciever.siteFound()) {
            foundSite = true;
            stage21 = false;
            stage2 = false;
            stage3 = true;
        }
        else if (drone.getPrevDecision().equals("fly")) {
            drone.setDecision(drone.scan());
        }
        else {
            drone.setDecision(drone.fly());
        }
    }


}