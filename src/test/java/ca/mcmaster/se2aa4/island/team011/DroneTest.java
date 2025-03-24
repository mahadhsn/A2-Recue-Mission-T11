package ca.mcmaster.se2aa4.island.team011;

import ca.mcmaster.se2aa4.island.team011.Drone.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;

public class DroneTest {

    private Drone drone;

    @BeforeEach
    void setUp() {
        drone = new Drone("E");
    }

    @Test
    void testInitialization() {
        Position pos = drone.getPosition();
        assertEquals(1, pos.getX());
        assertEquals(1, pos.getY());
    }
    

    @Test
    void testFly() {
        drone.fly();
        Position pos = drone.getPosition();
        assertEquals(2, pos.getX());
        assertEquals(1, pos.getY());
    }
    

    @Test
    void testHeadingLeft() {
        drone.headingLeft();
        assertEquals(Direction.N, drone.getDirection()); // Ensure the drone turns correctly
        Position pos = drone.getPosition();
        assertEquals(2, pos.getX());
        assertEquals(0, pos.getY()); // Moves forward before turning
    }
    

    @Test
    void testHeadingRight() {
        drone.headingRight();
        assertEquals(Direction.S, drone.getDirection()); 
        Position pos = drone.getPosition();
        assertEquals(2, pos.getX());
        assertEquals(2, pos.getY()); 
    }
    

    @Test
    void testHeadingOnDirection() {
        drone.headingOnDirection(Direction.S);
        assertEquals(Direction.S, drone.getDirection()); 
        Position pos = drone.getPosition();
        assertEquals(1, pos.getX()); 
        assertEquals(3, pos.getY()); 
    }
    

    @Test
    void testStop() {
        JSONObject action = drone.stop();
        assertEquals("stop", action.getString("action"));
    }


    @Test
    void testScan() {
        JSONObject action = drone.scan();
        assertEquals("scan", action.getString("action"));
    }

    @Test
    void testSetAndGetDecision() {
        JSONObject decision = new JSONObject().put("action", "FLY");
        drone.setDecision(decision);
        assertEquals(decision, drone.getDecision());
        assertEquals("FLY", drone.getPrevDecision());
    }

    @Test
    void testDirectionTurning() {
        assertEquals(Direction.S, drone.getRightDirection());
        assertEquals(Direction.N, drone.getLeftDirection());
        assertEquals(Direction.W, drone.getUTurnDirection());
    }
}
