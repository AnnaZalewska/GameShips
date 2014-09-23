/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Anna Zalewska
 */
public class MenuManager extends JComponent implements KeyListener {

    private JButton NewGame;
    private JButton help;
    private JButton about;
    private JButton exit;
    private int type;
    private GameManager gameBoard;
    private Background title;
    private Background ship1;
    private ButtonGroup group;
    private Action action;
    private JPanel menuPanel;
    private Background next;
    private Background previous;
    private JLabel esc;

    public MenuManager(int x, int y, int width, int height) {

        this.gameBoard = null;

        this.setBounds(x, y, width, height);

        this.setPreferredSize(new Dimension(width, height));

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setFocusable(true);

        this.type = 0;

    }

    public static JButton addOneButton(String text, JComponent panel, int x, int y) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(x, y));// 120 30
        button.setMinimumSize(new Dimension(x, y));
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        button.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JComponent the_vertical_distance = new MenuManager(0, 0, 0, 70);
        panel.add(button);
        panel.add(the_vertical_distance);
        return button;
    }

    public void addAllButtons() {


        this.NewGame = this.addOneButton("Nowa gra", this, 120, 30);
        this.help = addOneButton("Instrukcja", this, 120, 30);
        this.about = addOneButton("Twórcy", this, 120, 30);
        this.exit = addOneButton("Wyjście", this, 120, 30);


        NewGame.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NewGameMouseClicked(evt);
            }
        });
        exit.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitMouseClicked(evt);
            }
        });
        help.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HelpMouseClicked(evt);
            }
        });
        about.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AboutMouseClicked(evt);
            }
        });
    }

    private void NewGameMouseClicked(java.awt.event.MouseEvent evt) {
        gameBoard.play();
        this.setVisible(false);
        title.setVisible(false);
        ship1.setVisible(false);
        gameBoard.setVisible(true);
        type = 1;
    }

    private void HelpMouseClicked(MouseEvent evt) {
        menuPanel = this.createPanel(1, 1);
        this.setVisibleButtons(false);
        addKeyListener(this);

    }

    private void AboutMouseClicked(MouseEvent evt) {
        menuPanel = this.createPanel(2, 1);
        this.setVisibleButtons(false);
        addKeyListener(this);
    }

    private void ExitMouseClicked(java.awt.event.MouseEvent evt) {
        System.exit(0);

    }

    public int getType() {
        return this.type;
    }

    public void setGameManager(GameManager gameBoard, Background title, Background ship1, JLabel esc) {
        this.gameBoard = gameBoard;
        this.title = title;
        this.ship1 = ship1;
        this.esc = esc;

    }

    public JPanel createPanel(int num, final int co) {
        String image = "";
        esc.setVisible(true);
        if (num == 1) {
            image = this.numberOfInstructions(co);
        } else if (num == 2) {
            image = "images/tworca.png";
        }
        final Background panel = new Background(image, 0, 0, 200, 400);
        this.setLayout(null);
        panel.setBounds(0, 0, 200, 400);
        panel.setLayout(null);
        if (num == 1) {

            next = new Background("images/strzalka_prawo.png", 171, 172, 40, 40);
            previous = new Background("images/strzalka_lewo.png", 148, 172, 40, 40);
            if (co == 4) {
                next.setVisible(false);
            } else {
                next.setVisible(true);
            }

            if (co == 1) {
                previous.setVisible(false);
            } else {
                previous.setVisible(true);
            }
            this.add(next);
            this.add(previous);
            next.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    sseMouseClicked(evt);
                }

                private void sseMouseClicked(MouseEvent evt) {

                    menuPanel.setVisible(false);
                    next.setVisible(false);
                    previous.setVisible(false);
                    menuPanel = createPanel(1, co + 1);
                }
            });
            previous.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    PreviousMouseClicked(evt);
                }

                private void PreviousMouseClicked(MouseEvent evt) {

                    menuPanel.setVisible(false);
                    next.setVisible(false);
                    previous.setVisible(false);
                    menuPanel = createPanel(1, co - 1);
                }
            });
        }



        panel.setVisible(true);

        this.add(panel);
        this.repaint();
        return panel;
    }

    public String numberOfInstructions(int num) {
        switch (num) {
            case 1:
                return "images/instrukcja1.png";
            case 2:
                return "images/instrukcja2.png";
            case 3:
                return "images/instrukcja3.png";
            case 4:
                return "images/instrukcja4.png";

        }
        return "images/instrukcja1.png";
    }

    public void setVisibleButtons(boolean b) {

        this.about.setVisible(b);
        this.exit.setVisible(b);
        this.help.setVisible(b);
        this.NewGame.setVisible(b);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == e.VK_ESCAPE) {

            menuPanel.setVisible(false);
            next.setVisible(false);
            previous.setVisible(false);
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            this.setVisibleButtons(true);
            esc.setVisible(false);

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
}

class NewGameAction extends AbstractAction {

    private GameManager gameBoard;
    private Background title;
    private Background ship;
    private MenuManager menu;

    public NewGameAction(String name, GameManager gameBoard, Background title, Background ship, MenuManager menu) {
        super(name);
        this.gameBoard = gameBoard;
        this.title = title;
        this.ship = ship;
        this.menu = menu;
    }

    public void actionPerformed(ActionEvent e) {
        gameBoard.play();
        menu.setVisible(false);
        title.setVisible(false);
        ship.setVisible(false);
        gameBoard.setVisible(true);
    }
}
