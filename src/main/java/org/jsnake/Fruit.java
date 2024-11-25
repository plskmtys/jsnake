package org.jsnake;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Fruit {
    APPLE(1, 1, "apple"),
    PEAR(2, 0.4, "pear"),
    BANANA(3, 0.2, "banana"),
    STRAWBERRY(5, 0.1, "strawberry");

    private final int value;
    private final double probability;
    private final String imageName;
    private BufferedImage image;

    Fruit(int value, double probability, String imageName) {
        this.value = value;
        this.probability = probability;
        this.imageName = imageName;

        try{
            this.image = ImageIO.read(new File("src/main/resources/" + imageName + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getValue() {
        return value;
    }

    public double getProbability() {
        return probability;
    }

    public BufferedImage getImage() {
        return image;
    }
}
