package org.jsnake;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.awt.Graphics2D;

public class TextureCreator {

    private TextureCreator(){/* */}

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
    
    public static BufferedImage combine(BufferedImage base, BufferedImage overlay, Color fill) {
        return overlayImages(applyColorWithMultiply(base, fill), overlay);
    }

}
