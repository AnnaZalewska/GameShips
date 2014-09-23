/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.Toolkit;
import javax.swing.*;
import network.Server;
import network.Client;

/**
 *
 * @author Anna Zalewska
 */
public class GameShips {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Window okno = new Window(800, 500);

                okno.ActionMenu();
                okno.setIconImage(Toolkit.getDefaultToolkit().getImage("images/pole_statek.png"));

            }
        });
    }
}
