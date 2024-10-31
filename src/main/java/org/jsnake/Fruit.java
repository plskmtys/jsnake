package org.jsnake;

import java.awt.*;

public class Fruit {
    private Point location;
    private int type;

    public Fruit() {
        // Randomly generate fruit location and type
    }

    public void draw(Graphics g) {
        switch (type) {
            case 1:
                g.setColor(Color.RED);
                break;
            case 2:
                g.setColor(Color.YELLOW);
                break;
            case 3:
                g.setColor(Color.ORANGE);
                break;
            case 4:
                g.setColor(Color.PINK);
                break;
        }
        g.fillRect(location.x * 20, location.y * 20, 20, 20);
    }
}