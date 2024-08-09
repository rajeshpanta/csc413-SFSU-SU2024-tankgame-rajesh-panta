package tankwargame.menus;

import tankwargame.Launcher;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("title.png"));
        } catch (IOException e) {
            System.out.println("Error: Cannot read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton restart = new JButton("Restart Game");
        restart.setFont(new Font("Courier New", Font.BOLD, 24));
        restart.setBounds(150, 300, 250, 50);
        restart.addActionListener(actionEvent -> this.lf.setFrame("game"));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 400, 250, 50);
        exit.addActionListener(actionEvent -> {
            this.lf.closeGame();
            System.exit(0); // Terminate the application completely
        });

        this.add(restart);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(menuBackground, 0, 0, null);
    }
}
