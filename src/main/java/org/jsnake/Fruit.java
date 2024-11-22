package org.jsnake;

import java.awt.*;
import java.util.Random;

public class Fruit {
    private Point location;
    private int type;

    public Fruit() {
        // Randomly generate fruit location and type
        Random random = new Random();
        location = new Point(random.nextInt(40), random.nextInt(40));
        type = random.nextInt(4) + 1;
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