package ca.mcmaster.se2aa4.island.team011;

import ca.mcmaster.se2aa4.island.team011.Coordinates.Position;
import ca.mcmaster.se2aa4.island.team011.Coordinates.Direction;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    void testForwardEast() {
        Position start = new Position(2, 2);
        Position result = start.forward(Direction.getEast());

        assertEquals(3, result.getX(), "Moving east should increase x by 1");
        assertEquals(2, result.getY(), "Moving east should not change y");
    }

    @Test
    void testForwardNorth() {
        Position start = new Position(2, 2);
        Position result = start.forward(Direction.getNorth());

        assertEquals(2, result.getX(), "Moving north should not change x");
        assertEquals(1, result.getY(), "Moving north should decrease y by 1");
    }

    @Test
    void testForwardSouth() {
        Position start = new Position(2, 2);
        Position result = start.forward(Direction.getSouth());

        assertEquals(2, result.getX(), "Moving south should not change x");
        assertEquals(3, result.getY(), "Moving south should increase y by 1");
    }

    @Test
    void testForwardWest() {
        Position start = new Position(2, 2);
        Position result = start.forward(Direction.getWest());

        assertEquals(1, result.getX(), "Moving west should decrease x by 1");
        assertEquals(2, result.getY(), "Moving west should not change y");
    }
}