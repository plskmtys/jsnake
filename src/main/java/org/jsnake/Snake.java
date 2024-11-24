package org.jsnake;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Board gameBoard;
    private LeaderBoard leaderBoard;
    private ScoreKeeper scoreKeeper;

    public Snake() {
        scoreKeeper = new ScoreKeeper();
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MainMenu mainMenu = new MainMenu(
            e -> showGameBoard(),
            e -> showLeaderBoard(),
            e -> System.exit(0)
        );

        // Pass the scoreKeeper to Board
        gameBoard = new Board(scoreKeeper);  // Modified line
        leaderBoard = new LeaderBoard(scoreKeeper);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(gameBoard, "GameBoard");
        mainPanel.add(leaderBoard, "LeaderBoard");

        add(mainPanel);

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    private void showLeaderBoard() {
        leaderBoard.updateLeaderboard();
        cardLayout.show(mainPanel, "LeaderBoard");
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "MainMenu");
    }

    private void showGameBoard() {
        gameBoard.resetGame(); // Reset the game state
        cardLayout.show(mainPanel, "GameBoard");
        gameBoard.requestFocusInWindow(); // Request focus for the game board
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}