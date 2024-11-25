package org.jsnake;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    public MainMenu(ActionListener playAction, ActionListener leaderBoardAction, ActionListener settingsAction, ActionListener exitAction) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton playButton = new JButton("Play");
        playButton.addActionListener(playAction);
        add(playButton, gbc);

        gbc.gridy++;
        JButton leaderBoardButton = new JButton("Leaderboard");
        leaderBoardButton.addActionListener(leaderBoardAction);
        add(leaderBoardButton, gbc);

        gbc.gridy++;
        JButton settingButton = new JButton("Settings");
        settingButton.addActionListener(settingsAction);
        add(settingButton, gbc);

        gbc.gridy++;
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAction);
        add(exitButton, gbc);
    }
}
