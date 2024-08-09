// package tankwargame.game;

// import java.awt.*;

// public class UnbreakableWall {
//     private Rectangle bounds;
//     private int initialX, initialY, initialWidth, initialHeight;

//     public UnbreakableWall(int x, int y, int width, int height) {
//         this.bounds = new Rectangle(x, y, width, height);
//         this.initialX = x;
//         this.initialY = y;
//         this.initialWidth = width;
//         this.initialHeight = height;
//     }

//     public Rectangle getBounds() {
//         return bounds;
//     }

//     public void resize(double xScale, double yScale, int initialWallWidth) {
//         int newX = (int) (initialX * xScale);
//         int newY = (int) (initialY * yScale);
//         int newWidth = (int) (initialWidth * Math.min(xScale, yScale));
//         int newHeight = (int) (initialHeight * Math.min(xScale, yScale));
//         bounds.setBounds(newX, newY, newWidth, newHeight);
//     }
// }
