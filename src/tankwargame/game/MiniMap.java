package tankwargame.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MiniMap extends JPanel {
    private GameWorld gameWorld;
    private BufferedImage mapImage;
    private int miniMapWidth;
    private int miniMapHeight;

    public MiniMap(GameWorld gameWorld, int width, int height) {
        this.gameWorld = gameWorld;
        this.miniMapWidth = width;
        this.miniMapHeight = height;

        try {
            mapImage = ImageIO.read(getClass().getClassLoader().getResource("mapImage.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            mapImage = null;  // Ensure mapImage is set to null if loading fails
        }

        this.setPreferredSize(new Dimension(miniMapWidth, miniMapHeight));
    }

    public void draw(Graphics2D g2d) {
        int offsetX = 10;  // Offset to position the mini-map correctly
        int offsetY = 10;

        if (mapImage != null) {
            g2d.drawImage(mapImage, offsetX, offsetY, miniMapWidth, miniMapHeight, this);
        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(offsetX, offsetY, miniMapWidth, miniMapHeight);
        }

        double xScale = miniMapWidth / (double) gameWorld.getWorldWidth();
        double yScale = miniMapHeight / (double) gameWorld.getWorldHeight();

        drawTank(g2d, gameWorld.getTank1(), xScale, yScale, offsetX, offsetY);
        drawTank(g2d, gameWorld.getTank2(), xScale, yScale, offsetX, offsetY);
        drawPowerUps(g2d, xScale, yScale, offsetX, offsetY);
        drawWalls(g2d, xScale, yScale, offsetX, offsetY);
    }

    private void drawTank(Graphics2D g, Tank tank, double xScale, double yScale, int offsetX, int offsetY) {
        if (!tank.isDestroyed()) {
            int miniX = offsetX + (int) (tank.getX() * xScale);
            int miniY = offsetY + (int) (tank.getY() * yScale);
            int miniWidth = (int) (tank.getImage().getWidth() * xScale);
            int miniHeight = (int) (tank.getImage().getHeight() * yScale);
            g.setColor(Color.RED);
            g.fillRect(miniX, miniY, miniWidth, miniHeight);
        }
    }

    private void drawPowerUps(Graphics2D g, double xScale, double yScale, int offsetX, int offsetY) {
        for (PowerUp powerUp : gameWorld.getPowerUpManager().getPowerUps()) {
            if (powerUp.isActive()) {
                Rectangle bounds = powerUp.getBounds();
                int miniX = offsetX + (int) (bounds.x * xScale);
                int miniY = offsetY + (int) (bounds.y * yScale);
                int miniWidth = (int) (bounds.width * xScale);
                int miniHeight = (int) (bounds.height * yScale);
                g.setColor(Color.BLACK);
                g.fillRect(miniX, miniY, miniWidth, miniHeight);
            }
        }
    }

    private void drawWalls(Graphics2D g, double xScale, double yScale, int offsetX, int offsetY) {
        for (Rectangle wall : gameWorld.getWallManager().getUnbreakableWalls()) {
            int miniX = offsetX + (int) (wall.x * xScale);
            int miniY = offsetY + (int) (wall.y * yScale);
            int miniWidth = (int) (wall.width * xScale);
            int miniHeight = (int) (wall.height * yScale);
            g.setColor(Color.BLUE);
            g.fillRect(miniX, miniY, miniWidth, miniHeight);
        }

        for (BreakableWall wall : gameWorld.getWallManager().getBreakableWalls()) {
            int miniX = offsetX + (int) (wall.getBounds().x * xScale);
            int miniY = offsetY + (int) (wall.getBounds().y * yScale);
            int miniWidth = (int) (wall.getBounds().width * xScale);
            int miniHeight = (int) (wall.getBounds().height * yScale);
            g.setColor(Color.GREEN);
            g.fillRect(miniX, miniY, miniWidth, miniHeight);
        }
    }
}
