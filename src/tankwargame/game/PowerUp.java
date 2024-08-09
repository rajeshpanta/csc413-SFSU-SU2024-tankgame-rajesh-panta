package tankwargame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp {
    public enum Type {
        HEALTH, SPEED, BULLET
    }

    private int x, y;
    private Type type;
    private BufferedImage image;
    private boolean active;
    private long spawnTime;

    public PowerUp(int x, int y, Type type, BufferedImage image, long spawnTime) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.image = image;
        this.active = true;
        this.spawnTime = spawnTime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public void draw(Graphics g) {
        if (active) {
            System.out.println("Drawing power-up: " + type + " at (" + x + ", " + y + ")");
            g.drawImage(image, x, y, null);
        }
    }
}
