package tankwargame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet implements GameObject {
    private int x, y;
    private int angle;
    private int speed;
    private BufferedImage image;
    private boolean active;

    public Bullet(int x, int y, int angle, int speed, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;
        this.image = image;
        this.active = true;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public void draw(Graphics g) {
        if (active) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
            g2d.drawImage(image, x, y, null);
            g2d.rotate(-Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
        }
    }

    public void update(int panelWidth, int panelHeight, int wallWidth) {
        if (active) {
            x += speed * Math.cos(Math.toRadians(angle));
            y += speed * Math.sin(Math.toRadians(angle));
            if (x < 0 || x > panelWidth || y < 0 || y > panelHeight) {
                active = false;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
