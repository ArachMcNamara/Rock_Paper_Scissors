import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame {
    /**
     * Main GUI frame for the Rock Paper Scissors game.

     * This class builds the graphical user interface, handles user input,
     * applies computer strategies, updates statistics, and displays results.

     * It contains three inner strategy classes:
     * LeastUsed: chooses the move that beats the player's least-used move
     * MostUsed: chooses the move that beats the player's most-used move
     * LastUsed: chooses the move that beats the player's previous move

     * External strategies:
     * Cheat
     * RandomStrategy
     */
        // Player stats
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    // Player move history
    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorsCount = 0;
    private String lastPlayerMove = null;

    // GUI components
    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;
    private JTextArea resultsArea;

    // Strategies
    private final Strategy cheat = new Cheat();
    private final Strategy randomStrategy = new RandomStrategy();
    private final Strategy leastUsed = new LeastUsed();
    private final Strategy mostUsed = new MostUsed();
    private final Strategy lastUsed = new LastUsed();

    private final Random rand = new Random();

    public RockPaperScissorsFrame() {
        super("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildGUI();

        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGUI() {
        setLayout(new BorderLayout());

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Move"));

        JButton rockBtn = new JButton("Rock");
        rockBtn.setActionCommand("R");

        JButton paperBtn = new JButton("Paper");
        paperBtn.setActionCommand("P");

        JButton scissorsBtn = new JButton("Scissors");
        scissorsBtn.setActionCommand("S");

        JButton quitBtn = new JButton("Quit");
        quitBtn.addActionListener(_ -> System.exit(0));

        ActionListener playListener = ae -> playRound(ae.getActionCommand());

        rockBtn.addActionListener(playListener);
        paperBtn.addActionListener(playListener);
        scissorsBtn.addActionListener(playListener);

        buttonPanel.add(rockBtn);
        buttonPanel.add(paperBtn);
        buttonPanel.add(scissorsBtn);
        buttonPanel.add(quitBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== STATS PANEL =====
        JPanel statsPanel = new JPanel(new GridLayout(3, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));

        statsPanel.add(new JLabel("Player Wins:"));
        playerWinsField = new JTextField("0");
        playerWinsField.setEditable(false);
        statsPanel.add(playerWinsField);

        statsPanel.add(new JLabel("Computer Wins:"));
        computerWinsField = new JTextField("0");
        computerWinsField.setEditable(false);
        statsPanel.add(computerWinsField);

        statsPanel.add(new JLabel("Ties:"));
        tiesField = new JTextField("0");
        tiesField.setEditable(false);
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.EAST);

        // ===== RESULTS AREA =====
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void playRound(String playerMove) {
        updatePlayerCounts(playerMove);

        Strategy strat = chooseStrategy();
        String computerMove = strat.getMove(playerMove);

        String result = determineWinner(playerMove, computerMove);

        resultsArea.append(result + " (Computer: " + strat.getClass().getSimpleName() + ")\n");

        lastPlayerMove = playerMove;
    }

    private void updatePlayerCounts(String move) {
        switch (move) {
            case "R" -> playerRockCount++;
            case "P" -> playerPaperCount++;
            case "S" -> playerScissorsCount++;
        }
    }

    private Strategy chooseStrategy() {
        int roll = rand.nextInt(100) + 1;

        if (roll <= 10) return cheat;
        if (roll <= 30) return leastUsed;
        if (roll <= 50) return mostUsed;
        if (roll <= 70) return lastUsed;
        return randomStrategy;
    }

    private String determineWinner(String p, String c) {
        if (p.equals(c)) {
            ties++;
            tiesField.setText(String.valueOf(ties));
            return "Tie! Both chose " + moveName(p);
        }

        boolean playerWinsRound =
                (p.equals("R") && c.equals("S")) ||
                        (p.equals("P") && c.equals("R")) ||
                        (p.equals("S") && c.equals("P"));

        if (playerWinsRound) {
            playerWins++;
            playerWinsField.setText(String.valueOf(playerWins));
            return moveName(p) + " beats move " + moveName(c) + ". Player wins!";
        } else {
            computerWins++;
            computerWinsField.setText(String.valueOf(computerWins));
            return moveName(c) + " beats " + moveName(p) + ". Computer wins!";
        }
    }

    private String moveName(String m) {
        return switch (m) {
            case "R" -> "Rock";
            case "P" -> "Paper";
            case "S" -> "Scissors";
            default -> "Unknown";
        };
    }

    // ===== INNER STRATEGIES =====

    private class LeastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            int r = playerRockCount;
            int p = playerPaperCount;
            int s = playerScissorsCount;

            if (r <= p && r <= s) return "P";   // beat Rock
            if (p <= r && p <= s) return "S";   // beat Paper
            return "R";                         // beat Scissors
        }
    }

    private class MostUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            int r = playerRockCount;
            int p = playerPaperCount;
            int s = playerScissorsCount;

            if (r >= p && r >= s) return "P";   // beat Rock
            if (p >= r && p >= s) return "S";   // beat Paper
            return "R";                         // beat Scissors
        }
    }

    private class LastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (lastPlayerMove == null)
                return randomStrategy.getMove(playerMove);

            return switch (lastPlayerMove) {
                case "R" -> "P";
                case "P" -> "S";
                default -> "R";
            };
        }
    }
}
