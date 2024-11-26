package org.jsnake;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A játékban elért legmagasabb pontszámokat megjelenítő panel.
 */
public class LeaderBoard extends JPanel {
    /**
     * A pontszámokat tároló objektum.
     */
    private ScoreKeeper scoreKeeper;

    /**
     * Konstruktor.
     * @param scoreKeeper A pontszámokat tároló objektum.
     */
    public LeaderBoard(ScoreKeeper scoreKeeper) {
        this.scoreKeeper = scoreKeeper;
        setLayout(new BorderLayout());
        updateLeaderboard();
    }

    /**
     * Frissíti a ranglistát.
     */
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
        table.setRowHeight(30);

        // Center align table cells and set font size
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.PLAIN, 20));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set font size for column headers
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 22));
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        add(titleLabel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.addActionListener(e -> returnToMainMenu());
        add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Visszatér a főmenübe.
     */
    private void returnToMainMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof SnakeGame) {
            SnakeGame snake = (SnakeGame) topFrame;
            snake.showMainMenu();
        }
    }
}
