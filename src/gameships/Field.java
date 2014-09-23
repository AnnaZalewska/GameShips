/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anna Zalewska
 */
public class Field extends Background {

    private TypeField type;     // typ pola: EMPTY - puste pole, SHIP- statek, SUNK - zatopiony statek, HIT - trafiony statek
    private ClikInButton action;  // akcja na polu: edycja - ustawianie statkow, gra- możliwość strzelania, pauza
    private int ship;      // numer statku, do którego należy pole
    private int temp;    //zmienna pomocnicza do nadania numeru statku
    Field[][] board;    // tablica statków
    private int[] posistion; // pozycja pionowa "i" pola na planszy
    //private int positionJ;  // pozycja poziomoa "j" pola na planszy
    private Define define;
    private GameManager game;
    private boolean sendPosition;
    private int numberBoard;
    private boolean hit; // czy w dane pole został oddany strzał

    public Field(String name, int x, int y, int w, int h, int i, int j, GameManager game, int numberBoard) {
        super(name, x, y, w, h);

        this.type = TypeField.EMPTY;

        this.action = ClikInButton.PAUSE;

        this.ship = 0;

        this.posistion = new int[2];

        this.posistion[0] = i;

        this.posistion[1] = j;

        this.game = game;

        this.define = game.getDefine();

        this.sendPosition = false;

        this.numberBoard = numberBoard;

        this.hit = false;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FiledMouseClicked(evt);
            }
        });
    }

    public TypeField getType() {
        return type;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setFiled(int number) {
        this.ship = number;
        type = TypeField.SHIP;
    }

    public void setType(TypeField type) {
        this.type = type;
    }

    public void setAction(ClikInButton new_action) {
        this.action = new_action;
    }

    public int getNumberShip() {
        return ship;
    }

    public void setNumberShip(int s) {
        this.ship = s;
        if (ship != 0) {
            this.type = TypeField.SHIP;
        }
    }

    public void createDefine(Define d) {
        this.define = d;
    }

    private void FiledMouseClicked(MouseEvent evt) {
        if (action == ClikInButton.EDIT) {
            boolean is = this.isPossibleInsertShip(posistion[0], posistion[1]);
            if (is) {
                type = TypeField.SHIP;
                ship = temp;
                setName("images/pole_statek.png");

                if (game.getNumberLastMessage() == 306 || game.getNumberLastMessage() == 308) {
                    game.setTextArea(202, define.lastNumberShip);
                }
            }
        } else if (action == ClikInButton.GAME && define.getMotion() == 1) {
            define.setMotion(0);
            game.setTextArea(301, 0);
            if (type == TypeField.EMPTY) {
                setName("images/puste_pole.png");
                hit = true;
            } else if (type == TypeField.SHIP) {

                this.shut(null);
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                repaint();

            } else if (type == TypeField.SUNK) {
                setName("images/pole_zatopiony.png");
                hit = true;
            }

            if (game.getGame() == TypeGame.PC) {

                game.shutSecendPlayer(0, 0);
            } else if (game.getGame() == TypeGame.NETWORK) {
                define.sendPosition = true;
                hit = true;
                game.getPositionShipAndSendNetwork(posistion);
            }

        } else if (action == ClikInButton.PAUSE) {
        } else if (define.getMotion() == 0) {
            game.setTextArea(307, 0);
        }
        repaint();
    }
    /*
     * Funkcja sprawdzająca czy na danym polu można ustwić statek
     */

    public boolean isPossibleInsertShip(int k, int f) {
        int tempI, tempJ;  // zmienne do forów
        int maxI, maxJ; // ile pól ma być sprawdznych. wielkosć: maxI x maxJ

        // sprawdzenie czy statek ustawiany jest na skraju planszy

        // pionowo
        if (k == 0) {
            tempI = k;
        } else {
            tempI = k - 1;
        }


        if (k == 9) {
            maxI = k;
        } else {
            maxI = k + 1;
        }



        //poziomo
        if (f == 0) {
            tempJ = f;
        } else {
            tempJ = f - 1;
        }

        if (f == 9) {
            maxJ = f;
        } else {
            maxJ = f + 1;
        }



        for (int i = tempI; i <= maxI; i++) {
            for (int j = tempJ; j <= maxJ; j++) {

                if (board[i][j].getNumberShip() != 0) {

                    if (board[i][j].getNumberShip() != this.temp) {

                        game.setTextArea(306, 0);
                        return false;

                    } else if (board[i][j].getNumberShip() == this.temp
                            && ((i != 0 && board[i - 1][j].getNumberShip() == this.temp)
                            || (i != 9 && board[i + 1][j].getNumberShip() == this.temp))) {

                        if (j - 1 == f || j + 1 == f) {

                            game.setTextArea(306, 0);
                            return false;
                        }


                    } else if (board[i][j].getNumberShip() == this.temp
                            && ((j != 0 && board[i][j - 1].getNumberShip() == this.temp)
                            || (j != 9 && board[i][j + 1].getNumberShip() == this.temp))) {

                        if (i - 1 == k || i + 1 == k) {

                            game.setTextArea(306, 0);
                            return false;
                        }
                    }


                } else if (((k != 0 && board[k - 1][f].getNumberShip() != this.temp) || k == 0)
                        && ((k != 9 && board[k + 1][f].getNumberShip() != this.temp) || k == 9)
                        && ((f != 0 && board[k][f - 1].getNumberShip() != this.temp) || f == 0)
                        && ((f != 9 && board[k][f + 1].getNumberShip() != this.temp) || f == 9)) {

                    boolean is = this.isThisShip();
                    if (is) {

                        game.setTextArea(308, 0); // Pola w statku muszą być klikane po koleji, jeden obok drugiego
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void updateListBoard(Field[][] board) {
        this.board = board;
    }

    public boolean isThisShip() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j].getNumberShip() == this.temp) {
                    return true;
                }
            }
        }
        return false;
    }

    public void showFiled() {

        if (this.getType() == TypeField.SHIP) {
            setName("images/pole_statek.png");
        }
    }

    public int getShip() {
        return this.ship;
    }

    public boolean isWasShut() {
        return hit;
    }

    public boolean isSunk(int numberS) {
     //   System.out.println(numberS);
        int[] Ship = new int[define.NUMBERSHIP];
        Ship = define.createArrayShips();

        int number = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j].getType() == TypeField.HIT && board[i][j].getShip() == numberS) {
                    number++;
                }
            }
        }
        if (number == Ship[numberS - 1]) {
            if (numberBoard == 1) {
                define.myShips++;
            } else {
                define.secondShips++;
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (board[i][j].getShip() == numberS) {
                        board[i][j].setName("images/pole_zatopiony.png");
                        board[i][j].type = TypeField.SUNK;

                        board[i][j].repaint();

                    }
                }
                repaint();

            }
            this.game.setTextArea(302, 0);
            return true;
        }
        return false;
    }

    public TypeField shut(Who who) {
        if (type == TypeField.SHIP) {
            setName("images/pole_trafiony.png");
            type = TypeField.HIT;


            this.isSunk(ship);
        } else if (type == TypeField.EMPTY) {
            setName("images/puste_pole.png");


            this.repaint();
        }

        game.setTextArea(300, 0);
        if (isGameOver()) {

            game.showWhoWin(numberBoard);
        }

        hit = true;
        return type;
    }

    public boolean isGameOver() {
        if (define.myShips == define.NUMBERSHIP || define.secondShips == define.NUMBERSHIP) {

            define.gameOver = true;
            return true;
        } else {
            return false;
        }
    }
}
