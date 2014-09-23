/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Anna Zalewska
 * Klasa do wczytywania obrazków z pliku do gry
 */
public class Background extends Board {

    private String name;   // nazwa ewentualnej grafiki 

    public Background(String name, int x, int y, int w, int h) {
        super(x, y, w, h, "", null);
        this.setLayout(null);
        this.name = name;

        this.setBounds(x, y, w, h);
        this.setVisible(true);

    }

    public BufferedImage loadImage(String sciezka) {
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource(sciezka);
            return ImageIO.read(url);
        } catch (Exception e) {
            System.out.println("Przy otwieraniu " + sciezka + " jako " + url);
            System.out.println("Wystapil blad : " + e.getClass().getName() + "" + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public void paint(Graphics g) {
        BufferedImage background = loadImage(name);
        g.drawImage(background, 0, 0, this);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }

    public void setName(String name) {
        this.name = name;
    }
}