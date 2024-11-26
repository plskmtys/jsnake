package org.jsnake;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
 * Ez az osztály a kígyó textúrájának létrehozására szolgál.
 * Ez megengedi azt, hogy a kígyó különböző színekben jelenjen meg, de maradjanak rajta a részletek.
 */
public class TextureCreator {
    /**
     * A konstruktor privát, mert nem szükséges példányosítani az osztályt.
     */
    private TextureCreator(){}

    /**
     * A metódus egy képet színez megadott színnel.
     * @param source A színezendő kép.
     * @param color A szín.
     * @return A színezett kép.
     */
    public static BufferedImage applyColorWithMultiply(BufferedImage source, Color color) {
        int width = source.getWidth();
        int height = source.getHeight();
    
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        float rColor = color.getRed() / 255f;
        float gColor = color.getGreen() / 255f;
        float bColor = color.getBlue() / 255f;
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = source.getRGB(x, y);
    
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
    
                red = (int) (red / 255f * rColor * 255);
                green = (int) (green / 255f * gColor * 255);
                blue = (int) (blue / 255f * bColor * 255);
    
                int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                result.setRGB(x, y, newPixel);
            }
        }
    
        return result;
    }

    /**
     * A metódus két képet egymásra helyez.
     * @param baseImage Az alsó kép.
     * @param overlay A felső kép.
     * @return Az egymásra helyezett kép.
     */
    public static BufferedImage overlayImages(BufferedImage baseImage, BufferedImage overlay) {
        BufferedImage result = new BufferedImage(
            baseImage.getWidth(),
            baseImage.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
    
        Graphics2D g = result.createGraphics();
    
        g.drawImage(baseImage, 0, 0, null);
    
        g.drawImage(overlay, 0, 0, null);
        g.dispose();
    
        return result;
    }
    
    /**
     * A metódus egy képet színez megadott színnel, majd ráhelyezi egy másik képet.
     * @param base Az alsó kép.
     * @param overlay A felső kép.
     * @param fill A szín.
     * @return Az egymásra helyezett és színezett kép.
     */
    public static BufferedImage combine(BufferedImage base, BufferedImage overlay, Color fill) {
        return overlayImages(applyColorWithMultiply(base, fill), overlay);
    }

}
