/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.util.Random;

/**
 *
 * @author Anna Zalewska
 */
public class Define {
    // łączenie przz sieć:

    public int PORT = 7799;
    public String HOST = "localhost";
    // gra
    public int NUMBERSHIP = 5;
    public int[] typesOfShips = new int[NUMBERSHIP];
    public int maxFiledShip = 17;
    public int motion = 1;
    public boolean sendPosition = false;
    public Who who = Who.UNKNOWN;
    public boolean gameOver = false;
    public static int myShips = 0;
    public static int secondShips = 0;
    public int lastNumberShip = 5;

    public Define() {
    }

    public int[] createArrayShips() {
        typesOfShips[0] = 5;
        typesOfShips[1] = 4;
        typesOfShips[2] = 3;
        typesOfShips[3] = 3;
        typesOfShips[4] = 2;

        return typesOfShips;
    }

    public void setMotion(int m) {
        this.motion = m;
    }

    public int getMotion() {
        return this.motion;
    }
}

enum ClikInButton {

    EDIT, GAME, PAUSE
}

enum TypeField {

    EMPTY, SHIP, HIT, SUNK
}

enum TypeGame {

    NETWORK, PC
}

enum Direction {

    UPRIGHT(0), HORIZONTALLY(1);

    Direction(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static Direction randomDirection() {
        Direction direction;
        Random random = new Random();
        int number = random.nextInt(2);
        switch (number) {
            case 0:
                direction = Direction.UPRIGHT;
                return direction;
            case 1:
                direction = Direction.HORIZONTALLY;
                return direction;
            default:
                direction = Direction.randomDirection();
                return direction;
        }

    }
    private int type;
}