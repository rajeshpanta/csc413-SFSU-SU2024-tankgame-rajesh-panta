package tankwargame.game;

import tankwargame.GameConstants;
import tankwargame.Launcher;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameManager {
    private static final int MAX_LIVES = 3;
    private Tank t1, t2;
    private GameWorld gameWorld;
    private boolean gameRunning;
    private Launcher launcher;

    private Sound soundManager;

    public GameManager(GameWorld gameWorld, Launcher launcher) {
        this.gameWorld = gameWorld;
        this.launcher = launcher;
        this.gameRunning = true;
        this.soundManager = new Sound();
        this.soundManager.loopSound(Sound.MUSIC);
    }

    public void setTanks(Tank t1, Tank t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void checkForCollisions() {
        checkTankDestruction(t1, "Player 2 wins!");
        checkTankDestruction(t2, "Player 1 wins!");
    }

    private void checkTankDestruction(Tank tank, String winMessage) {
        if (tank.isDestroyed()) {
            System.out.println("Tank destroyed: " + winMessage); // Debugging output
            if (tank.isOutOfLives()) {
                endGame(winMessage);
            } else {
                int centerX = tank.getX() + tank.getImage().getWidth() / 2;
                int centerY = tank.getY() + tank.getImage().getHeight() / 2;
                System.out.println("Triggering explosion at: " + centerX + ", " + centerY); // Debugging output
                gameWorld.triggerExplosion(centerX, centerY);
                respawnTank(tank);
            }
        }
    }
    public void respawnTank(Tank tank) {
        Random rand = new Random();
        int x, y;
        Rectangle newBounds;
        do {
            x = rand.nextInt(GameConstants.WORLD_WIDTH - tank.getImage().getWidth());
            y = rand.nextInt(GameConstants.WORLD_HEIGHT - tank.getImage().getHeight());
            newBounds = new Rectangle(x, y, tank.getImage().getWidth(), tank.getImage().getHeight());
        } while (gameWorld.getWallManager().isOverlapping(newBounds) || gameWorld.isTankCollision(x, y, tank));

        tank.setPosition(x, y);
        tank.resetHealth();
    }

    private void endGame(String message) {
        gameWorld.triggerExplosion(t1.getX() + t1.getImage().getWidth() / 2, t1.getY() + t1.getImage().getHeight() / 2);
        gameWorld.triggerExplosion(t2.getX() + t2.getImage().getWidth() / 2, t2.getY() + t2.getImage().getHeight() / 2);
        gameRunning = false;
        soundManager.stopAllSounds(); // This will stop all playing sounds
        showEndGameDialog(message);
    }
    private void showEndGameDialog(String message) {
        int option = JOptionPane.showOptionDialog(gameWorld,
                message + "\nWould you like to restart or quit?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Restart", "Quit"},
                "Restart");

        if (option == JOptionPane.YES_OPTION) {
            launcher.restartGame();
        } else {
            launcher.showMainMenu();
        }
    }
}
