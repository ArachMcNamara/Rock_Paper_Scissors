/**
 * Cheat strategy always selects the move that beats the player's move.

 * is used 10% of the time and guarantees a computer win.
 */
public class Cheat implements Strategy {

    /**
     * Determines the computer's move by choosing the move
     * that beats the player's move.

     * the player's move ("R", "P", or "S")
     * the winning move for the computer
     */
    @Override
    public String getMove(String playerMove) {
        return switch (playerMove) {
            case "R" -> "P";
            case "P" -> "S";
            case "S" -> "R";
            default -> "X";
        };
    }
}
