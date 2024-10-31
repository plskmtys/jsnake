package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Snake playerSnake;
    private Snake aiSnake;
    private Fruit fruit;
    private ScoreManager scoreManager;

    public GamePanel(JFrame frame) {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.BLACK);

        playerSnake = new Snake(Color.GREEN);
        aiSnake = new Snake(Color.RED);
        fruit = new Fruit();
        scoreManager = new ScoreManager();

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        playerSnake.draw(g);
        aiSnake.draw(g);
        fruit.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playerSnake.move();
        aiSnake.move();
        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        // Implement collision detection logic
    }
}