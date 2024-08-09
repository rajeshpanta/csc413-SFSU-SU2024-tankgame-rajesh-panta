package tankwargame;

import tankwargame.menus.StartMenuPanel;
import tankwargame.game.GameWorld;
import tankwargame.game.TankControl;
import tankwargame.game.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Launcher extends JFrame {
    private GameWorld gamePanel;
    private Sound soundManager;

    public Launcher() throws IOException {
        this.setLayout(new BorderLayout());
        this.setSize(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.soundManager = new Sound();
        soundManager.loadSound(Sound.EXPLOSION, "Explosion_large.wav");
        soundManager.loadSound(Sound.FIRE, "Explosion_small.wav");

        showMainMenu();

        this.setVisible(true);
    }

    public void startGame() {
        this.getContentPane().removeAll();
        this.gamePanel = new GameWorld(this, soundManager);
        this.add(gamePanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

        TankControl player1Control = new TankControl(
            gamePanel.getTank1(), 
            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_SPACE
        );
        TankControl player2Control = new TankControl(
            gamePanel.getTank2(), 
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_ENTER
        );

        gamePanel.setPlayer1Control(player1Control);
        gamePanel.setPlayer2Control(player2Control);

        this.addKeyListener(player1Control);
        this.addKeyListener(player2Control);
        this.setFocusable(true);
        this.requestFocusInWindow();

        (new Thread(this.gamePanel)).start();
    }

    public void restartGame() {
        startGame();
    }

    public void showMainMenu() {
        this.getContentPane().removeAll();
        try {
            StartMenuPanel startMenu = new StartMenuPanel(this, soundManager);
            this.add(startMenu, BorderLayout.CENTER);
            this.revalidate();
            this.repaint();
            soundManager.playBackgroundMusic("Music.wav");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load the main menu.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void closeGame() {
        SwingUtilities.invokeLater(this::dispose);
    }

    public void setFrame(String frameType) {
        if ("game".equals(frameType)) {
            startGame();
        }
    }

    public static void main(String[] args) {
        try {
            new Launcher();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
