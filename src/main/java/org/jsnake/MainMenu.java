package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JButton newGameButton = new JButton("Új játék");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new GamePanel(frame));
                frame.revalidate();
            }
        });

        JButton highScoreButton = new JButton("Dicsőséglista");
        highScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new HighScoreMenu(frame));
                frame.revalidate();
            }
        });

        JButton settingsButton = new JButton("Beállítások");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new SettingsMenu(frame));
                frame.revalidate();
            }
        });

        add(newGameButton, BorderLayout.NORTH);
        add(highScoreButton, BorderLayout.CENTER);
        add(settingsButton, BorderLayout.SOUTH);
    }
}