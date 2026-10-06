import java.util.Random;

/**
 * Random strategy selects a random move for the computer.

 * is used 30% of the time.
 */
public class RandomStrategy implements Strategy {

    private final Random rand = new Random();

    /**
     * Returns a random computer move.

     * ignored for this strategy
     *  "R", "P", or "S" randomly
     */
    @Override
    public String getMove(String playerMove) {
        int r = rand.nextInt(3);
        return switch (r) {
            case 0 -> "R";
            case 1 -> "P";
            default -> "S";
        };
    }
}
