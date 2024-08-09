package tankwargame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Explosion {
    private List<BufferedImage> frames;
    private int x, y;
    private int frameIndex;
    private int ticksPerFrame;
    private int tickCount;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.frames = new ArrayList<>();
        this.frameIndex = 0;
        this.ticksPerFrame = 3;  // Adjust for animation speed
        this.tickCount = 0;

        // Load the explosion frames
        for (int i = 1; i <= 6; i++) {
            String filename = String.format("/explosion_sm/explosion_sm_%04d.png", i);
            try {
                BufferedImage frame = ImageIO.read(getClass().getResource(filename));
                if (frame != null) {
                    frames.add(frame);
                } else {
                    System.out.println("Failed to load frame: " + filename);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filename);
                e.printStackTrace();
            }
        }


    }

    public void update() {
        tickCount++;
        if (tickCount >= ticksPerFrame) {
            tickCount = 0;
            frameIndex++;
            if (frameIndex >= frames.size()) {
                frameIndex = frames.size() - 1; // Stop at the last frame
            }
        }
    }

    public boolean isFinished() {
        return frameIndex >= frames.size() - 1;
    }

    public void draw(Graphics g) {
        if (!isFinished()) {
            BufferedImage currentFrame = frames.get(frameIndex);
            g.drawImage(currentFrame, x - currentFrame.getWidth() / 2, y - currentFrame.getHeight() / 2, null);
        }
    }
}
