package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoreMenu extends JPanel {
    private JFrame frame;
    private ScoreManager scoreManager;

    public HighScoreMenu(JFrame frame) {
        this.frame = frame;
        this.scoreManager = new ScoreManager();
        setLayout(new BorderLayout());

        List<Integer> scores = scoreManager.getScores();
        String[] columnNames = {"Pontszám"};
        Object[][] data = new Object[scores.size()][1];
        for (int i = 0; i < scores.size(); i++) {
            data[i][0] = scores.get(i);
        }

        JTable table = new JTable(data, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}