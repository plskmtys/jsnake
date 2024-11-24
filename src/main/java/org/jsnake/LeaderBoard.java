package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoard extends JPanel {
    private ScoreKeeper scoreKeeper;

    public LeaderBoard(ScoreKeeper scoreKeeper) {
        this.scoreKeeper = scoreKeeper;
        setLayout(new BorderLayout());
        updateLeaderboard();
    }

    public void updateLeaderboard() {
        removeAll();

        List<Integer> scores = new ArrayList<>(scoreKeeper.getScores());
        scores.sort(Collections.reverseOrder());

        String[] columnNames = {"Rank", "Score"};
        Object[][] data = new Object[scores.size()][2];

        for (int i = 0; i < scores.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = scores.get(i);
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> returnToMainMenu());
        add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void returnToMainMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof Snake) {
            Snake snake = (Snake) topFrame;
            snake.showMainMenu();
        }
    }
}
