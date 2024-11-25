package org.jsnake;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Point;
import java.util.Map;

public class SnakeGame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Board gameBoard;
    private LeaderBoard leaderBoard;
    private ScoreKeeper scoreKeeper;
    private Settings settings;
    private SettingsPage settingsPage;
    private Color boardColor;
    

    public SnakeGame() {
        scoreKeeper = new ScoreKeeper();
        settings = new Settings();
        boardColor = new Color(0, 0, 0);
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MainMenu mainMenu = new MainMenu(
            e -> showGameBoard(),
            e -> showLeaderBoard(),
            e -> showSettings(),
            e -> System.exit(0)
        );

        gameBoard = new Board(scoreKeeper, boardColor, (Color) settings.getSettings().getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN), (Color) settings.getSettings().getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED));
        leaderBoard = new LeaderBoard(scoreKeeper);
        settingsPage = new SettingsPage(settings);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(gameBoard, "GameBoard");
        mainPanel.add(leaderBoard, "LeaderBoard");
        mainPanel.add(settingsPage, "SettingsPage");

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

    private void applySettings() {
        Map<String, Object> currentSettings = settings.getSettings();
        
        Color boardColor = (Color) currentSettings.getOrDefault(Settings.BOARD_COLOR, Color.BLACK);
        Color playerColor = (Color) currentSettings.getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN);
        Color aiColor = (Color) currentSettings.getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED);
        
        gameBoard.setBackgroundColor(boardColor);
        gameBoard.setPlayerSnakeColor(playerColor);
        gameBoard.setAiSnakeColor(aiColor);
    }

    private void showGameBoard() {
        applySettings(); // Apply settings before resetting game
        gameBoard.resetGame();
        cardLayout.show(mainPanel, "GameBoard");
        gameBoard.requestFocusInWindow();
    }

    private void showSettings() {
        cardLayout.show(mainPanel, "SettingsPage");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new SnakeGame();
            ex.setVisible(true);
        });
    }
}