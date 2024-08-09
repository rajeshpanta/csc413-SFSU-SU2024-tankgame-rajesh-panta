package tankwargame.game;

import tankwargame.GameConstants;
import tankwargame.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld extends JPanel implements Runnable {
    private Tank t1, t2;
    private BufferedImage t1img, t2img, background, wallImage, breakableWallImage, bulletImage, powerUpBulletImage;
    private TankControl player1Control, player2Control;
    private Launcher launcher;
    private Settings settings;
    private WallManager wallManager;
    private GameManager gameManager;
    private Sound soundManager;
    private Grid grid;
    private MiniMap miniMap1, miniMap2;
    private PowerUpManager powerUpManager;

    private BufferedImage view1, view2;
    private Graphics2D g2dView1, g2dView2;

    private int worldWidth;
    private int worldHeight;
    private int initialWidth;
    private int initialHeight;
    private List<Explosion> explosions;

    public GameWorld(Launcher launcher, Sound soundManager) {
        this.launcher = launcher;
        this.soundManager = soundManager;
        this.setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);

        this.worldWidth = GameConstants.WORLD_WIDTH;
        this.worldHeight = GameConstants.WORLD_HEIGHT;
        this.initialWidth = GameConstants.SCREEN_WIDTH;
        this.initialHeight = GameConstants.SCREEN_HEIGHT;
        explosions = new ArrayList<>();

        ImageLoader imageLoader = new ImageLoader();
        loadImages(imageLoader);
        wallManager = new WallManager(worldWidth, worldHeight);

        BufferedImage healthImage = imageLoader.loadImage("HealthKit.jpeg");
        BufferedImage speedImage = imageLoader.loadImage("Rocket.gif");
        powerUpBulletImage = imageLoader.loadImage("Bouncing.gif");
        BufferedImage referenceImage = imageLoader.loadImage("Bouncing.gif");
        powerUpManager = new PowerUpManager(worldWidth, worldHeight, healthImage, speedImage, powerUpBulletImage, referenceImage);

        TankInitializer tankInitializer = new TankInitializer(t1img, t2img, bulletImage, soundManager, wallManager);
        t1 = tankInitializer.createTank1();
        t2 = tankInitializer.createTank2();

        grid = new Grid(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, 100);
        grid.addObject(t1);
        grid.addObject(t2);

        gameManager = new GameManager(this, launcher);
        gameManager.setTanks(t1, t2);

        initializeControls();

        settings = new Settings(launcher);
        this.setLayout(new BorderLayout());
        this.add(settings.getSettingsButton(), BorderLayout.NORTH);

        miniMap1 = new MiniMap(this, 200, 200);
        miniMap2 = new MiniMap(this, 200, 200);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onResize(e);
            }
        });

        // Play game background music
        if (this.soundManager != null) {
            this.soundManager.stopBackgroundMusic(); // Stop menu music if playing
            this.soundManager.playBackgroundMusic("MusicGame.wav");
        }

        this.view1 = new BufferedImage(initialWidth / 2, initialHeight, BufferedImage.TYPE_INT_ARGB);
        this.view2 = new BufferedImage(initialWidth / 2, initialHeight, BufferedImage.TYPE_INT_ARGB);
        this.g2dView1 = view1.createGraphics();
        this.g2dView2 = view2.createGraphics();
    }

    public void triggerExplosion(int x, int y) {
        explosions.add(new Explosion(x, y));
    }

    private void onResize(ComponentEvent e) {
        Dimension newSize = e.getComponent().getSize();
        int newWidth = newSize.width / 2; // Each view takes half the width
        int newHeight = newSize.height;

        view1 = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        view2 = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        g2dView1 = view1.createGraphics();
        g2dView2 = view2.createGraphics();

        repaint();
    }

    private void resizeTank(Tank tank, double xScale, double yScale) {
        BufferedImage originalImage = tank.getOriginalImage();
        BufferedImage scaledImage = new BufferedImage(
            (int) (originalImage.getWidth() * Math.min(xScale, yScale)),
            (int) (originalImage.getHeight() * Math.min(xScale, yScale)),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
        g2d.dispose();

        tank.setImage(scaledImage);
        tank.setPosition((int) (tank.getInitialX() * xScale), (int) (tank.getInitialY() * yScale));
    }

    public WallManager getWallManager() {
        return wallManager;
    }

    public Sound getSoundManager() {
        return soundManager;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public PowerUpManager getPowerUpManager() {
        return this.powerUpManager;
    }

    private void loadImages(ImageLoader imageLoader) {
        t1img = imageLoader.loadImage("tank1.png");
        t2img = imageLoader.loadImage("tank2.png");
        background = imageLoader.loadImage("Background.bmp");
        wallImage = imageLoader.loadImage("wall2.png");
        breakableWallImage = imageLoader.loadImage("wall1.png");
        bulletImage = imageLoader.loadImage("Shell.gif");
    }

    private void initializeControls() {
        TankControl player1Control = new TankControl(
            t1, 
            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            KeyEvent.VK_SPACE
        );
        TankControl player2Control = new TankControl(
            t2, 
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_ENTER
        );

        this.setPlayer1Control(player1Control);
        this.setPlayer2Control(player2Control);

        this.addKeyListener(player1Control);
        this.addKeyListener(player2Control);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawViewport(g2dView1, t1, view1);
        drawViewport(g2dView2, t2, view2);

        g.drawImage(view1, 0, 0, this);
        g.drawImage(view2, view1.getWidth(), 0, this);

        // Draw mini-maps on top
        miniMap1.draw((Graphics2D) g);
        miniMap2.draw((Graphics2D) g);

        // Draw lives count on top
        drawLives(g);
    }

    private void drawViewport(Graphics2D g2d, Tank tank, BufferedImage view) {
        int viewportWidth = view.getWidth();
        int viewportHeight = view.getHeight();
        
        int tankX = Math.max(0, Math.min(tank.getX() - viewportWidth / 2, worldWidth - viewportWidth));
        int tankY = Math.max(0, Math.min(tank.getY() - viewportHeight / 2, worldHeight - viewportHeight));
        
        g2d.translate(-tankX, -tankY);

        drawBackground(g2d);
        drawGameElements(g2d);

        g2d.translate(tankX, tankY);
    }

    private void drawBackground(Graphics2D g2d) {
        for (int x = 0; x < worldWidth; x += background.getWidth()) {
            for (int y = 0; y < worldHeight; y += background.getHeight()) {
                g2d.drawImage(background, x, y, this);
            }
        }
    }

    private void drawGameElements(Graphics2D g2d) {
        drawWalls(g2d);
        drawBreakableWalls(g2d);
        drawUnbreakableWalls(g2d);
        if (!t1.isDestroyed()) {
            t1.draw(g2d);
        }
        if (!t2.isDestroyed()) {
            t2.draw(g2d);
        }
        powerUpManager.draw(g2d);
        synchronized (explosions) {
            Iterator<Explosion> it = explosions.iterator();
            while (it.hasNext()) {
                Explosion explosion = it.next();
                explosion.draw(g2d);
                if (explosion.isFinished()) {
                    it.remove();
                }
            }
        }
    }

    private void drawLives(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1 Lives: " + t1.getLives(), 10, 30);
        g.drawString("Player 2 Lives: " + t2.getLives(), getWidth() / 2 + 10, 30);
    }

    private void drawWalls(Graphics g) {
        for (int x = 0; x < worldWidth; x += wallManager.getWallWidth()) {
            g.drawImage(wallImage, x, 0, wallManager.getWallWidth(), wallManager.getWallWidth(), this);
            g.drawImage(wallImage, x, worldHeight - wallManager.getWallWidth(), wallManager.getWallWidth(), wallManager.getWallWidth(), this);
        }
        for (int y = wallManager.getWallWidth(); y < worldHeight - wallManager.getWallWidth(); y += wallManager.getWallWidth()) {
            g.drawImage(wallImage, 0, y, wallManager.getWallWidth(), wallManager.getWallWidth(), this);
            g.drawImage(wallImage, worldWidth - wallManager.getWallWidth(), y, wallManager.getWallWidth(), wallManager.getWallWidth(), this);
        }
    }

    private void drawBreakableWalls(Graphics g) {
        for (BreakableWall wall : wallManager.getBreakableWalls()) {
            g.drawImage(breakableWallImage, wall.getBounds().x, wall.getBounds().y, wall.getBounds().width, wall.getBounds().height, this);
        }
    }

    private void drawUnbreakableWalls(Graphics g) {
        for (Rectangle wall : wallManager.getUnbreakableWalls()) {
            g.drawImage(wallImage, wall.x, wall.y, wall.width, wall.height, this);
        }
    }

    public void update() {
        if (gameManager.isGameRunning()) {
            checkBulletCollisionsWithWalls();
            checkBulletCollisionsWithTanks();
            checkTankCollisionsWithPowerUps();
            powerUpManager.update();
            gameManager.checkForCollisions();

            if (player1Control != null && !t1.isDestroyed()) {
                if (player1Control.isForwardPressed()) {
                    t1.moveForward(worldWidth, worldHeight, wallManager.getUnbreakableWalls(), wallManager.getBreakableWalls(), t2);
                }
                if (player1Control.isBackwardPressed()) {
                    t1.moveBackward(worldWidth, worldHeight, wallManager.getUnbreakableWalls(), wallManager.getBreakableWalls(), t2);
                }
                if (player1Control.isLeftPressed()) {
                    t1.rotateLeft();
                }
                if (player1Control.isRightPressed()) {
                    t1.rotateRight();
                }
            }

            if (player2Control != null && !t2.isDestroyed()) {
                if (player2Control.isForwardPressed()) {
                    t2.moveForward(worldWidth, worldHeight, wallManager.getUnbreakableWalls(), wallManager.getBreakableWalls(), t1);
                }
                if (player2Control.isBackwardPressed()) {
                    t2.moveBackward(worldWidth, worldHeight, wallManager.getUnbreakableWalls(), wallManager.getBreakableWalls(), t1);
                }
                if (player2Control.isLeftPressed()) {
                    t2.rotateLeft();
                }
                if (player2Control.isRightPressed()) {
                    t2.rotateRight();
                }
            }

            if (!t1.isDestroyed()) {
                t1.update(worldWidth, worldHeight, wallManager.getWallWidth());
                grid.updateObjectPosition(t1, t1.getX(), t1.getY());
            } else if (!t1.isOutOfLives()) {
                gameManager.respawnTank(t1);
                t1.setDestroyed(false);
            }
            if (!t2.isDestroyed()) {
                t2.update(worldWidth, worldHeight, wallManager.getWallWidth());
                grid.updateObjectPosition(t2, t2.getX(), t2.getY());
            } else if (!t2.isOutOfLives()) {
                gameManager.respawnTank(t2);
                t2.setDestroyed(false);
            }
            Iterator<Explosion> it = explosions.iterator();
            while (it.hasNext()) {
                Explosion explosion = it.next();
                explosion.update();
                if (explosion.isFinished()) {
                    it.remove();
                }
            }
            repaint();
        }
    }

    private void checkBulletCollisionsWithWalls() {
        List<Bullet> bullets = new ArrayList<>();
        bullets.addAll(t1.getBullets());
        bullets.addAll(t2.getBullets());

        for (Bullet bullet : bullets) {
            for (BreakableWall wall : wallManager.getBreakableWalls()) {
                if (bullet.isActive() && wall.getBounds().intersects(bullet.getBounds())) {
                    bullet.setActive(false);
                    wall.reduceHealth();
                    if (wall.isBroken()) {
                        wallManager.getBreakableWalls().remove(wall);
                    }
                    break;
                }
            }

            for (Rectangle wall : wallManager.getUnbreakableWalls()) {
                if (bullet.isActive() && wall.intersects(bullet.getBounds())) {
                    bullet.setActive(false);
                    break;
                }
            }
        }
    }

    private void checkBulletCollisionsWithTanks() {
        if (!t1.isDestroyed()) {
            t1.checkBulletCollisions(t2);
        }
        if (!t2.isDestroyed()) {
            t2.checkBulletCollisions(t1);
        }
    }

    private void checkTankCollisionsWithPowerUps() {
        for (PowerUp powerUp : powerUpManager.getPowerUps()) {
            if (powerUp.isActive()) {
                if (t1.getBounds().intersects(powerUp.getBounds())) {
                    t1.applyPowerUp(powerUp);
                    powerUp.setActive(false);
                }
                if (t2.getBounds().intersects(powerUp.getBounds())) {
                    t2.applyPowerUp(powerUp);
                    powerUp.setActive(false);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            update();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Tank getTank1() {
        return t1;
    }

    public Tank getTank2() {
        return t2;
    }

    public void setPlayer1Control(TankControl player1Control) {
        this.player1Control = player1Control;
    }

    public void setPlayer2Control(TankControl player2Control) {
        this.player2Control = player2Control;
    }

    public boolean isTankCollision(int x, int y, Tank currentTank) {
        Rectangle newBounds = new Rectangle(x, y, currentTank.getImage().getWidth(), currentTank.getImage().getHeight());
        if (currentTank.equals(t1)) {
            return newBounds.intersects(t2.getBounds());
        } else if (currentTank.equals(t2)) {
            return newBounds.intersects(t1.getBounds());
        }
        return false;
    }
}
