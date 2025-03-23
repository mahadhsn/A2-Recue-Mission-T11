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
    private final int scanRange;
    protected enum Phase { // Enums should not have access modifiers (like private)
        ECHO, // first echo to get distance
        STRAIGHT,
        UTURN_RIGHT,
        UTURN_LEFT,
        STOP
    }
    private Phase currentPhase;
    private final Map<Phase, Runnable> phaseActions = new HashMap<>();

    private int distance;

    private int state = 1;
    private int subState = 0;
    private int count;


    public InterlacedScanner(Drone drone, Reciever reciever){
        super(drone, reciever);
        InterlacedScanner.initialDir = drone.getDirection();
        this.scanRange = 3; // scanner has 3x3 coverage (was already predetermined for us)
        this.currentPhase = Phase.ECHO;
        this.count = 0;
        this.distance = 0;

        phaseActions.put(Phase.ECHO, this::initialize);
        phaseActions.put(Phase.STRAIGHT, this::straightScan);
        phaseActions.put(Phase.STOP, this::stop);
    }
   

    // FOR NOW - scanner will scan ENTIRE map

    @Override
    public void decide(){
        logger.info("Drone coords: {} Direction: {}", drone.getCoords(), drone.getHeading());
        if (phaseActions.containsKey(currentPhase)) {
            phaseActions.get(currentPhase).run(); // running corresponding method for current phase
        } else {
            logger.info("ERROR. Invalid phase {}.", currentPhase);
        }
        logger.info("Current phase: {}", currentPhase);
    }

    public void initialize(){ // echos to find remaining distance
        distance = 5;
        drone.setDecision(drone.echoStraight());
        nextPhase();
    }

    public void straightScan(){ // only scans when going straight 
        logger.info("Count is: {}", count);
        logger.info("Prev decision: " + drone.getPrevDecision());
        if(drone.getPrevDecision().equals("")){ // to solve issue of nothing starting until a scan has occured
            logger.info("Prev decision: ",drone.getPrevDecision());
            drone.setDecision(drone.echoStraight());
        }
        logger.info("facingground: " + reciever.facingGround());
        logger.info("range: "+ reciever.getRange());
        if(distance!=0){ 
            //logger.info("Starting straight scanning");
            if(count >= scanRange){
                logger.info("Straight scan, scan");
                drone.setDecision(drone.scan());
                count = 0; // reset count
            }
            else{
                logger.info("Straight scanning, flying");
                drone.setDecision(drone.fly());
                logger.info("Count before increment: {}", count);
                count ++;
                logger.info("Count after increment: {}", count);
            }  
            distance --;
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

    public void nextPhase(){ // getting next phase of interlaced scan
        Direction dir = drone.getDirection();
        if(reciever.siteFound()){ // stop if emergency site is found
            this.currentPhase = Phase.STOP;
        }
        else if(currentPhase==Phase.ECHO){
            this.currentPhase = Phase.STRAIGHT;
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
        //count = scanRange; // reset count
    }

    }
