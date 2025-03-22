package ca.mcmaster.se2aa4.island.team011;

import ca.mcmaster.se2aa4.island.team011.Coordinates.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DirectionTest {

    @Test
    void testTurnRight() {
        assertEquals(Direction.E, Direction.N.turnRight(), "North should turn right to East");
        assertEquals(Direction.S, Direction.E.turnRight(), "East should turn right to South");
        assertEquals(Direction.W, Direction.S.turnRight(), "South should turn right to West");
        assertEquals(Direction.N, Direction.W.turnRight(), "West should turn right to North");
    }

    @Test
    void testTurnLeft() {
        assertEquals(Direction.W, Direction.N.turnLeft(), "North should turn left to West");
        assertEquals(Direction.N, Direction.E.turnLeft(), "East should turn left to North");
        assertEquals(Direction.E, Direction.S.turnLeft(), "South should turn left to East");
        assertEquals(Direction.S, Direction.W.turnLeft(), "West should turn left to South");
    }

    @Test
    void testGetters() {
        assertEquals(Direction.N, Direction.getNorth(), "getNorth() should return N");
        assertEquals(Direction.S, Direction.getSouth(), "getSouth() should return S");
        assertEquals(Direction.W, Direction.getWest(), "getWest() should return W");
        assertEquals(Direction.E, Direction.getEast(), "getEast() should return E");
    }
}
