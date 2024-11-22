package org.jsnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Snake playerSnake;
    private Snake aiSnake;
    private Fruit fruit;
    //private ScoreManager scoreManager;

    public GamePanel(JFrame frame) {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.BLACK);

        playerSnake = new Snake(Color.GREEN);
        aiSnake = new Snake(Color.RED);
        fruit = new Fruit();
        //scoreManager = new ScoreManager();

        timer = new Timer(100, this);
        timer.start();

        addKeyListener(this);
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

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (playerSnake.getDirection() != Snake.Direction.DOWN) {
                    playerSnake.setDirection(Snake.Direction.UP);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (playerSnake.getDirection() != Snake.Direction.UP) {
                    playerSnake.setDirection(Snake.Direction.DOWN);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (playerSnake.getDirection() != Snake.Direction.RIGHT) {
                    playerSnake.setDirection(Snake.Direction.LEFT);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (playerSnake.getDirection() != Snake.Direction.LEFT) {
                    playerSnake.setDirection(Snake.Direction.RIGHT);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}