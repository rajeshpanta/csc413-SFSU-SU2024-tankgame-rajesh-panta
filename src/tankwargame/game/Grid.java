package tankwargame.game;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grid {
    private final int cellSize;
    private final int rows;
    private final int cols;
    private final List<Set<GameObject>> cells;

    public Grid(int worldWidth, int worldHeight, int cellSize) {
        this.cellSize = cellSize;
        this.rows = (worldHeight + cellSize - 1) / cellSize;
        this.cols = (worldWidth + cellSize - 1) / cellSize;
        this.cells = new ArrayList<>(rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            cells.add(new HashSet<>());
        }
    }

    private int getCellIndex(int x, int y) {
        int row = y / cellSize;
        int col = x / cellSize;
        return row * cols + col;
    }

    public void addObject(GameObject obj) {
        int index = getCellIndex(obj.getX(), obj.getY());
        cells.get(index).add(obj);
    }

    public void removeObject(GameObject obj) {
        int index = getCellIndex(obj.getX(), obj.getY());
        cells.get(index).remove(obj);
    }

    public void updateObjectPosition(GameObject obj, int oldX, int oldY) {
        int oldIndex = getCellIndex(oldX, oldY);
        int newIndex = getCellIndex(obj.getX(), obj.getY());
        if (oldIndex != newIndex) {
            cells.get(oldIndex).remove(obj);
            cells.get(newIndex).add(obj);
        }
    }

    public Set<GameObject> getPotentialCollisions(GameObject obj) {
        Set<GameObject> potentialCollisions = new HashSet<>();
        int x = obj.getX();
        int y = obj.getY();
        int startX = Math.max(0, (x / cellSize) - 1);
        int startY = Math.max(0, (y / cellSize) - 1);
        int endX = Math.min(cols - 1, (x / cellSize) + 1);
        int endY = Math.min(rows - 1, (y / cellSize) + 1);

        for (int row = startY; row <= endY; row++) {
            for (int col = startX; col <= endX; col++) {
                int index = row * cols + col;
                potentialCollisions.addAll(cells.get(index));
            }
        }

        return potentialCollisions;
    }
}
