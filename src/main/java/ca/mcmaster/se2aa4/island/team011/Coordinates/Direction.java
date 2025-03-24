package ca.mcmaster.se2aa4.island.team011.Coordinates;
import java.util.HashMap;
import java.util.Map;

// Direction represents the direction the drone is facin
public enum Direction{
        N,
        E,
        W,
        S;

    // stores the result of turning right for each direction
    private static final Map<Direction, Direction> rightTurns = new HashMap<>();
    static {
        rightTurns.put(N, E); 
        rightTurns.put(E, S); 
        rightTurns.put(S, W); 
        rightTurns.put(W, N); 
    }

    // stores the result of turning left
    private static final Map<Direction, Direction> leftTurns = new HashMap<>();
    static {
        leftTurns.put(N, W);  
        leftTurns.put(E, N);  
        leftTurns.put(S, E);  
        leftTurns.put(W, S);  
    }

    private static final Map<Direction, Direction> uTurns = new HashMap<>();
    static {
        uTurns.put(N, S);
        uTurns.put(E, W);
        uTurns.put(W, E);
        uTurns.put(S, N);
    }

    // turnRight returns the direction you would face after turning right
    public Direction turnRight() {
        return rightTurns.get(this); 
    }

    // turnLeft returns the direction after turning left
    public Direction turnLeft() {
        return leftTurns.get(this);
    }

    // uTurn returns direction after a u turn
    public Direction uTurn() {
        return uTurns.get(this);
    }

    public static Direction getNorth(){
        return N;
    }

    public static Direction getSouth(){
        return S;
    }

    public static Direction getWest(){
        return W;
    }

    public static Direction getEast(){
        return E;
    }
}