package org.jsnake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {
    
    private Snake snake;
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(new ScoreKeeper(), Color.BLACK, Color.GREEN, Color.RED);
        snake = new Snake(3, new Point(5, 5), Color.GREEN, Direction.RIGHT, board);
    }

    @Test
    public void testInitialLength() {
        assertEquals(3, snake.getLength());
    }

    @Test
    public void testInitialPosition() {
        assertEquals(new Point(5 * board.getSquareSize(), 5 * board.getSquareSize()), snake.getHeadPos());
    }

    @Test
    public void testMoveRight() {
        snake.move();
        assertEquals(new Point(6 * board.getSquareSize(), 5 * board.getSquareSize()), snake.getHeadPos());
    }

    @Test
    public void testChangeDirection() {
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals(new Point(5 * board.getSquareSize(), 6 * board.getSquareSize()), snake.getHeadPos());
    }

    @Test
    public void testCollisionWithWall() {
        snake.setDirection(Direction.UP);
        for (int i = 0; i < 6; i++) {
            snake.move();
        }
        assertTrue(snake.checkCollision());
    }

    @Test
    public void testResetTo() {
        snake.resetTo(5, new Point(10, 10), Color.BLUE);
        assertEquals(5, snake.getLength());
        assertEquals(new Point(10 * board.getSquareSize(), 10 * board.getSquareSize()), snake.getHeadPos());
        assertEquals(Color.BLUE, snake.getColor());
    }

    @Test
    public void testEat() {
        snake.eat(Fruit.APPLE);
        assertEquals(4, snake.getLength());
    }
}
