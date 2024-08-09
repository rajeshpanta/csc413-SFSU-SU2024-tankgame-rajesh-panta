package tankwargame.game;

import java.awt.*;

public class BreakableWall {
    private Rectangle bounds;
    private int health;
    private int initialX, initialY, initialWidth, initialHeight;

    public BreakableWall(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.initialX = x;
        this.initialY = y;
        this.initialWidth = width;
        this.initialHeight = height;
        this.health = 2; // Takes 2 bullets to break
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }

    public void reduceHealth() {
        this.health -= 1;
    }

    public boolean isBroken() {
        return health <= 0;
    }

    public void resize(double xScale, double yScale, int initialWallWidth) {
        int newX = (int) (initialX * xScale);
        int newY = (int) (initialY * yScale);
        int newWidth = (int) (initialWidth * Math.min(xScale, yScale));
        int newHeight = (int) (initialHeight * Math.min(xScale, yScale));
        bounds.setBounds(newX, newY, newWidth, newHeight);
    }
}
