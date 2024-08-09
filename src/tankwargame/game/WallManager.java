package tankwargame.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallManager {
    private List<BreakableWall> breakableWalls;
    private List<Rectangle> unbreakableWalls;
    private int initialWallWidth;
    private int wallWidth;
    private int gameWidth;
    private int gameHeight;

    public WallManager(int gameWidth, int gameHeight) {
        this.breakableWalls = new ArrayList<>();
        this.unbreakableWalls = new ArrayList<>();
        this.initialWallWidth = 32;
        this.wallWidth = initialWallWidth;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        initializeWalls();
    }

    public int getWallWidth() {
        return wallWidth;
    }

    public void setWallWidth(int wallWidth) {
        this.wallWidth = wallWidth;
    }

    public List<BreakableWall> getBreakableWalls() {
        return breakableWalls;
    }

    public List<Rectangle> getUnbreakableWalls() {
        return unbreakableWalls;
    }

    private void initializeWalls() {
        Random rand = new Random();

        for (int x = 0; x < gameWidth; x += initialWallWidth) {
            unbreakableWalls.add(new Rectangle(x, 0, initialWallWidth, initialWallWidth));
            unbreakableWalls.add(new Rectangle(x, gameHeight - initialWallWidth, initialWallWidth, initialWallWidth));
        }
        for (int y = initialWallWidth; y < gameHeight - initialWallWidth; y += initialWallWidth) {
            unbreakableWalls.add(new Rectangle(0, y, initialWallWidth, initialWallWidth));
            unbreakableWalls.add(new Rectangle(gameWidth - initialWallWidth, y, initialWallWidth, initialWallWidth));
        }

        for (int i = 0; i < 20; i++) {
            int wallCount = rand.nextInt(4) + 1;
            int startX, startY;
            Rectangle wallBounds;
            do {
                startX = rand.nextInt(gameWidth - initialWallWidth * 2);
                startY = rand.nextInt(gameHeight - initialWallWidth * 2);
                wallBounds = new Rectangle(startX, startY, initialWallWidth * ((wallCount > 2) ? 2 : 1), initialWallWidth * ((wallCount > 2) ? 2 : 1));
            } while (isOverlapping(wallBounds));

            for (int j = 0; j < wallCount; j++) {
                int x = startX + (j % 2) * initialWallWidth;
                int y = startY + (j / 2) * initialWallWidth;
                breakableWalls.add(new BreakableWall(x, y, initialWallWidth, initialWallWidth));
            }
        }

        for (int i = 0; i < 10; i++) {
            int wallCount = rand.nextInt(2) + 2;
            int startX, startY;
            Rectangle wallBounds;
            do {
                startX = rand.nextInt(gameWidth - initialWallWidth * 2);
                startY = rand.nextInt(gameHeight - initialWallWidth * 2);
                wallBounds = new Rectangle(startX, startY, initialWallWidth * ((wallCount > 1) ? 2 : 1), initialWallWidth * ((wallCount > 1) ? 2 : 1));
            } while (isOverlapping(wallBounds));

            for (int j = 0; j < wallCount; j++) {
                int x = startX + (j % 2) * initialWallWidth;
                int y = startY + (j / 2) * initialWallWidth;
                unbreakableWalls.add(new Rectangle(x, y, initialWallWidth, initialWallWidth));
            }
        }
    }

    public boolean isOverlapping(Rectangle bounds) {
        for (BreakableWall wall : breakableWalls) {
            if (wall.getBounds().intersects(bounds)) {
                return true;
            }
        }
        for (Rectangle wall : unbreakableWalls) {
            if (wall.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    public void resizeWalls(double xScale, double yScale) {
        for (Rectangle wall : unbreakableWalls) {
            int newX = (int) (wall.x * xScale);
            int newY = (int) (wall.y * yScale);
            int newWidth = (int) (initialWallWidth * Math.min(xScale, yScale));
            int newHeight = (int) (initialWallWidth * Math.min(xScale, yScale));
            wall.setBounds(newX, newY, newWidth, newHeight);
        }
        for (BreakableWall wall : breakableWalls) {
            wall.resize(xScale, yScale, initialWallWidth);
        }
    }
}
