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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A Board osztály felelős a játéktér megjelenítéséért és a játék logikájáért.
 */
public class Board extends JPanel implements ActionListener {

    /**
     * A kígyót irányító billentyűkonstansok.
     */
    static final int LEFTKEY = KeyEvent.VK_A;
    static final int RIGHTKEY = KeyEvent.VK_D;
    static final int UPKEY = KeyEvent.VK_W;
    static final int DOWNKEY = KeyEvent.VK_S;
    
    /**
     * A játéktér méretei és a frissítési időköz.
     */
    private static final int SQUARE_SIZE = 16;
    private static final int B_WIDTH = SQUARE_SIZE * 40;
    private static final int B_HEIGHT = SQUARE_SIZE * 40;
    private static final int DELAY = 100;

    /**
     * A pontszám megjelenítéséhez szükséges konstansok.
     */
    //private static final int SCORE_HEIGHT = 32;
    
    /**
     * A gyümölcs és pozíciója
     */
    private Fruit fruit;
    private int fruitX;
    private int fruitY;

    /**
     * A játék állapotát tároló változók.
     */
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
    private BoardPanel boardPanel;

    /**
     * Visszaadja a játéktér egy négyzetének oldalhosszát.
     * @return a négyzet oldalhossza
     */
    public int getSquareSize(){
        return SQUARE_SIZE;
    }

    /**
     * Visszaadja a játéktér magasságát.
     * @return a játéktér magassága
     */
    public int getBoardHeight(){
        return B_HEIGHT;
    }

    /**
     * Visszaadja a játéktér szélességét.
     * @return a játéktér szélessége
     */
    public int getBoardWidth(){
        return B_WIDTH;
    }

    /**
     * Konstruktor, amely beállítja a játéktér és a kígyók tulajdonságait.
     * @param scoreKeeper a pontszámot kezelő objektum
     * @param color a játéktér hátterének színe
     * @param playerSnakeColor a játékos kígyó színe
     * @param aiSnakeColor az AI kígyó színe
     */
    public Board(ScoreKeeper scoreKeeper, Color color, Color playerSnakeColor, Color aiSnakeColor) {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        this.scoreKeeper = scoreKeeper;
        this.color = color;
        
        this.playerSnakeColor = playerSnakeColor;
        this.aiSnakeColor = aiSnakeColor;
        
        this.playerSnake = new Snake(3, new Point(5, 5), playerSnakeColor, Direction.RIGHT, this);
        this.aiSnake = new Snake(3, new Point(5, 20), aiSnakeColor, Direction.RIGHT, this);
        
        setBackground(color);
        initBoard();
    }

    /**
     * Beállítja a BoardPanel-t.
     * @param boardPanel a BoardPanel
     */
    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }
    /**
     * Beállítja a játékos kígyó színét.
     * @param color a kígyó színe
     */
    public void setPlayerSnakeColor(Color color) {
        playerSnakeColor = color;
        if (playerSnake != null) {
            playerSnake.setColor(color);
            playerSnake.loadImages();
            repaint();
        }
    }

    /**
     * Beállítja az AI kígyó színét.
     * @param color a kígyó színe
     */
    public void setAiSnakeColor(Color color) {
        aiSnakeColor = color;
        if (aiSnake != null) {
            aiSnake.setColor(color);
            aiSnake.loadImages();
            repaint();
        }
    }

    /**
     * Beállítja a játéktér hátterének színét.
     * @param color a hátter színe
     */
    public void setBackgroundColor(Color color) {
        this.color = color;
        setBackground(color);
        repaint();
    }
    
    /**
     * Inicializálja a játéktér elemeit.
     */
    public void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        loadImages();
        initGame();
    }

    /**
     * Betölti a kígyók képeit.
     */
    private void loadImages() {
        playerSnake.loadImages();
        aiSnake.loadImages();
    }

    /**
     * Inicializálja a játékot.
     */
    private void initGame() {
        scoreKeeper.resetScore();
        if(boardPanel != null) boardPanel.updateScore(0);
        
        aiSnakeController = new AiSnakeController(aiSnake, playerSnake);
        locateFruit();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Új játék indítása.
     */
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

    /**
     * A játékteret kirajzoló metódus.
     * @param g a rajzolásért felelős Graphics objektum
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        if (!inGame) {
            gameOver(g);
        }
    }
    
    /**
     * Kirajzolja a játéktéren lévő elemeket.
     * @param g a rajzolásért felelős Graphics objektum
     */
    private void doDrawing(Graphics g) {
        g.drawImage(fruit.getImage(), fruitX, fruitY, this);

        playerSnake.doDrawing(g);
        if (isAiPlaying) aiSnake.doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Kirajzolja a játék végét jelző üzenetet.
     * @param g a rajzolásért felelős Graphics objektum
     */
    private void gameOver(Graphics g) {

        String msg = "Game Over";
        String scoreMsg = "Score: " + scoreKeeper.getCurrentScore();
        Font small = new Font("Arial", Font.BOLD, 32);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, (B_HEIGHT) / 2);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, (B_HEIGHT) / 2 + 40);

        JButton mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 20));
        mainMenuButton.setBounds((B_WIDTH - 250) / 2, (B_HEIGHT) / 2 + 60, 250, 40);
        mainMenuButton.addActionListener(e -> {
            scoreKeeper.saveScore();
            returnToMainMenu();
        });

        setLayout(null);
        add(mainMenuButton);
        SwingUtilities.updateComponentTreeUI(this);
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

    /**
     * Ellenőrzi, hogy a kígyó megevett-e egy gyümölcsöt.
     */
    private void checkFruit() {
        if (playerSnake.getHeadPos().equals(new Point(fruitX, fruitY))) {
            playerSnake.eat(fruit);
            scoreKeeper.increaseScore(fruit.getValue());
            boardPanel.updateScore(scoreKeeper.getCurrentScore());
            locateFruit();
        } else if (isAiPlaying && aiSnake.getHeadPos().equals(new Point(fruitX, fruitY))){
            aiSnake.eat(fruit);
            locateFruit();
        }
    }

    /**
     * Eltávolítja a kígyót a játéktérről.
     * @param snake a kígyó, amelyet eltávolítunk
     */
    private void removeSnakeFromBoard(Snake snake) {
        snake.resetTo(0, new Point(-2, -2), null);
    }

    /**
     * Ellenőrzi, hogy a kígyó ütközött-e valamivel.
     * Ha a játékos kígyó ütközik, a játék véget ér.
     * Ha az AI kígyója ütközik, eltávolítja a kígyóját a játéktérről.
     */
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

    /**
     * Elhelyezi a gyümölcsöt a játéktéren egy véletlenszerű pozícióra.
     * A gyümölcs nem kerülhet a kígyók alá.
     */
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

            for (int i = 0; i < playerSnake.getLength(); i++) {
                if (fruitPosition.equals(new Point(playerSnake.getSnakex()[i], playerSnake.getSnakey()[i]))) {
                    validPosition = false;
                    break;
                }
            }

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

    /**
     * A játék logikáját végrehajtó metódus.
     * @param e az esemény, amely a játék logikáját indítja
     */
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

    /**
     * A billentyűzet eseményeit, azaz a játékos kígyójának irányítását kezelő osztály.
     */
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