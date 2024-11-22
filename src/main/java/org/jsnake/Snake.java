package org.jsnake;

import java.awt.*;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private Color color;
    private Direction direction;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Snake(Color color) {
        this.color = color;
        body = new LinkedList<>();
        body.add(new Point(10, 10));
        direction = Direction.RIGHT; // Initial direction
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.y -= 1;
                break;
            case DOWN:
                newHead.y += 1;
                break;
            case LEFT:
                newHead.x -= 1;
                break;
            case RIGHT:
                newHead.x += 1;
                break;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void draw(Graphics g) {
        g.setColor(color);
        for (Point point : body) {
            g.fillRect(point.x * 20, point.y * 20, 20, 20);
        }
    }
}