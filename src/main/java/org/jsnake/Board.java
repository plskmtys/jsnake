package org.jsnake;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Board extends JPanel implements ActionListener {

    static final int LEFTKEY = KeyEvent.VK_A;
    static final int RIGHTKEY = KeyEvent.VK_D;
    static final int UPKEY = KeyEvent.VK_W;
    static final int DOWNKEY = KeyEvent.VK_S;
    
    private static final int SQUARE_SIZE = 16;
    private static final int B_WIDTH = SQUARE_SIZE * 40;
    private static final int B_HEIGHT = SQUARE_SIZE * 40;
    private static final int DELAY = 100;

    private static final int SCORE_HEIGHT = 32;
    
    private Fruit fruit;
    private int fruitX;
    private int fruitY;

    private boolean inGame = true;
    private ScoreKeeper scoreKeeper;
    private Timer timer;
    private Color color;
    private Snake playerSnake;
    private Snake aiSnake;
    private Color playerSnakeColor;
    private Color aiSnakeColor;
    private AiSnakeController aiSnakeController;
    private boolean isAiPlaying = true;
    private JLabel scoreLabel;

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

        setLayout(null);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT + SCORE_HEIGHT));
        setBackground(color);
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
        setFocusable(true);
        loadImages();
        initGame();
    }

    private void loadImages() {
        playerSnake.loadImages();
        aiSnake.loadImages();
    }

    private void initGame() {
        scoreKeeper.resetScore();
        
        aiSnakeController = new AiSnakeController(aiSnake, playerSnake);
        locateFruit();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void resetGame() {
        removeAll();
        setLayout(null);
        
        inGame = true;
        isAiPlaying = true;
        
        if (timer != null) {
            timer.stop();
        }

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
        drawScoreArea(g); // Draw the score area at the top
        doDrawing(g);
        if(!inGame){
            gameOver(g);
        }
    }

    private void drawScoreArea(Graphics g) {
        g.setColor(new Color(69, 69, 69));
        g.fillRect(0, 0, B_WIDTH, SCORE_HEIGHT);

        String scoreMsg = "Score: " + scoreKeeper.getCurrentScore();
        Font small = new Font("Arial", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, 24);
    }
    
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setClip(new Rectangle(0, SCORE_HEIGHT, B_WIDTH, B_HEIGHT));
        g2d.translate(0, SCORE_HEIGHT);

        g2d.drawImage(fruit.getImage(), fruitX, fruitY, this);

        playerSnake.doDrawing(g2d);
        if (isAiPlaying) aiSnake.doDrawing(g2d);

        g2d.dispose();

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        String scoreMsg = "Score: " + scoreKeeper.getCurrentScore();
        Font small = new Font("Arial", Font.BOLD, 32);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, (B_HEIGHT + SCORE_HEIGHT) / 2);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, (B_HEIGHT + SCORE_HEIGHT) / 2 + 40);

        JButton mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 20));
        mainMenuButton.setBounds((B_WIDTH - 250) / 2, (B_HEIGHT) / 2 + SCORE_HEIGHT + 60, 250, 40);
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

    private void checkFruit() {
        if (playerSnake.getHeadPos().equals(new Point(fruitX, fruitY))) {
            playerSnake.eat(fruit);
            scoreKeeper.increaseScore(fruit.getValue());
            locateFruit();
        } else if (isAiPlaying && aiSnake.getHeadPos().equals(new Point(fruitX, fruitY))){
            aiSnake.eat(fruit);
            locateFruit();
        }
    }

    private void removeSnakeFromBoard(Snake snake) {
        snake.resetTo(0, new Point(-2, -2), null);
        //repaint();
    }

    private void checkCollision() {
        if(playerSnake.checkCollision()){
            inGame = false;
        }

        if(isAiPlaying){
            if(playerSnake.checkCollision(aiSnake)){
                inGame = false;
            }
    
            if(aiSnake.checkCollision() || aiSnake.checkCollision(playerSnake)){
                removeSnakeFromBoard(aiSnake);
                isAiPlaying = false;
            }
        }
    }

    private void locateFruit() {
        boolean validPosition = false;
        while (!validPosition) {
            fruit = FruitGenerator.generateFruit();

            int r = (int) (Math.random() * (B_WIDTH / SQUARE_SIZE));
            fruitX = r * SQUARE_SIZE;

            r = (int) (Math.random() * (B_HEIGHT / SQUARE_SIZE));
            fruitY = r * SQUARE_SIZE;

            Point fruitPosition = new Point(fruitX, fruitY);
            validPosition = true;

            // Check if the fruit position collides with the playerSnake
            for (int i = 0; i < playerSnake.getLength(); i++) {
                if (fruitPosition.equals(new Point(playerSnake.getSnakex()[i], playerSnake.getSnakey()[i]))) {
                    validPosition = false;
                    break;
                }
            }

            // Check if the fruit position collides with the aiSnake
            if (validPosition && isAiPlaying) {
                for (int i = 0; i < aiSnake.getLength(); i++) {
                    if (fruitPosition.equals(new Point(aiSnake.getSnakex()[i], aiSnake.getSnakey()[i]))) {
                        validPosition = false;
                        break;
                    }
                }
            }
        }

        if (isAiPlaying) aiSnakeController.setTarget(new Point(fruitX, fruitY));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkFruit();
            checkCollision();
            playerSnake.move();
            if(isAiPlaying){
                aiSnakeController.moveTowardsTarget();
                aiSnake.move();
            }
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