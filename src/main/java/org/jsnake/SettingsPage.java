package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A SettingPage osztály felelős a játék beállításainak megjelenítéséért és kezeléséért.
 */
public class SettingsPage extends JPanel {
    /**
     * Ebben a változóban tároljuk a játék beállításait.
     */
    private final Settings settings;

    /**
     * A tábla színe.
     */
    private Color boardColor;

    /**
     * A játékos kígyójának színe.
     */
    private Color playerSnakeColor;

    /**
     * Az AI kígyójának színe.
     */
    private Color aiSnakeColor;

    /**
     * A SettingsPage osztály konstruktora.
     *
     * @param settings a játék beállításait tartalmazó objektum
     */
    SettingsPage(Settings settings) {
        this.settings = settings;
        loadCurrentSettings();
        setupUI();
    }

    /**
     * Betölti a jelenlegi beállításokat.
     */
    private void loadCurrentSettings() {
        Map<String, Object> currentSettings = settings.getSettings();
        boardColor = (Color) currentSettings.getOrDefault(Settings.BOARD_COLOR, Color.BLACK);
        playerSnakeColor = (Color) currentSettings.getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN);
        aiSnakeColor = (Color) currentSettings.getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED);
    }

    /**
     * Beállítja az ablak kinézetét.
     */
    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel boardColorLabel = new JLabel("Board Color");
        boardColorLabel.setPreferredSize(new Dimension(320, 80));
        boardColorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(boardColorLabel, gbc);
        gbc.gridx = 1;
        add(createColorButton("Board Color", boardColor, color -> {
            boardColor = color;
            settings.getSettings().put(Settings.BOARD_COLOR, color);
            settings.saveSettings();
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel playerSnakeColorLabel = new JLabel("Player Snake Color");
        playerSnakeColorLabel.setPreferredSize(new Dimension(320, 80));
        playerSnakeColorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(playerSnakeColorLabel, gbc);
        gbc.gridx = 1;
        add(createColorButton("Player Snake Color", playerSnakeColor, color -> {
            playerSnakeColor = color;
            settings.getSettings().put(Settings.PLAYER_SNAKE_COLOR, color);
            settings.saveSettings();
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel aiSnakeColorLabel = new JLabel("AI Snake Color");
        aiSnakeColorLabel.setPreferredSize(new Dimension(320, 80));
        aiSnakeColorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(aiSnakeColorLabel, gbc);
        gbc.gridx = 1;
        add(createColorButton("AI Snake Color", aiSnakeColor, color -> {
            aiSnakeColor = color;
            settings.getSettings().put(Settings.AI_SNAKE_COLOR, color);
            settings.saveSettings();
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.addActionListener(e -> returnToMainMenu());
        add(backButton, gbc);
    }

    /**
     * Létrehoz egy gombot, amely a szín kiválasztására szolgál.
     * @param name a gomb neve
     * @param initialColor az alapértelmezett szín
     * @param listener a színváltozás eseménykezelője
     * @return a gomb
     */
    private JButton createColorButton(String name, Color initialColor, ColorChangeListener listener) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground());
                g.fillRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        };
        button.setBackground(initialColor);
        button.setPreferredSize(new Dimension(60, 60));
        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose " + name, button.getBackground());
            if (newColor != null) {
                button.setBackground(newColor);
                listener.onColorChanged(newColor);
            }
        });
        return button;
    }

    /**
     * Visszatér a főmenübe.
     */
    private void returnToMainMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof SnakeGame) {
            ((SnakeGame) topFrame).showMainMenu();
        }
    }

    /**
     * Az interfész, amelyet a színváltozás eseménykezelői implementálnak.
     */
    @FunctionalInterface
    interface ColorChangeListener {
        void onColorChanged(Color color);
    }
}
