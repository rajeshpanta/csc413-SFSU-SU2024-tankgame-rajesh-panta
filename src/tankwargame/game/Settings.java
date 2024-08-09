package tankwargame.game;

import tankwargame.Launcher;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Settings {
    private Launcher launcher;
    private JButton settingsButton;
    private JPopupMenu settingsMenu;

    public Settings(Launcher launcher) {
        this.launcher = launcher;
        setupSettingsButton();
    }

    private void setupSettingsButton() {
        BufferedImage menuIconImg = null;
        BufferedImage wallImg = null;
        try {
            URL menuIconURL = getClass().getClassLoader().getResource("menuicon.jpg");
            if (menuIconURL != null) {
                menuIconImg = ImageIO.read(menuIconURL);
                System.out.println("menuicon.jpg loaded successfully from: " + menuIconURL.getPath());
            } else {
                System.out.println("menuicon.jpg not found in resources");
            }

            URL wallURL = getClass().getClassLoader().getResource("wall2.png");
            if (wallURL != null) {
                wallImg = ImageIO.read(wallURL);
                System.out.println("wall2.png loaded successfully from: " + wallURL.getPath());
            } else {
                System.out.println("wall2.png not found in resources");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (menuIconImg != null && wallImg != null) {
            // Scale the image to 7/9th the size of the unbreakable wall
            int wallSize = wallImg.getWidth(); // Assuming the wall image width is the thickness of the wall
            int buttonSize = (7 * wallSize) / 9;
            ImageIcon icon = new ImageIcon(menuIconImg.getScaledInstance(buttonSize, buttonSize, BufferedImage.SCALE_SMOOTH));
            // Create a button with the menu icon
            settingsButton = new JButton(icon);
            settingsButton.setSize(buttonSize, buttonSize); // Adjust size as needed
            settingsButton.setContentAreaFilled(false);
            settingsButton.setBorderPainted(false);
            settingsButton.setFocusPainted(false);

            // Create the popup menu
            settingsMenu = new JPopupMenu();
            settingsMenu.setBackground(Color.GREEN); // Set the background color to green

            JMenuItem restartItem = new JMenuItem("Restart");
            restartItem.setBackground(Color.GREEN); // Set the background color to green
            restartItem.addActionListener(e -> launcher.restartGame());

            JMenuItem quitItem = new JMenuItem("Exit Game");
            quitItem.setBackground(Color.GREEN); // Set the background color to green
            quitItem.addActionListener(e -> launcher.showMainMenu());

            settingsMenu.add(restartItem);
            settingsMenu.add(quitItem);

            // Add mouse listener to the button to show the popup menu
            settingsButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    settingsMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            });
        } else {
            // Fallback in case the image fails to load
            settingsButton = new JButton("Settings");
            settingsButton.setSize(100, 50);
            settingsButton.addActionListener(e -> {
                JPopupMenu fallbackMenu = new JPopupMenu();
                fallbackMenu.setBackground(Color.GREEN); // Set the background color to green

                JMenuItem restartItem = new JMenuItem("Restart");
                restartItem.setBackground(Color.GREEN); // Set the background color to green
                restartItem.addActionListener(ev -> launcher.restartGame());

                JMenuItem quitItem = new JMenuItem("Quit to Main Menu");
                quitItem.setBackground(Color.GREEN); // Set the background color to green
                quitItem.addActionListener(ev -> launcher.showMainMenu());

                fallbackMenu.add(restartItem);
                fallbackMenu.add(quitItem);
                fallbackMenu.show(settingsButton, settingsButton.getWidth() / 2, settingsButton.getHeight() / 2);
            });
        }
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }
}
