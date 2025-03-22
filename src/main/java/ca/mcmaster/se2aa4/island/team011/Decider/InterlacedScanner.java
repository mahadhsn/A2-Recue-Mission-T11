package ca.mcmaster.se2aa4.island.team011.Decider;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;
import ca.mcmaster.se2aa4.island.team011.Drone.Drone;
import ca.mcmaster.se2aa4.island.team011.Reciever;

// InterlacedScanner is the decider that takes over when scanning for site
public class InterlacedScanner extends Decider{
    private final Logger logger = LogManager.getLogger(InterlacedScanner.class);

    // map of scanning phases and queue of actions
    private static final Map<Direction, Position> MOVES = new HashMap<>();
    static {
        MOVES.put(Direction.getNorth(), new Position(0, -1));
        MOVES.put(Direction.getEast(), new Position(1, 0));
        MOVES.put(Direction.getWest(), new Position(-1, 0));
        MOVES.put(Direction.getSouth(), new Position(0, 1));
    }

    public InterlacedScanner(Drone drone, Reciever reciever){
        super(drone, reciever);
    }

    
    // FOR NOW - scanner will scan ENTIRE map
    @Override
    public void decide(){
        drone.setDecision(drone.stop());
    }



    



}