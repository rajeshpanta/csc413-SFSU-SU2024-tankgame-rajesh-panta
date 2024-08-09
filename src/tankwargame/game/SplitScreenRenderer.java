// package tankwargame.game;

// import java.awt.*;
// import java.awt.geom.AffineTransform;
// import java.awt.image.BufferedImage;

// public class SplitScreenRenderer {
//     private BufferedImage background;
//     private int worldWidth;
//     private int worldHeight;

//     public SplitScreenRenderer(BufferedImage background, int worldWidth, int worldHeight) {
//         this.background = background;
//         this.worldWidth = worldWidth;
//         this.worldHeight = worldHeight;
//     }

//     public void drawSplitScreen(Graphics2D g2d, Tank t1, Tank t2, int panelWidth, int panelHeight) {
//         // Draw Player 1's viewport
//         drawViewport(g2d, t1, 0, 0, panelWidth / 2, panelHeight);

//         // Draw Player 2's viewport
//         drawViewport(g2d, t2, panelWidth / 2, 0, panelWidth / 2, panelHeight);
//     }

//     private void drawViewport(Graphics2D g2d, Tank tank, int x, int y, int width, int height) {
//         // Save the original transform
//         AffineTransform originalTransform = g2d.getTransform();

//         // Set the viewport
//         g2d.setClip(x, y, width, height);

//         // Translate the graphics context to the viewport
//         g2d.translate(x - tank.getX() + width / 2, y - tank.getY() + height / 2);

//         // Debug statements
//         System.out.println("Drawing viewport for tank at (" + tank.getX() + ", " + tank.getY() + ") with viewport (" + x + ", " + y + ", " + width + ", " + height + ")");

//         // Draw the game world
//         g2d.drawImage(background, 0, 0, worldWidth, worldHeight, null);

//         // Restore the original transform
//         g2d.setTransform(originalTransform);
//     }
// }
