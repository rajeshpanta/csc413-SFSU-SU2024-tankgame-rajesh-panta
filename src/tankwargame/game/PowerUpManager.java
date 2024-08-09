package tankwargame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PowerUpManager {
    private List<PowerUp> powerUps;
    private BufferedImage healthImage, speedImage, bulletImage;
    private int gameWidth, gameHeight;
    private Random rand;
    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 30000; // 30 seconds
    private static final long DURATION = 20000; // 20 seconds

    public PowerUpManager(int gameWidth, int gameHeight, BufferedImage healthImage, BufferedImage speedImage, BufferedImage bulletImage, BufferedImage referenceImage) {
        this.powerUps = new ArrayList<>();
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        if (referenceImage == null) {
            throw new IllegalArgumentException("Reference image cannot be null.");
        }

        int targetWidth = referenceImage.getWidth();
        int targetHeight = referenceImage.getHeight();

        ImageLoader imageLoader = new ImageLoader();
        this.healthImage = safeResize(imageLoader, healthImage, targetWidth, targetHeight);
        this.speedImage = safeResize(imageLoader, speedImage, targetWidth, targetHeight);
        this.bulletImage = safeResize(imageLoader, bulletImage, targetWidth, targetHeight);

        this.rand = new Random();
        this.lastSpawnTime = System.currentTimeMillis();
    }

    private BufferedImage safeResize(ImageLoader loader, BufferedImage image, int width, int height) {
        if (image == null) {
            throw new IllegalArgumentException("Image to resize cannot be null.");
        }
        return loader.resizeImage(image, width, height);
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        // Remove expired power-ups
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (currentTime - powerUp.getSpawnTime() > DURATION) {
                iterator.remove();
            }
        }

        // Spawn new power-ups if the interval has passed
        if (currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
            spawnPowerUp();
            lastSpawnTime = currentTime;
        }
    }

    private void spawnPowerUp() {
        int x = rand.nextInt(gameWidth - healthImage.getWidth());
        int y = rand.nextInt(gameHeight - healthImage.getHeight());
        PowerUp.Type type = PowerUp.Type.values()[rand.nextInt(PowerUp.Type.values().length)];
        BufferedImage image;

        switch (type) {
            case HEALTH:
                image = healthImage;
                break;
            case SPEED:
                image = speedImage;
                break;
            case BULLET:
                image = bulletImage;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        powerUps.add(new PowerUp(x, y, type, image, System.currentTimeMillis()));
    }

    public void draw(Graphics g) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                powerUp.draw(g);
            }
        }
    }
}
