package org.jsnake;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * A főmenüt megjelenítő panel.
 */
public class MainMenu extends JPanel {
    /**
     * A menü háttérképe.
     */
    private Image backgroundImage;

    /**
     * A főmenü panel konstruktora.
     * @param playAction A játék indításáért felelős ActionListener.
     * @param leaderBoardAction A ranglista megjelenítéséért felelős ActionListener.
     * @param settingsAction A beállítások megjelenítéséért felelős ActionListener.
     * @param exitAction A kilépésért felelős ActionListener.
     */
    public MainMenu(ActionListener playAction, ActionListener leaderBoardAction, ActionListener settingsAction, ActionListener exitAction) {
        backgroundImage = new ImageIcon("src/main/resources/titlescreen.png").getImage();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 5, 5, 5);

        Font titleFont = new Font("Arial", Font.BOLD, 60);
        JLabel titleLabel = new JLabel("Snake");
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridy++;

        Font buttonFont = new Font("Arial", Font.BOLD, 25);

        JButton playButton = new JButton("Play");
        playButton.setFont(buttonFont);
        playButton.setPreferredSize(new Dimension(200, 50));
        playButton.addActionListener(playAction);
        add(playButton, gbc);

        gbc.gridy++;
        JButton leaderBoardButton = new JButton("Leaderboard");
        leaderBoardButton.setFont(buttonFont);
        leaderBoardButton.setPreferredSize(new Dimension(200, 50));
        leaderBoardButton.addActionListener(leaderBoardAction);
        add(leaderBoardButton, gbc);

        gbc.gridy++;
        JButton settingButton = new JButton("Settings");
        settingButton.setFont(buttonFont);
        settingButton.setPreferredSize(new Dimension(200, 50));
        settingButton.addActionListener(settingsAction);
        add(settingButton, gbc);

        gbc.gridy++;
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.addActionListener(exitAction);
        add(exitButton, gbc);
    }

    /**
     * A háttérkép kirajzolásáért felelős metódus.
     * @param g A kirajzolásért felelős Graphics objektum.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
