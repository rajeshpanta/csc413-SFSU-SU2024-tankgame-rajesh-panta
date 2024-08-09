package tankwargame.game;

import tankwargame.GameConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * TankInitializer handles the initialization of tanks in the game.
 */
public class TankInitializer {
    private BufferedImage tank1Image;
    private BufferedImage tank2Image;
    private BufferedImage bulletImage;
    private Sound soundManager;
    private WallManager wallManager;

    public TankInitializer(BufferedImage tank1Image, BufferedImage tank2Image, BufferedImage bulletImage, Sound soundManager, WallManager wallManager) {
        this.tank1Image = tank1Image;
        this.tank2Image = tank2Image;
        this.bulletImage = bulletImage;
        this.soundManager = soundManager;
        this.wallManager = wallManager;
    }

    /**
     * Initializes a tank at a random position that does not overlap with walls.
     *
     * @param x Initial x-coordinate.
     * @param y Initial y-coordinate.
     * @param tankImage The image of the tank.
     * @return The initialized Tank object.
     */
    public Tank initializeTank(int x, int y, BufferedImage tankImage) {
        Rectangle tankBounds = new Rectangle(x, y, tankImage.getWidth(), tankImage.getHeight());
        while (wallManager.isOverlapping(tankBounds)) {
            x = new Random().nextInt(GameConstants.WORLD_WIDTH - tankImage.getWidth());
            y = new Random().nextInt(GameConstants.WORLD_HEIGHT - tankImage.getHeight());
            tankBounds.setLocation(x, y);
        }
        return new Tank(x, y, 0, 2, tankImage, bulletImage, soundManager);
    }

    public Tank createTank1() {
        return initializeTank(300, 300, tank1Image);
    }

    public Tank createTank2() {
        return initializeTank(500, 300, tank2Image);
    }
}
