package org.jsnake;

import java.awt.*;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private Color color;

    public Snake(Color color) {
        this.color = color;
        body = new LinkedList<>();
        body.add(new Point(10, 10));
    }

    public void move() {
        // Implement snake movement logic
    }

    public void draw(Graphics g) {
        g.setColor(color);
        for (Point point : body) {
            g.fillRect(point.x * 20, point.y * 20, 20, 20);
        }
    }
}