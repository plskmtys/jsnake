package org.jsnake;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/** Snake osztály, amely a kígyót reprezentálja a játékban.
 */
public class Snake {
    
    /**
     * A kígyó maximális hossza.
     */
    private static final int MAXLEN = 900;

    /**
     * A kígyó szegmenseinek ("testrészeinek") x és y koordinátáit tároló tömbök.
     */
    private final int[] snakex = new int[MAXLEN];
    private final int[] snakey = new int[MAXLEN];

    /**
     * A kígyó hossza
     */
    private int length;

    /**
     * A kígyó mozgásirányát tároló enum.
     */
    private Direction direction = Direction.RIGHT;

    /**
     * A kígyó fejének képét tároló változó.
     */
    private Image head;

    /**
     * A kígyó farkának képét tároló változó.
     */
    private Image tail;

    /**
     * A kígyó testének egyenes szegmensének képét tároló változó.
     */
    private Image bodyStraight;

    /**
     * A kígyó testének hajlított szegmensének képét tároló változó.
     */
    private Image bodyBent;

    /**
     * A kígyó színét tároló változó.
     */
    private Color color;

    /**
     * A kígyó játéktábláját tároló változó.
     */
    private Board board;


    /**
     * Konstruktor, amely létrehoz egy kígyót a megadott paraméterekkel.
     * @param length a kígyó hossza
     * @param position a kígyó kezdőpozíciója
     * @param color a kígyó színe
     * @param direction a kígyó mozgásiránya
     * @param board a kígyó játéktáblája
     */
    Snake(int length, Point position, Color color, Direction direction, Board board) {
        this.length = length;
        this.color = color;
        this.direction = direction;
        this.board = board;
        for (int i = 0; i < length; i++) {
            snakex[i] = position.x * board.getSquareSize() - i;
            snakey[i] = position.y * board.getSquareSize();
        }
    }


    /**
     * A kígyó alaphelyzetbe állítása.
     * @param length a kígyó hossza
     * @param position a kígyó kezdőpozíciója
     * @param color a kígyó színe
     */
    void resetTo(int length, Point position, Color color) {
        this.length = length;
        this.direction = Direction.RIGHT;
        this.color = color;
        for (int i = 0; i < length; i++) {
            snakex[i] = position.x * board.getSquareSize() - i;
            snakey[i] = position.y * board.getSquareSize();
        }
    }

    /**
     * A kígyó hosszának növelése.
     * @param fruit a gyümölcs, amelyet a kígyó megesz
     */
    void eat(Fruit fruit) {
        length++;
    }

    /**
     * A kígyó fejének pozícióját adja vissza.
     * @return a kígyó fejének pozíciója
     */
    Point getHeadPos(){
        return new Point(snakex[0], snakey[0]);
    }

    /**
     * A kígyó hosszát adja vissza.
     * @return a kígyó hossza
     */
    int getLength(){
        return length;
    }

    /**
     * A kígyó szegmenseinek x kooordinátáit adja vissza.
     * @return
     */
    int[] getSnakex(){
        return snakex;
    }

    /**
     * A kígyó szegmenseinek y koordinátáit adja vissza.
     * @return
     */
    int[] getSnakey(){
        return snakey;
    }

    /**
     * A kígyó játéktábláját adja vissza.
     * @return a kígyó játéktáblája
     */
    Board getBoard(){
        return board;
    }

    /**
     * A kígyó színét adja vissza.
     * @return a kígyó színe
     */
    Color getColor(){
        return color;
    }

    /**
     * A kígyó képeinek betöltése.
     */
    void loadImages(){
        try {
            BufferedImage headBase = ImageIO.read(new File("src/main/resources/snake_head_base.png"));
            BufferedImage headOverlay = ImageIO.read(new File("src/main/resources/snake_head_overlay.png"));
            head = TextureCreator.combine(headBase, headOverlay, color);
            
            BufferedImage tailBase = ImageIO.read(new File("src/main/resources/snake_tail.png"));
            tail = TextureCreator.applyColorWithMultiply(tailBase, color);

            BufferedImage bodyStraightBase = ImageIO.read(new File("src/main/resources/snake_body_base.png"));
            bodyStraight = TextureCreator.applyColorWithMultiply(bodyStraightBase, color);

            BufferedImage bodyBentBase = ImageIO.read(new File("src/main/resources/snake_body_bent_base.png"));
            bodyBent = TextureCreator.applyColorWithMultiply(bodyBentBase, color);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A kígyó kirajzolása a játéktáblára.
     * @param g a rajzoló objektum
     */
    void doDrawing(Graphics g) {
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

    /**
     * A kígyó fejének kirajzolása.
     * @param g a rajzoló objektum
     * @param x a fej x koordinátája
     * @param y a fej y koordinátája
     */
    private void drawHead(Graphics g, int x, int y) {
        drawRotatedImage(g, head, x, y, directionToAngle(direction));
    }

    /**
     * A kígyó farkának kirajzolása.
     * @param g a rajzoló objektum
     * @param x a fark x koordinátája
     * @param y a fark y koordinátája
     */
    private void drawTail(Graphics g, int x, int y) {
        Direction tailDirection = getTailDirection();
        drawRotatedImage(g, tail, x, y, directionToAngle(tailDirection));
    }

    /**
     * A kígyó testének egy szegmensének kirajzolása.
     * @param g a rajzoló objektum
     * @param z a szegmens indexe
     */
    private void drawBodySegment(Graphics g, int z) {
        Direction prevDirection = toDirection(snakex[z], snakey[z], snakex[z - 1], snakey[z - 1]);
        Direction nextDirection = toDirection(snakex[z], snakey[z], snakex[z + 1], snakey[z + 1]);

        if (prevDirection == nextDirection || areOpposite(prevDirection, nextDirection)) {
            drawRotatedImage(g, bodyStraight, snakex[z], snakey[z], directionToAngle(prevDirection));
        } else {
            drawRotatedImage(g, bodyBent, snakex[z], snakey[z], getBendAngle(prevDirection, nextDirection));
        }
    }

    /**
     * Egy kép kirajzolása elforgatva.
     * @param g a rajzoló objektum
     * @param image a kirajzolandó kép
     * @param x a kép x koordinátája
     * @param y a kép y koordinátája
     * @param angle a forgatási szög
     */
    private void drawRotatedImage(Graphics g, Image image, int x, int y, double angle) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(angle), x + board.getSquareSize() / 2, y + board.getSquareSize() / 2);
        g2d.drawImage(image, x, y, board);
        g2d.rotate(Math.toRadians(-angle), x + board.getSquareSize() / 2, y + board.getSquareSize() / 2);
    }

    /**
     * Egy irányt átalakít egy szögbe.
     * @param direction az irány
     * @return az irányhoz tartozó szög
     */
    private double directionToAngle(Direction direction) {
        switch (direction) {
            case UP: return 0;
            case RIGHT: return 90;
            case DOWN: return 180;
            case LEFT: return 270;
        }
        return 0;
    }

    /**
     * Két pont közötti irányt adja vissza.
     * @param x1 pont1 x koordinátája
     * @param y1 pont1 y koordinátája
     * @param x2 pont2 x koordinátája
     * @param y2 pont2 y koordinátája
     * @return az irány
     */
    private Direction toDirection(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            return y1 > y2 ? Direction.UP : Direction.DOWN;
        } else {
            return x1 > x2 ? Direction.LEFT : Direction.RIGHT;
        }
    }


    /**
     * A kígyó farkának irányát adja vissza.
     * @return a kígyó farkának iránya
     */
    private Direction getTailDirection() {
        if (length < 2) return direction;
        return toDirection(snakex[length - 1], snakey[length - 1], snakex[length - 2], snakey[length - 2]);
    }


    /**
     * Két irány közötti szöget adja vissza.
     * @param prevDirection az előző irány
     * @param nextDirection a következő irány
     * @return a szög
     */
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


    /**
     * Két irány ellentétes-e.
     * @param dir1 az egyik irány
     * @param dir2 a másik irány
     * @return true, ha az irányok ellentétesek, egyébként false
     */
    private boolean areOpposite(Direction dir1, Direction dir2) {
        return (dir1 == Direction.UP && dir2 == Direction.DOWN) ||
               (dir1 == Direction.DOWN && dir2 == Direction.UP) ||
               (dir1 == Direction.LEFT && dir2 == Direction.RIGHT) ||
               (dir1 == Direction.RIGHT && dir2 == Direction.LEFT);
    }

    /**
     * A kígyó mozgatása.
     */
    void move() {

        for (int z = length; z > 0; z--) {
            snakex[z] = snakex[(z - 1)];
            snakey[z] = snakey[(z - 1)];
        }


        if (direction.equals(Direction.LEFT)) {
            snakex[0] -= board.getSquareSize();
        }

        if (direction.equals(Direction.RIGHT)) {
            snakex[0] += board.getSquareSize();
        }

        if (direction.equals(Direction.UP)) {
            snakey[0] -= board.getSquareSize();
        }

        if (direction.equals(Direction.DOWN)) {
            snakey[0] += board.getSquareSize();
        }
    }

    /**
     * A kígyó mozgásirányának beállítása.
     * @param direction az új irány
     */
    public void setDirection(Direction direction){
        if(!areOpposite(this.direction, direction)){
            this.direction = direction;
        }
    }

    /**
     * A kígyó mozgásirányának lekérdezése.
     * @return a kígyó mozgásiránya
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * A kígyó ütközésének ellenőrzése saját magával, vagy a tábla szélével.
     * @return true, ha a kígyó ütközött, egyébként false
     */
    public boolean checkCollision(){
        for (int z = length; z > 0; z--) {
            if ((z > 4) && (snakex[0] == snakex[z]) && (snakey[0] == snakey[z])) {
                return true;
            }
        }

        if (getHeadPos().getY() >= board.getBoardHeight()) {
            return true;
        }

        if (getHeadPos().getY() < 0) {
            return true;
        }

        if (getHeadPos().getX() >= board.getBoardWidth()) {
            return true;
        }

        if (getHeadPos().getX() < 0) {
            return true;
        }

        return false;
    }

    /**
     * A kígyó ütközésének ellenőrzése egy másik kígyóval.
     * @param other a másik kígyó
     * @return true, ha a kígyó ütközött, egyébként false
     */
    public boolean checkCollision(Snake other){
        for (int z = 0; z < other.length; z++) {
            if ((snakex[0] == other.snakex[z]) && (snakey[0] == other.snakey[z])) {
                return true;
            }
        }
        return false;
    }

    /**
     * A kígyó fejének pozícióját beállító metódus.
     * @param x az új x koordináta
     * @param y az új y koordináta
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
