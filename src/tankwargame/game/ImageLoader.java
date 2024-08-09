package tankwargame.game;

import javax.imageio.ImageIO;
import java.awt.Graphics2D; // Ensure this import is present
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader {
    public BufferedImage loadImage(String path) {
        URL resourceUrl = getClass().getClassLoader().getResource(path);
        if (resourceUrl == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            try {
                BufferedImage image = ImageIO.read(resourceUrl);
                System.out.println("Successfully loaded image from path: " + path);
                return image;
            } catch (IOException e) {
                System.err.println("Failed to load image from path: " + path);
                e.printStackTrace();
                return null;
            }
        }
    }


    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }
}
