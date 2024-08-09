package tankwargame.menus;

import tankwargame.Launcher;
import tankwargame.game.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class StartMenuPanel extends JPanel {
    private Launcher lf;
    private Image backgroundImage;
    private Sound soundManager;

    public StartMenuPanel(Launcher lf, Sound soundManager) throws IOException {
        this.lf = lf;
        this.soundManager = soundManager;
        this.setLayout(new GridBagLayout());

        // Load the image from the resources folder
        URL imgURL = getClass().getClassLoader().getResource("title.png");
        if (imgURL == null) {
            System.out.println("Image not found in resources: title.png");
            throw new IOException("Image not found in resources: title.png");
        } else {
            System.out.println("Image found at: " + imgURL.getPath());
        }
        backgroundImage = ImageIO.read(imgURL);

        JLabel label = new JLabel("Tank Wars", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 48));
        label.setForeground(Color.WHITE);

        JButton start = new JButton("Start Game");
        JButton exit = new JButton("Exit");

        start.setPreferredSize(new Dimension(200, 50));
        exit.setPreferredSize(new Dimension(200, 50));

        start.addActionListener(actionEvent -> {
            this.soundManager.stopBackgroundMusic(); // Stop the music when transitioning to the game
            this.lf.setFrame("game");
        });
        exit.addActionListener(actionEvent -> {
            this.lf.closeGame();
            System.exit(0); // Terminate the application completely
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(label, gbc);

        gbc.gridy = 1;
        this.add(start, gbc);

        gbc.gridy = 2;
        this.add(exit, gbc);

        // Play background music if it's not already playing
        if (this.soundManager != null) {
            this.soundManager.playBackgroundMusic("Music.wav");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
