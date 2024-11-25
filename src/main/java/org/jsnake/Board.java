package org.jsnake;

import java.awt.Color;
import java.awt.Point;
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
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

public class Board extends JPanel implements ActionListener {

    static final int LEFTKEY = KeyEvent.VK_A;
    static final int RIGHTKEY = KeyEvent.VK_D;
    static final int UPKEY = KeyEvent.VK_W;
    static final int DOWNKEY = KeyEvent.VK_S;
    
    private static final int SQUARE_SIZE = 16;
    private static final int B_WIDTH = SQUARE_SIZE * 40;
    private static final int B_HEIGHT = SQUARE_SIZE * 40;
    private static final int DELAY = 100;
    
    private Image apple;
    private int appleX;
    private int appleY;

    private boolean inGame = true;
    private ScoreKeeper scoreKeeper;
    private Timer timer;
    private Color color;
    private Snake playerSnake;
    private Snake aiSnake;
    private Color playerSnakeColor;
    private Color aiSnakeColor;

    public int getSquareSize(){
        return SQUARE_SIZE;
    }

    public int getBoardHeight(){
        return B_HEIGHT;
    }

    public int getBoardWidth(){
        return B_WIDTH;
    }

    public Board(ScoreKeeper scoreKeeper, Color color, Color playerSnakeColor, Color aiSnakeColor) {
        this.scoreKeeper = scoreKeeper;
        this.color = color;

        this.playerSnakeColor = playerSnakeColor;
        this.aiSnakeColor = aiSnakeColor;

        this.playerSnake = new Snake(3, new Point(5, 5), playerSnakeColor, Direction.RIGHT, this);
        this.aiSnake = new Snake(3, new Point(5, 20), aiSnakeColor, Direction.RIGHT, this);
        initBoard();
    }

    public void setPlayerSnakeColor(Color color) {
        playerSnakeColor = color;
        if (playerSnake != null) {
            playerSnake.setColor(color);
            playerSnake.loadImages(); // Reload textures with new color
            repaint();
        }
    }

    public void setAiSnakeColor(Color color) {
        aiSnakeColor = color;
        if (aiSnake != null) {
            aiSnake.setColor(color);
            aiSnake.loadImages(); // Reload textures with new color
            repaint();
        }
    }

    public void setBackgroundColor(Color color) {
        this.color = color;
        setBackground(color);
        repaint();
    }
    
    public void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(color);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        try {
            apple = ImageIO.read(new File("src/main/resources/apple.png")).getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerSnake.loadImages();
        aiSnake.loadImages();
    }

    private void initGame() {
        scoreKeeper.resetScore();
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void resetGame() {
        removeAll();
        setLayout(null);
        
        inGame = true;
        
        if (timer != null) {
            timer.stop();
        }
        
        // Use stored colors when resetting
        setBackground(color);
        playerSnake.resetTo(3, new Point(5,5), playerSnakeColor);
        aiSnake.resetTo(3, new Point(5,20), aiSnakeColor);

        initGame();
        
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //drawGrid(g);
        doDrawing(g);
        if(!inGame){
            gameOver(g);
        }
    }

    private void drawGrid(Graphics g) {
        Color darkSquare = new Color(40, 40, 40);
        Color lightSquare = new Color(60, 60, 60);
        
        for (int i = 0; i < B_WIDTH; i += SQUARE_SIZE) {
            for (int j = 0; j < B_HEIGHT; j += SQUARE_SIZE) {
                // If sum of row and column is even, use light color
                if ((i / SQUARE_SIZE + j / SQUARE_SIZE) % 2 == 0) {
                    g.setColor(lightSquare);
                } else {
                    g.setColor(darkSquare);
                }
                g.fillRect(i, j, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    
    private void doDrawing(Graphics g) {
        g.drawImage(apple, appleX, appleY, this);

        playerSnake.doDrawing(g);
        aiSnake.doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
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
        if (topFrame instanceof SnakeGame) {
            SnakeGame snake = (SnakeGame) topFrame;
            snake.showMainMenu();
        }
    }

    private void checkApple() {
        if (playerSnake.getHeadPos().equals(new Point(appleX, appleY))) {
            playerSnake.eat(1);
            scoreKeeper.increaseScore();
            locateApple();
        } else if (aiSnake.getHeadPos().equals(new Point(appleX, appleY))){
            aiSnake.eat(1);
            locateApple();
        }
    }

    private void checkCollision() {
        if(playerSnake.checkCollision()){
            inGame = false;
        }

        if (playerSnake.getHeadPos().getY() >= B_HEIGHT) {
            inGame = false;
        }

        if (playerSnake.getHeadPos().getY() < 0) {
            inGame = false;
        }

        if (playerSnake.getHeadPos().getX() >= B_WIDTH) {
            inGame = false;
        }

        if (playerSnake.getHeadPos().getX() < 0) {
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
            playerSnake.move();
            aiSnake.move();
        } else {
            timer.stop();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == LEFTKEY) {
                playerSnake.setDirection(Direction.LEFT);
            }

            if (key == RIGHTKEY) {
                playerSnake.setDirection(Direction.RIGHT);
            }

            if (key == UPKEY) {
                playerSnake.setDirection(Direction.UP);
            }

            if (key == DOWNKEY) {
                playerSnake.setDirection(Direction.DOWN);
            }
        }
    }
}