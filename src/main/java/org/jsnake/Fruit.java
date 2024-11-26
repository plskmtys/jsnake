package org.jsnake;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A kígyó által megehető gyümölcsök.
 */
public enum Fruit {
    APPLE(1, 1, "apple"),
    PEAR(2, 0.4, "pear"),
    BANANA(3, 0.2, "banana"),
    STRAWBERRY(5, 0.1, "strawberry");

    /**
     * A gyümölcs értéke.
     */
    private final int value;

    /**
     * A gyümölcs előfordulásának valószínűsége.
     */
    private final double probability;

    /**
     * A gyümölcs képe.
     */
    private BufferedImage image;

    /**
     * Konstruktor.
     * @param value A gyümölcs értéke.
     * @param probability A gyümölcs előfordulásának valószínűsége.
     * @param imageName A gyümölcs képének neve.
     */
    Fruit(int value, double probability, String imageName) {
        this.value = value;
        this.probability = probability;

        try{
            this.image = ImageIO.read(new File("src/main/resources/" + imageName + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Visszaadja a gyümölcs értékét.
     * @return A gyümölcs értéke.
     */
    public int getValue() {
        return value;
    }

    /**
     * Visszaadja a gyümölcs előfordulásának valószínűségét.
     * @return A gyümölcs előfordulásának valószínűsége.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Visszaadja a gyümölcs képét.
     * @return A gyümölcs képe.
     */
    public BufferedImage getImage() {
        return image;
    }
}
