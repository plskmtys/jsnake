package org.jsnake;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    public MainMenu(ActionListener playAction, ActionListener exitAction) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton playButton = new JButton("Play");
        playButton.addActionListener(playAction);
        add(playButton, gbc);

        gbc.gridy++;
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAction);
        add(exitButton, gbc);
    }
}
