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
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Board extends JPanel implements ActionListener {
    
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    private static final int SQUARE_SIZE = 16;
    private static final int B_WIDTH = SQUARE_SIZE * 40;
    private static final int B_HEIGHT = SQUARE_SIZE * 40;
    private static final int MAXLEN = 900;
    private static final int DELAY = 100;

    private final int[] snakex = new int[MAXLEN];
    private final int[] snakey = new int[MAXLEN];

    private int length;
    private int appleX;
    private int appleY;
    private boolean inGame = true;
    private Direction direction = Direction.RIGHT;

    private Timer timer;
    private Image apple;
    private Image head;
    private Image tail;
    private Image bodyStraight;
    private Image bodyBent;
    private ScoreKeeper scoreKeeper;

    public Board(ScoreKeeper scoreKeeper) {  // Modified constructor
        this.scoreKeeper = scoreKeeper;
        initBoard();
    }
    
    public void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        try {
            apple = ImageIO.read(new File("src/main/resources/apple.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
            head = ImageIO.read(new File("src/main/resources/snake_head.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
            tail = ImageIO.read(new File("src/main/resources/snake_tail.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
            bodyStraight = ImageIO.read(new File("src/main/resources/snake_body_straight.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
            bodyBent = ImageIO.read(new File("src/main/resources/snake_body_bent.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGame() {
        length = 3;
        scoreKeeper.resetScore();

        // Reset snake position
        for (int z = 0; z < length; z++) {
            snakex[z] = 0;
            snakey[z] = B_HEIGHT / 2;
        }
        
        locateApple();

        // Create and start new timer
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void resetGame() {
        // Remove any components added during game over
        removeAll();
        setLayout(null); // Reset layout
        
        // Reset game state
        inGame = true;
        direction = Direction.RIGHT;
        
        // Stop existing timer if running
        if (timer != null) {
            timer.stop();
        }
        
        // Initialize new game
        initGame();
        
        // Ensure the panel is updated
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        doDrawing(g);
        if(!inGame){
            gameOver(g);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.GRAY); // Set the color for the grid lines

        for (int i = 0; i <= B_WIDTH; i += SQUARE_SIZE) {
            g.drawLine(i, 0, i, B_HEIGHT); // Draw vertical lines
        }

        for (int j = 0; j <= B_HEIGHT; j += SQUARE_SIZE) {
            g.drawLine(0, j, B_WIDTH, j); // Draw horizontal lines
        }
    }
    
    private void doDrawing(Graphics g) {
        g.drawImage(apple, appleX, appleY, this);

        for (int z = 0; z < length; z++) {
            if (z == 0) {
                drawHead(g, snakex[z], snakey[z]);
            } else if (z == length - 1) {
                drawTail(g, snakex[z], snakey[z]);
            } else {
                drawBodySegment(g, z);
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawHead(Graphics g, int x, int y) {
        drawRotatedImage(g, head, x, y, directionToAngle(direction));
    }

    private void drawTail(Graphics g, int x, int y) {
        Direction tailDirection = getTailDirection();
        drawRotatedImage(g, tail, x, y, directionToAngle(tailDirection));
    }

    private void drawBodySegment(Graphics g, int z) {
        Direction prevDirection = getDirection(snakex[z], snakey[z], snakex[z - 1], snakey[z - 1]);
        Direction nextDirection = getDirection(snakex[z], snakey[z], snakex[z + 1], snakey[z + 1]);

        if (prevDirection == nextDirection || areOpposite(prevDirection, nextDirection)) {
            drawRotatedImage(g, bodyStraight, snakex[z], snakey[z], directionToAngle(prevDirection));
        } else {
            drawRotatedImage(g, bodyBent, snakex[z], snakey[z], getBendAngle(prevDirection, nextDirection));
        }
    }

    private void drawRotatedImage(Graphics g, Image image, int x, int y, double angle) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(angle), x + SQUARE_SIZE / 2, y + SQUARE_SIZE / 2);
        g2d.drawImage(image, x, y, this);
        g2d.rotate(Math.toRadians(-angle), x + SQUARE_SIZE / 2, y + SQUARE_SIZE / 2);
    }

    private double directionToAngle(Direction direction) {
        switch (direction) {
            case UP: return 0;
            case RIGHT: return 90;
            case DOWN: return 180;
            case LEFT: return 270;
        }
        return 0;
    }

    private Direction getDirection(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            return y1 > y2 ? Direction.UP : Direction.DOWN;
        } else {
            return x1 > x2 ? Direction.LEFT : Direction.RIGHT;
        }
    }

    private Direction getTailDirection() {
        if (length < 2) return direction;
        return getDirection(snakex[length - 1], snakey[length - 1], snakex[length - 2], snakey[length - 2]);
    }

    private double getBendAngle(Direction prevDirection, Direction nextDirection) {
        if ((prevDirection == Direction.UP && nextDirection == Direction.RIGHT) || (prevDirection == Direction.RIGHT && nextDirection == Direction.UP)) {
            return 0;
        } else if ((prevDirection == Direction.RIGHT && nextDirection == Direction.DOWN) || (prevDirection == Direction.DOWN && nextDirection == Direction.RIGHT)) {
            return 90;
        } else if ((prevDirection == Direction.DOWN && nextDirection == Direction.LEFT) || (prevDirection == Direction.LEFT && nextDirection == Direction.DOWN)) {
            return 180;
        } else if ((prevDirection == Direction.LEFT && nextDirection == Direction.UP) || (prevDirection == Direction.UP && nextDirection == Direction.LEFT)) {
            return 270;
        }
        return 0;
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Score: " + scoreKeeper.getCurrentScore();
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, B_HEIGHT / 2 + 30);

        JButton mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.setBounds((B_WIDTH - 200) / 2, B_HEIGHT / 2 + 60, 200, 30);
        mainMenuButton.addActionListener(e -> {
            scoreKeeper.saveScore();
            returnToMainMenu();
        });

        setLayout(null);
        add(mainMenuButton);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void returnToMainMenu() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof Snake) {
            Snake snake = (Snake) topFrame;
            snake.showMainMenu();
        }
    }

    private void checkApple() {
        if ((snakex[0] == appleX) && (snakey[0] == appleY)) {
            length++;
            scoreKeeper.increaseScore();
            locateApple();
        }
    }

    private void move() {

        for (int z = length; z > 0; z--) {
            snakex[z] = snakex[(z - 1)];
            snakey[z] = snakey[(z - 1)];
        }


        if (direction.equals(Board.Direction.LEFT)) {
            snakex[0] -= SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.RIGHT)) {
            snakex[0] += SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.UP)) {
            snakey[0] -= SQUARE_SIZE;
        }

        if (direction.equals(Board.Direction.DOWN)) {
            snakey[0] += SQUARE_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = length; z > 0; z--) {
            if ((z > 4) && (snakex[0] == snakex[z]) && (snakey[0] == snakey[z])) {
                inGame = false;
            }
        }

        if (snakey[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (snakey[0] < 0) {
            inGame = false;
        }

        if (snakex[0] >= B_WIDTH) {
            inGame = false;
        }

        if (snakex[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * (B_WIDTH / SQUARE_SIZE));
        appleX = r * SQUARE_SIZE;

        r = (int) (Math.random() * (B_HEIGHT / SQUARE_SIZE));
        appleY = r * SQUARE_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        } else {
            timer.stop();
        }

        repaint();
    }

    private boolean areOpposite(Direction dir1, Direction dir2) {
        return (dir1 == Direction.UP && dir2 == Direction.DOWN) ||
               (dir1 == Direction.DOWN && dir2 == Direction.UP) ||
               (dir1 == Direction.LEFT && dir2 == Direction.RIGHT) ||
               (dir1 == Direction.RIGHT && dir2 == Direction.LEFT);
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