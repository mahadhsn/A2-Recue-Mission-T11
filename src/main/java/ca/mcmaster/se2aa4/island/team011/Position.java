package ca.mcmaster.se2aa4.island.team011;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Jeslyn Lu
 * lu196
 * Position represents a position in the maze with x and y coordinates
 */

// CAN REPLACE MOST OF IT WITH the bulit in Point of Java 

public class Position {
    private static final Logger logger = LogManager.getLogger();
    private int x; // x coord
    private int y; // y coord
    private static final Map<Direction, Position> MOVES = new HashMap<>(); // direction to move in mapped with Position offsets
    static {
        MOVES.put(Direction.N, new Position(0, -1));
        MOVES.put(Direction.E, new Position(1, 0));
        MOVES.put(Direction.W, new Position(-1, 0));
        MOVES.put(Direction.S, new Position(0, 1));
    }
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // foward moves the position in the given direction and returns a new Position object
    public Position forward(Direction dir) {
        Position offset = MOVES.get(dir);
        if (offset == null) {
            throw new IllegalStateException("Unexpected value: " + dir);
        }
        return new Position(this.x + offset.getX(), this.y + offset.getY());
    }

    // equals checks if this position is equal to inputted position
    public boolean equals(Position pos) {
        return this.x == pos.x && this.y == pos.y;
    }

    // getX returns x coord
    public int getX() {
        return this.x;
    }

    // getY returns y coord
    public int getY() {
        return this.y;
    }

    // toString returns string representation of Position
    @Override
    public String toString() {
        return "Position: (" + this.x + ", " + this.y + ")";
    }
}