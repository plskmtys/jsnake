package org.jsnake;

import java.awt.Point;
import java.util.*;

public class AiSnakeController {
    private Snake snake;
    private Snake playerSnake;
    private Point target;

    public AiSnakeController(Snake snake, Snake playerSnake) {
        this.snake = snake;
        this.playerSnake = playerSnake;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public void moveTowardsTarget() {
        Point head = snake.getHeadPos();
        if (head.equals(target)) {
            return; // Already at the target
        }

        // Define possible directions
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        int[][] deltas = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        // Priority queue for Dijkstra's algorithm
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        pq.add(new Node(head, 0, null));

        // Set to track visited nodes
        Set<Point> visited = new HashSet<>();
        visited.add(head);

        // Map to reconstruct the path
        Map<Point, Node> cameFrom = new HashMap<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            Point currentPos = current.position;

            // If we reached the target, reconstruct the path
            if (currentPos.equals(target)) {
                Node node = current;
                while (node.previous != null && node.previous.previous != null) {
                    node = node.previous;
                }
                setDirection(head, node.position);
                return;
            }

            // Explore neighbors
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

    private boolean isValidMove(Point pos) {
        // Check if the position is within the board boundaries
        if (pos.x < 0 || pos.x >= snake.getBoard().getBoardWidth() ||
            pos.y < 0 || pos.y >= snake.getBoard().getBoardHeight()) {
            return false;
        }

        // Check for collision with the snake itself
        for (int i = 0; i < snake.getLength(); i++) {
            if (pos.equals(new Point(snake.getSnakex()[i], snake.getSnakey()[i]))) {
                return false;
            }
        }

        // Check for collision with the player snake
        for (int i = 0; i < playerSnake.getLength(); i++) {
            if (pos.equals(new Point(playerSnake.getSnakex()[i], playerSnake.getSnakey()[i]))) {
                return false;
            }
        }

        return true;
    }

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

    private static class Node {
        Point position;
        int distance;
        Node previous;

        Node(Point position, int distance, Node previous) {
            this.position = position;
            this.distance = distance;
            this.previous = previous;
        }
    }
}
