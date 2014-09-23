/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Anna Zalewska
 */
public class Window extends JFrame {

    private int WIDTH;
    private int HEIGHT;
    private Background background;
    private GameManager gameBoard;
    private MenuManager menu;
    private Background title;
    private Background ship1;
    private JLabel esc;
    private int a;

    public Window(int w, int h) {
        super("Statki");

        this.WIDTH = w;

        this.HEIGHT = h;

        this.background = new Background("images/morze.png", 0, 0, WIDTH, HEIGHT);

        this.gameBoard = new GameManager(WIDTH, HEIGHT);

        this.title = new Background("images/tytuł.png", 170, 18, 600, 200);

        this.ship1 = new Background("images/f-handl-bez.png", 505, 120, 300, 419);

        this.menu = new MenuManager(275, 150, 200, 220);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);

        this.setSize(WIDTH, HEIGHT);

        this.setLocationRelativeTo(null);  // ustawia okno na środku ekranu

        this.setResizable(false);

        this.setVisible(true);
    }

    public void ActionMenu() {

        menu.addAllButtons();
        esc = creatESC(esc);

        menu.setGameManager(gameBoard, title, ship1, esc);
        getContentPane().add(ship1);
        getContentPane().add(title);
        getContentPane().add(esc);
        getContentPane().add(menu).setVisible(true);
        getContentPane().add(gameBoard).setVisible(false);

        getContentPane().add(background);
    }

    public JLabel creatESC(JLabel label) {

        label = new JLabel();
        label.setText("Wciśnij ESC aby wrócić do menu");
        label.setBounds(600, 440, 200, 30); // 200, 30
        label.setForeground(new Color(246, 80, 80));
        Font f = label.getFont();
        label.setFont(new Font(f.getFontName(), f.getStyle(), 12));
        this.add(label);
        label.setVisible(false);
        return label;
    }
}

enum ActionButton {

    NEWGAME, SAVE, HELP,
    ABOUT, EXIT, OTH;
}
