package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SettingsPage extends JPanel {
    private final Settings settings;
    private Color boardColor;
    private Color playerSnakeColor;
    private Color aiSnakeColor;

    SettingsPage(Settings settings) {
        this.settings = settings;
        loadCurrentSettings();
        setupUI();
    }

    private void loadCurrentSettings() {
        Map<String, Object> currentSettings = settings.getSettings();
        boardColor = (Color) currentSettings.getOrDefault(Settings.BOARD_COLOR, Color.BLACK);
        playerSnakeColor = (Color) currentSettings.getOrDefault(Settings.PLAYER_SNAKE_COLOR, Color.GREEN);
        aiSnakeColor = (Color) currentSettings.getOrDefault(Settings.AI_SNAKE_COLOR, Color.RED);
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Board Color:"), gbc);
        gbc.gridx = 1;
        add(createColorButton("Board Color", boardColor, color -> {
            boardColor = color;
            settings.getSettings().put(Settings.BOARD_COLOR, color);
            settings.saveSettings();
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Player Snake Color:"), gbc);
        gbc.gridx = 1;
        add(createColorButton("Player Snake Color", playerSnakeColor, color -> {
            playerSnakeColor = color;
            settings.getSettings().put(Settings.PLAYER_SNAKE_COLOR, color);
            settings.saveSettings();
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("AI Snake Color:"), gbc);
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
        backButton.addActionListener(e -> returnToMainMenu());
        add(backButton, gbc);
    }

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
        button.setPreferredSize(new Dimension(60, 30));
        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose " + name, button.getBackground());
            if (newColor != null) {
                button.setBackground(newColor);
                listener.onColorChanged(newColor);
            }
        });
        return button;
    }

    private void returnToMainMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof SnakeGame) {
            ((SnakeGame) topFrame).showMainMenu();
        }
    }

    @FunctionalInterface
    interface ColorChangeListener {
        void onColorChanged(Color color);
    }
}
