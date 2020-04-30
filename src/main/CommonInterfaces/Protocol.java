package main.CommonInterfaces;

/**
 * protocol shared between the client and the server
 * each will implement this so the protocol is standardized
 */
public interface Protocol
{
    String START_GAME = "startGame";
    String DATA = "data";
    String TURN_LEFT = "turnLeft";
    String TURN_RIGHT = "turnRight";
    String ERROR = "error";
    String END_GAME = "endGame";
    String MESSAGE = "message";
}
