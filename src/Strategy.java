/**
 * Strategy for determining the computer's move
 * in the Rock Paper Scissors game.

 * Implementing this provide different algorithms
 * for selecting the computer's move.
 */
public interface Strategy {

    /**
     * Returns the computer's move based on the player's move.

     * the player's chosen move ("R", "P", or "S")
     * the computer's move ("R", "P", or "S")
     */
    String getMove(String playerMove);
}
