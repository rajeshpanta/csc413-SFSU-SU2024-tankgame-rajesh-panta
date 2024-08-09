package tankwargame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tank implements GameObject {
    private int x, y;
    private int initialX, initialY;
    private int angle;
    private int speed;
    private BufferedImage image;
    private BufferedImage originalImage;
    private List<Bullet> bullets;
    private BufferedImage bulletImage;
    private int health;
    private int lives;
    private long lastFireTime;
    private static final int FIRE_RATE_LIMIT = 500;
    private boolean destroyed;
    private Sound soundManager;
    private boolean speedBoostActive;
    private long speedBoostEndTime;
    private boolean bulletPowerUpActive;
    private long bulletPowerUpEndTime;

    public Tank(int x, int y, int angle, int speed, BufferedImage image, BufferedImage bulletImage, Sound soundManager) {
        this.x = x;
        this.initialX = x;
        this.y = y;
        this.initialY = y;
        this.angle = angle;
        this.speed = speed;
        this.image = image;
        this.originalImage = image;
        this.bullets = new ArrayList<>();
        this.bulletImage = bulletImage;
        this.health = 100;
        this.lives = 3;
        this.lastFireTime = 0;
        this.destroyed = false;
        this.soundManager = soundManager;
        this.speedBoostActive = false;
        this.bulletPowerUpActive = false;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
            g2d.drawImage(image, x, y, null);
            g2d.rotate(-Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            g.setColor(Color.GREEN);
            g.fillRect(x, y - 10, health / 2, 5);
        }
    }

    public void update(int panelWidth, int panelHeight, int wallWidth) {
        if (!destroyed) {
            for (Bullet bullet : bullets) {
                bullet.update(panelWidth, panelHeight, wallWidth);
            }

            if (speedBoostActive && System.currentTimeMillis() > speedBoostEndTime) {
                deactivateSpeedBoost();
            }

            if (bulletPowerUpActive && System.currentTimeMillis() > bulletPowerUpEndTime) {
                deactivateBulletPowerUp();
            }
        }
    }

    public void fire() {
        long currentTime = System.currentTimeMillis();
        if (!destroyed && currentTime - lastFireTime >= FIRE_RATE_LIMIT) {
            lastFireTime = currentTime;
            int bulletX = x + image.getWidth() / 2 - bulletImage.getWidth() / 2;
            int bulletY = y + image.getHeight() / 2 - bulletImage.getHeight() / 2;
            Bullet bullet = new Bullet(bulletX, bulletY, angle, 5, bulletImage);
            bullets.add(bullet);
            soundManager.playSound(Sound.FIRE);
        }
    }

    public void move(int dx, int dy, int panelWidth, int panelHeight, List<Rectangle> unbreakableWalls, List<BreakableWall> breakableWalls, Tank otherTank) {
        if (!destroyed) {
            int newX = x + dx;
            int newY = y + dy;
            Rectangle newBounds = new Rectangle(newX, newY, image.getWidth(), image.getHeight());

            if (newX >= 0 && newX + image.getWidth() <= panelWidth && newY >= 0 && newY + image.getHeight() <= panelHeight) {
                if (!newBounds.intersects(otherTank.getBounds()) && !isCollidingWithWalls(newBounds, unbreakableWalls, breakableWalls)) {
                    x = newX;
                    y = newY;
                }
            }
        }
    }

    private boolean isCollidingWithWalls(Rectangle newBounds, List<Rectangle> unbreakableWalls, List<BreakableWall> breakableWalls) {
        for (Rectangle wall : unbreakableWalls) {
            if (newBounds.intersects(wall)) {
                return true;
            }
        }
        for (BreakableWall wall : breakableWalls) {
            if (newBounds.intersects(wall.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public void moveForward(int panelWidth, int panelHeight, List<Rectangle> unbreakableWalls, List<BreakableWall> breakableWalls, Tank otherTank) {
        int actualSpeed = speedBoostActive ? speed * 2 : speed;
        int dx = (int) (actualSpeed * Math.cos(Math.toRadians(angle)));
        int dy = (int) (actualSpeed * Math.sin(Math.toRadians(angle)));
        move(dx, dy, panelWidth, panelHeight, unbreakableWalls, breakableWalls, otherTank);
    }

    public void moveBackward(int panelWidth, int panelHeight, List<Rectangle> unbreakableWalls, List<BreakableWall> breakableWalls, Tank otherTank) {
        int actualSpeed = speedBoostActive ? speed * 2 : speed;
        int dx = (int) (-actualSpeed * Math.cos(Math.toRadians(angle)));
        int dy = (int) (-actualSpeed * Math.sin(Math.toRadians(angle)));
        move(dx, dy, panelWidth, panelHeight, unbreakableWalls, breakableWalls, otherTank);
    }

    public void rotateLeft() {
        if (!destroyed) {
            angle -= 5;
        }
    }

    public void rotateRight() {
        if (!destroyed) {
            angle += 5;
        }
    }

    public void decreaseHealth(int amount) {
        if (!destroyed) {
            this.health -= amount;
            if (this.health <= 0) {
                this.health = 0;
                this.destroyed = true;
                this.lives--;
                soundManager.playSound(Sound.EXPLOSION);
            }
        }
    }

    public void resetHealth() {
        this.health = 100;
        this.destroyed = false;
    }

    public boolean isOutOfLives() {
        return lives <= 0;
    }

    public int getLives() {
        return lives;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.destroyed = false;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void checkBulletCollisions(Tank otherTank) {
        if (!destroyed) {
            for (Bullet bullet : bullets) {
                if (bullet.isActive() && bullet.getBounds().intersects(otherTank.getBounds())) {
                    bullet.setActive(false);
                    otherTank.decreaseHealth(10);
                }
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case HEALTH:
                applyHealthPowerUp();
                break;
            case SPEED:
                applySpeedPowerUp();
                break;
            case BULLET:
                applyBulletPowerUp();
                break;
        }
    }

    private void applyHealthPowerUp() {
        if (!destroyed) {
            this.health = Math.min(100, this.health + 50);
        }
    }

    private void applySpeedPowerUp() {
        this.speedBoostActive = true;
        this.speedBoostEndTime = System.currentTimeMillis() + 10000;
    }

    private void deactivateSpeedBoost() {
        this.speedBoostActive = false;
    }

    private void applyBulletPowerUp() {
        this.bulletPowerUpActive = true;
        this.bulletPowerUpEndTime = System.currentTimeMillis() + 10000;
    }

    private void deactivateBulletPowerUp() {
        this.bulletPowerUpActive = false;
    }
}
