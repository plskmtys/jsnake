package org.jsnake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.*;

public class Board extends JPanel implements ActionListener {
    
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    private static final int B_WIDTH = 800;
    private static final int B_HEIGHT = 800;
    private static final int SQUARE_SIZE = 20;
    private static final int MAXLEN = 900;
    private static final int RAND_POS = 29;
    private static final int DELAY = 60;

    private final int x[] = new int[MAXLEN];
    private final int y[] = new int[MAXLEN];

    private int length;
    private int appleX;
    private int appleY;
    private boolean inGame = true;
    private Direction direction = Direction.RIGHT;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        BufferedImage iid = null;
        try{
            iid = ImageIO.read(new File("src/main/resources/dot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ball = iid.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);

        BufferedImage iia = null;
        try{
            iia = ImageIO.read(new File("src/main/resources/apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        apple = iia.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);

        BufferedImage iih = null;
        try{
            iih = ImageIO.read(new File("src/main/resources/head.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        head = iih.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
    }

    private void initGame() {

        length = 3;

        for (int z = 0; z < length; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, appleX, appleY, this);

            for (int z = 0; z < length; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {

            length++;
            locateApple();
        }
    }

    private void move() {

        for (int z = length; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (direction.equals(Board.Direction.LEFT)) {
            x[0] -= SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.RIGHT)) {
            x[0] += SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.UP)) {
            y[0] -= SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.DOWN)) {
            y[0] += SQUARE_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = length; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * (B_WIDTH / SQUARE_SIZE));
        appleX = r * SQUARE_SIZE - SQUARE_SIZE/2;

        r = (int) (Math.random() * (B_HEIGHT / SQUARE_SIZE));
        appleY = r * SQUARE_SIZE - SQUARE_SIZE/2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!direction.equals(Direction.RIGHT))) {
                direction = Direction.LEFT;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!direction.equals(Direction.LEFT))) {
                direction = Direction.RIGHT;
            }

            if ((key == KeyEvent.VK_UP) && (!direction.equals(Direction.DOWN))) {
                direction = Direction.UP;
            }

            if ((key == KeyEvent.VK_DOWN) && (!direction.equals(Direction.UP))) {
                direction = Direction.DOWN;
            }
        }
    }
}