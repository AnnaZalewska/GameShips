/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Anna Zalewska
 */
public class Board extends JPanel {

    private int x; // pozycja x na planszy
    private int y; // pozycja y na planszy
    private int w;   // szerokośc panelu
    private int h;   // wysokość panelu
    private Field[][] field;   // pola do gry
    private Dimension size;   // rozmiar plnaszy
    private Define define;   // stałe w programie
    private boolean good;
    private GameManager game;
    private LastComputerShut last;
    static int numberLuck;

    public Board(int x, int y, int w, int h, String name, GameManager t) {
        this.game = t;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.size = new Dimension(w, h);
        this.good = false;
        this.last = new LastComputerShut();

        this.setName(name);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setBounds(x, y, w, h);
        this.setLayout(null);
        this.setVisible(true);

    }

    /**
     * drukowanie planszy na ekranie
     */
    public void printBoard(int numberBoard) {
        this.define = game.getDefine();
        field = new Field[10][10];
        int k = 0, p = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = new Field("images/pole.png", p, k, 25, 30, i, j, game, numberBoard);
                field[i][j].createDefine(define);
                add(field[i][j]);
                p += 25;

            }
            k += 30;
            p = 0;
        }
    }

    /**
     * Ustawianie statków na planszy
     *
     * @param max - maksymalna ilośc pól do przeznaczenia na statki
     */
    public void insertShips(int max) {
        InsertShip ships = new InsertShip(max);

        ships.start();

    }

    /**
     * Zmienia akcje na polach planszy. Możliwe są trzy akcje: PAUSE - brak
     * rozgrywki, nic nie można z danym polem zrobić EDIT - ustawianie statków
     * na planszy GAME - granie w statki
     *
     * @param action - przechwouje jeden z powyżych typów
     */
    public void setAction(ClikInButton action) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j].setAction(action);
                field[i][j].updateListBoard(field);

            }
        }
    }

    /**
     * Funkcja ustawia losowo statki na planszy. W rozgryce gracz kontra
     * komputer. Działa jako komupter
     */
    public void RandomInsertShip() {

        int[] Ships = define.createArrayShips();
        int number = 0;
        int[] position = new int[2];

        while (number < define.NUMBERSHIP) {

            position = this.goodField();
            int i = position[0];
            int j = position[1];


            Direction dircetion = Direction.randomDirection();

            if (dircetion == Direction.UPRIGHT) {

                if (i >= Ships[number]) {

                    for (int k = i - 1; k > i - Ships[number] && k >= 0; k--) {
                        if (!(field[k][j].isPossibleInsertShip(k, j))) {

                            position = this.goodField();
                            i = position[0];
                            j = position[1];
                            k = i - 1;
                        }
                    }

                    for (int k = i; k > i - Ships[number] && k >= 0; k--) {
                        field[k][j].setFiled(number + 1);
                        field[k][j].updateListBoard(field);
                    }
                } else {

                    for (int k = i + 1; k < i - Ships[number] && k <= 9; k++) {

                        if (!(field[k][j].isPossibleInsertShip(k, j))) {

                            position = this.goodField();
                            i = position[0];
                            j = position[1];
                            k = j + 1;
                        }
                    }
                    for (int k = i; k < i + Ships[number]; k++) {
                        field[k][j].setFiled(number + 1);
                        field[k][j].updateListBoard(field);
                    }
                }
            } else {

                if (j >= Ships[number]) {

                    for (int k = j - 1; k > j - Ships[number] && k >= 0; k--) {
                        if (!(field[i][k].isPossibleInsertShip(i, k))) {

                            position = this.goodField();
                            i = position[0];
                            j = position[1];
                            k = j + 1;
                        }
                    }

                    for (int k = j; k > j - Ships[number] && k >= 0; k--) {
                       
                        field[i][k].setFiled(number + 1);
                        field[i][k].updateListBoard(field);
                    }


                } else {

                    for (int k = j + 1; k < j + Ships[number] && k <= 9; k++) {
                        if (!(field[i][k].isPossibleInsertShip(i, k))) {

                            position = this.goodField();
                            i = position[0];
                            j = position[1];
                            k = j + 1;

                        }
                    }
                    for (int k = j; k < j + Ships[number] && k <= 9; k++) {
                        field[i][k].setFiled(number + 1);
                        field[i][k].updateListBoard(field);
                    }
                }

            }
            number++;
        }
    }

    /**
     * Funkcja losuje pola do ustawiania statków przez komupter. Losowanie trwa
     * do mommentu znalezienia pustego pola, na które można postawić statek.
     *
     * @return pozycję pola
     */
    public int[] goodField() {
        Random random = new Random();
        int[] tabel = new int[2];
        do {
            tabel[0] = random.nextInt(10);
            tabel[1] = random.nextInt(10);
        } while (!field[tabel[0]][tabel[1]].isPossibleInsertShip(tabel[0], tabel[1]));
        return tabel;
    }

    /**
     * Funkcja zapisuje do tablicy intów wartość pól. Gdzie są statki i jakie, a
     * gdzie puste pola.
     *
     * @return int[][] talica dwuwymiaorwa intów
     */
    public int[][] seveShips() {
        int[][] table;
        table = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                table[i][j] = field[i][j].getNumberShip();
            }
        }
        return table;
    }

    /**
     * Funkcja pobraną tablicę ze statkim zapisuje do wybranej plnaszy
     * @param array 
     */
    public void downloadShips(int[][] array) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j].setNumberShip(array[i][j]);
            }
        }

    }

    /**
     * Funkcja pokazuje ukryte statki
     */
    public void show() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j].showFiled();
            }
        }
    }

    public boolean getGood() {
        return good;
    }

    public boolean checkField(int i, int j) {
        if (field[i][j].isWasShut()) {
            return true;
        } else {
            return false;
        }
    }

    public void shutt(int i, int j) {

        int numberShip;
        if (game.getGame() == TypeGame.PC) {
            if (last.getType() == TypeField.HIT) {
                ShutInShip(last.getPosistionI(), last.getPosistionJ());

            } else {
                field[i][j].shut(Who.COMPUTER);
                last.setAll(i, j, field[i][j].getType(), field[i][j].getNumberShip());
            }
        } else {
            field[i][j].shut(null);
        }
        repaint();
    }

    public void ShutInShip(int i, int j) {
        int k, t; // pomocnicze zmienne do określenia czy i oraz j nie są skrajną pozycją tablicy
        int maxK, maxT; //pomocnicze zmienne do ogranicze pętli szukającej statków na danym polu 
        boolean isHit = false; // przechowuje informację czy statek został trafiony

        Random r = new Random();  // losuje 
        /*
         * pionowo
         */

        if (i != 0) {
            k = i - 1;
        } else {
            k = i;
        }


        if (i != 9) {
            maxK = i + 1;
        } else {
            maxK = i;
        }

        /*
         * poziomo
         */

        if (j != 0) {
            t = j - 1;
        } else {
            t = j;
        }

        if (j != 9) {
            maxT = j + 1;
        } else {
            maxT = j;
        }

        /*
         * algorytm strzelania
         */
        int luck;
        int min = k, max = maxK;
        int points, points2;

        if (this.isHitTwoField(field[i][j].getNumberShip()) || numberLuck >= 4) {
            luck = 1;
        } else {
            luck = r.nextInt(1);
        }
        do {
            points = r.nextInt(max - min + 1) + min;
            min = t;
            max = maxT;
            points2 = r.nextInt(max - min + 1) + min;
        } while (field[points][points2].isWasShut());
        if (luck == 1) {
            for (int shutI = k; shutI <= maxK; shutI++) {
                for (int shutJ = t; shutJ <= maxT; shutJ++) {
                    if (field[shutI][shutJ].getType() == TypeField.SHIP) {
                        field[shutI][shutJ].shut(Who.COMPUTER);
                        last.setAll(shutI, shutJ, field[shutI][shutJ].getType(), field[shutI][shutJ].getNumberShip());
                        isHit = true;
                        break;
                    }
                }
            }

            if (!isHit) {
                if (last.getNumber() > 0 && last.getType() != TypeField.SUNK) {

                    int[] position = this.searchShip(last.getNumber());
                    if (position[0] != 11 && position[1] != 11) {
                        field[position[0]][position[1]].shut(Who.COMPUTER);
                        last.setAll(position[0], position[1], field[position[0]][position[1]].getType(), field[position[0]][position[0]].getNumberShip());
                    }

                } else {
                    field[i][j].shut(Who.COMPUTER);
                    last.setAll(i, j, field[i][j].getType(), field[i][j].getNumberShip());
                }
            }
        } else {
            numberLuck++;
            field[points][points2].shut(Who.COMPUTER);
        }
        isHit = false;
    }

    public boolean isHitTwoField(int ship) {
        int number = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field[i][j].getType() == TypeField.HIT && field[i][j].getNumberShip() == ship) {
                    number++;
                }
                if (number >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[] searchShip(int numberShip) {
        int[] shut = new int[2];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field[i][j].getNumberShip() == numberShip && (field[i][j].getType() == TypeField.SHIP)) {
                    shut[0] = i;
                    shut[1] = j;
                    return shut;
                }
            }
        }
        shut[0] = 11;
        shut[1] = 11;
        return shut;
    }
/**
 * Funkcja zmienia akcje na wszystkich polach plnaszy
 * @param type 
 */
    public void setAllField(TypeField type) {
        String image = " ";
        int ship = 0;
        switch (type) {
            case EMPTY:
                image = "images/pole.png";
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j].setType(type);
                field[i][j].setName(image);
                field[i][j].setNumberShip(ship);
            }
        }
        repaint();
    }
    
    /*
     * Klasa pozwala wstawiać statki użyykownikowi
     */

    class InsertShip extends Thread {

        private int max;
        private int numberShip;
        private boolean setNumberShip;
        private int[] Ship;

        public InsertShip(int max) {
            this.max = max;
            this.numberShip = 1;
            this.setNumberShip = true;

            Ship = new int[define.NUMBERSHIP];
            Ship = define.createArrayShips();


        }

        public void run() {
            int number = 0;
            int OneShipNumber = 0;
            int k = 0;
            while (number != max) {


                number = 0;
                OneShipNumber = 0;
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {

                        if (setNumberShip) {
                            field[i][j].setTemp(numberShip);
                        }

                        if (field[i][j].getType() == TypeField.SHIP) {
                            number++;
                            if (field[i][j].getNumberShip() == numberShip) {
                                OneShipNumber++;


                            }
                        }
                    }
                }
                setNumberShip = false;

                if (OneShipNumber == Ship[k]) {


                    if (k != 4) {
                        if (k == 2) {
                            game.setTextArea(203, Ship[k + 1]);
                        } else {
                            game.setTextArea(202, Ship[k + 1]);
                        }
                        define.lastNumberShip = Ship[k + 1];
                        
                    }

                    numberShip++;
                    setNumberShip = true;
                    k++;

                }
                // włączenie opcji umieszczania statków na każdym polu
                if (number < max) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            field[i][j].setAction(ClikInButton.EDIT);
                            field[i][j].updateListBoard(field);

                        }
                    }

                }

                // wyłączenie umieszczania statków 
                if (number >= max) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            field[i][j].setAction(ClikInButton.PAUSE);
                        }

                    }
                }
            }
            game.enableButton(game.getButton("START"), true, 204);
        }
    }
}

/**
 * Klasa do zapisania ostatniego strzału komputera, przed aktulanym strzałem
 * @author Ania
 */
class LastComputerShut {

    private int i;
    private int j;
    private TypeField type;
    private int numberShip;

    public LastComputerShut() {
        this.i = 11;
        this.j = 11;
        this.type = null;
        this.numberShip = 0;
    }

    public void setAll(int k, int t, TypeField _type, int ship) {
        i = k;
        j = t;
        type = _type;
        numberShip = ship;
    }

    public int getPosistionI() {
        return i;
    }

    public int getPosistionJ() {
        return j;
    }

    public TypeField getType() {
        return type;
    }

    public int getNumber() {
        return numberShip;
    }
}
