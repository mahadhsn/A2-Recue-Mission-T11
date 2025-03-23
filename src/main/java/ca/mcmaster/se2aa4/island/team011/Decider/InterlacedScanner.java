package ca.mcmaster.se2aa4.island.team011.Decider;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Decider.InterlacedScanner.Phase;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;

// InterlacedScanner is the decider that takes over when scanning for site
public class InterlacedScanner extends Decider{
    private final Logger logger = LogManager.getLogger(InterlacedScanner.class);
    private static Direction initialDir; // starting direction of drone when it gets to island/right before scanning. scan direction will be based off of this
    private int scanRange;
    protected enum Phase { // Enums should not have access modifiers (like private)
        STRAIGHT,
        UTURN_RIGHT,
        UTURN_LEFT,
        STOP
    }
    private Phase currentPhase;
    private final Map<Phase, Runnable> phaseActions = new HashMap<>();
    private int count; // counter to determine when to scan, avoids overlapping scans

    public InterlacedScanner(Drone drone, Reciever reciever){
        super(drone, reciever);
        InterlacedScanner.initialDir = drone.getDirection();
        this.scanRange = 3; // scanner has 3x3 coverage (was already predetermined for us)
        this.currentPhase = Phase.STRAIGHT;
        this.count = scanRange;

        phaseActions.put(Phase.STRAIGHT, this::straightScan);
        phaseActions.put(Phase.STOP, this::stop);
    }
   

    // FOR NOW - scanner will scan ENTIRE map

    @Override
    public void action(){
        if (phaseActions.containsKey(currentPhase)) {
            phaseActions.get(currentPhase).run(); // running corresponding method for current phase
        } else {
            logger.info("ERROR. Invalid phase {}.", currentPhase);
        }
        logger.info("Current phase: {}", currentPhase);
    }

    public void straightScan(){ // only scans when going straight 
        logger.info("Count is: {}", count);
        logger.info("Prev decision: ",drone.getPrevDecision());
        if(drone.getPrevDecision().equals("")){ // to solve issue of nothing starting until a scan has occured
            logger.info("Prev decision: ",drone.getPrevDecision());
            drone.setDecision(drone.echoStraight());
        }
        logger.info("facingground: ",reciever.facingGround());
        logger.info("range: ",reciever.getRange());
        while(reciever.facingGround() && reciever.getRange()!=0){ 
            //logger.info("Starting straight scanning");
            if(count == scanRange){
                logger.info("Straight scan, scan");
                drone.setDecision(drone.scan());
                count = 0; // reset count
            }
            else{
                logger.info("Straight scanning, flying");
                drone.setDecision(drone.fly());
                count ++;
            }  
        } 
        //setPhase();
    }

    public void uturnLeft(){
        if(count == scanRange){
            drone.setDecision(drone.scan());
            count = 0; // reset count
        }
        else{
            drone.setDecision(drone.fly());
            count ++;
        } 
    }

    public void stop(){ // stop scanning
        logger.info("Site found {}", drone.getPosition().toString());
        drone.setDecision(drone.stop());
    }

    public void setPhase(){ // getting next phase of interlaced scan
        Direction dir = drone.getDirection();
        if(reciever.siteFound()){ // stop if emergency site is found
            this.currentPhase = Phase.STOP;
        }
        else if (currentPhase==Phase.STRAIGHT && dir==Direction.getEast()){
            this.currentPhase = Phase.UTURN_LEFT;
        }
        else if (currentPhase==Phase.STRAIGHT && dir==Direction.getWest()){
            this.currentPhase = Phase.UTURN_RIGHT;
        }
        else{
            this.currentPhase = Phase.STRAIGHT;
        }
        count = scanRange; // reset count
    }

}







