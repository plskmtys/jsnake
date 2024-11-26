package org.jsnake;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import java.util.Map;
import java.awt.Image;

/**
 * A játék fő osztálya, amely a játékot, a ranglistát és a beállításokat tartalmazza.
 */
public class SnakeGame extends JFrame {

    /**
     * CardLayout, amely a különböző panelek közötti váltást teszi lehetővé.
     */
    private CardLayout cardLayout;

    /**
     * A fő panel, amely a különböző panelek közötti váltást teszi lehetővé.
     */
    private JPanel mainPanel;

    /**
     * A játékot tartalmazó panel.
     */
    private Board gameBoard;

    private BoardPanel boardPanel;

    /**
     * A ranglistát tartalmazó panel.
     */
    private LeaderBoard leaderBoard;

    /**
     * A pontokat kezelő osztály.
     */
    private ScoreKeeper scoreKeeper;

    /**
     * A beállításokat tartalmazó osztály.
     */
    private Settings settings;

    /**
     * A beállításokat tartalmazó panel.
     */
    private SettingsPage settingsPage;
    
    /**
     * A SnakeGame osztály konstruktora.
     */
    public SnakeGame() {
        scoreKeeper = new ScoreKeeper();
        settings = new Settings();
        initUI();
    }

    /**
     * Inicializálja a felhasználói felületet.
     */
    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MainMenu mainMenu = new MainMenu(
            e -> showBoardPanel(),
            e -> showLeaderBoard(),
            e -> showSettings(),
            e -> System.exit(0)
        );

        gameBoard = new Board(scoreKeeper, (Color) settings.getSettings().getOrDefault(Settings.BOARD_COLOR, Color.BLACK), (Color) settings.getSettings().getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN), (Color) settings.getSettings().getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED));
        boardPanel = new BoardPanel(gameBoard);
        leaderBoard = new LeaderBoard(scoreKeeper);
        settingsPage = new SettingsPage(settings);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(boardPanel, "BoardPanel");
        mainPanel.add(leaderBoard, "LeaderBoard");
        mainPanel.add(settingsPage, "SettingsPage");

        add(mainPanel);

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        Image icon = new ImageIcon("src/main/resources/snake_icon_transparent_90.png").getImage();
        setIconImage(icon);
    }

    /**
     * A ranglista megjelenítése.
     */
    private void showLeaderBoard() {
        leaderBoard.updateLeaderboard();
        cardLayout.show(mainPanel, "LeaderBoard");
    }

    /**
     * A főmenü megjelenítése.
     */
    public void showMainMenu() {
        cardLayout.show(mainPanel, "MainMenu");
    }

    /**
     * A beállítások alkalmazása.
     */
    private void applySettings() {
        Map<String, Object> currentSettings = settings.getSettings();
        
        Color boardColor = (Color) currentSettings.getOrDefault(Settings.BOARD_COLOR, Color.BLACK);
        Color playerColor = (Color) currentSettings.getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN);
        Color aiColor = (Color) currentSettings.getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED);
        
        gameBoard.setBackgroundColor(boardColor);
        gameBoard.setPlayerSnakeColor(playerColor);
        gameBoard.setAiSnakeColor(aiColor);
    }

    /**
     * A játékteret megjelenítő metódus.
     */
    private void showBoardPanel() {
        applySettings();
        gameBoard.resetGame();
        cardLayout.show(mainPanel, "BoardPanel");
        gameBoard.requestFocusInWindow();
    }

    /**
     * A beállítások megjelenítése.
     */
    private void showSettings() {
        cardLayout.show(mainPanel, "SettingsPage");
    }

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            JFrame ex = new SnakeGame();
            ex.setVisible(true);
        });
    }
}