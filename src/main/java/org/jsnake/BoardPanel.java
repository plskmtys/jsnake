package org.jsnake;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * A játék tábláját megjelenítő panel.
 */
public class BoardPanel extends JPanel {
    /**
     * A játék táblája.
     */
    private Board gameBoard;

    /**
     * A pontszámot megjelenítő címke.
     */
    private JLabel scoreLabel;

    /**
     * A BoardPanel osztály konstruktora.
     * @param gameBoard A játék táblája.
     */
    public BoardPanel(Board gameBoard) {
        this.gameBoard = gameBoard;
        gameBoard.setBoardPanel(this);

        setLayout(new BorderLayout());
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setPreferredSize(new Dimension(640, 40));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setVerticalAlignment(SwingConstants.CENTER);
        scoreLabel.setOpaque(true);
        scoreLabel.setBorder(new javax.swing.border.MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        //scoreLabel.setBackground(Color.LIGHT_GRAY);

        add(scoreLabel, BorderLayout.NORTH);

        add(gameBoard, BorderLayout.CENTER);
    }

    /**
     * A pontszám címke frissítése.
     * @param score Az új pontszám.
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
