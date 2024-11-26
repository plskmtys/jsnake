package org.jsnake;

import java.awt.Point;
import java.util.*;

/**
 * Az AI kígyó vezérlője. A kígyó egy cél felé mozgatása a Dijkstra algoritmus segítségével.
 * A kígyó a játékos kígyóját és a falakat elkerüli.
 */
public class AiSnakeController {
    /**
     * A kígyó, amelyet vezérel az AI.
     */
    private Snake snake;

    /**
     * A játékos kígyója.
     */
    private Snake playerSnake;

    /**
     * A cél, amely felé a kígyót mozgatni kell.
     */
    private Point target;

    /**
     * Konstruktor.
     * @param snake a kígyó, amelyet vezérel az AI
     * @param playerSnake a játékos kígyója
     */
    public AiSnakeController(Snake snake, Snake playerSnake) {
        this.snake = snake;
        this.playerSnake = playerSnake;
    }

    /**
     * Beállítja a célt, amely felé a kígyót mozgatni kell.
     * @param target a cél
     */
    public void setTarget(Point target) {
        this.target = target;
    }

    /**
     * A kígyót mozgatja a cél felé.
     */
    public void moveTowardsTarget() {
        Point head = snake.getHeadPos();
        if (head.equals(target)) {
            return;
        }

        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        int[][] deltas = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        pq.add(new Node(head, 0, null));

        Set<Point> visited = new HashSet<>();
        visited.add(head);

        Map<Point, Node> cameFrom = new HashMap<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            Point currentPos = current.position;

            if (currentPos.equals(target)) {
                Node node = current;
                while (node.previous != null && node.previous.previous != null) {
                    node = node.previous;
                }
                setDirection(head, node.position);
                return;
            }

            for (int i = 0; i < directions.length; i++) {
                Point neighborPos = new Point(currentPos.x + deltas[i][0] * snake.getBoard().getSquareSize(),
                                              currentPos.y + deltas[i][1] * snake.getBoard().getSquareSize());

                if (!visited.contains(neighborPos) && isValidMove(neighborPos)) {
                    visited.add(neighborPos);
                    Node neighbor = new Node(neighborPos, current.distance + 1, current);
                    pq.add(neighbor);
                    cameFrom.put(neighborPos, neighbor);
                }
            }
        }
    }

    /**
     * Megvizsgálja, hogy a kígyó adott pozícióra tud-e lépni.
     * @param pos a pozíció
     * @return true, ha a kígyó léphet a pozícióra, egyébként false
     */
    private boolean isValidMove(Point pos) {
        if (pos.x < 0 || pos.x >= snake.getBoard().getBoardWidth() ||
            pos.y < 0 || pos.y >= snake.getBoard().getBoardHeight()) {
            return false;
        }

        for (int i = 0; i < snake.getLength(); i++) {
            if (pos.equals(new Point(snake.getSnakex()[i], snake.getSnakey()[i]))) {
                return false;
            }
        }

        for (int i = 0; i < playerSnake.getLength(); i++) {
            if (pos.equals(new Point(playerSnake.getSnakex()[i], playerSnake.getSnakey()[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Beállítja a kígyó mozgási irányát a fej és a következő pozíció alapján.
     * @param head a kígyó feje
     * @param next a következő pozíció
     */
    private void setDirection(Point head, Point next) {
        if (next.x < head.x) {
            snake.setDirection(Direction.LEFT);
        } else if (next.x > head.x) {
            snake.setDirection(Direction.RIGHT);
        } else if (next.y < head.y) {
            snake.setDirection(Direction.UP);
        } else if (next.y > head.y) {
            snake.setDirection(Direction.DOWN);
        }
    }

    /**
     * A Dijkstra algoritmusban használt Node objektum.
     */
    private static class Node {
        /**
         * A Node pozíciója.
         */
        Point position;
        
        /**
         * A Node távolsága a kezdőponttól.
         */
        int distance;

        /**
         * A Node előző Node-ja.
         */
        Node previous;

        /**
         * Konstruktor.
         * @param position a Node pozíciója
         * @param distance a Node távolsága a kezdőponttól
         * @param previous a Node előző Node-ja
         */
        Node(Point position, int distance, Node previous) {
            this.position = position;
            this.distance = distance;
            this.previous = previous;
        }
    }
}
