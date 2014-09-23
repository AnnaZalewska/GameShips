/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import gameships.Board;
import gameships.*;
import gameships.Define;
import gameships.GameManager;
import java.awt.TextArea;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anna Zalewska
 */
public class Server extends Thread {

    ServerSocket socket;    // gniazdo połączenia
    Socket connection;      // połączenie
    BufferedReader in;      // strumień wyjściowy
    PrintWriter out;        // tekst do wysłania
    int port;               // PORT na którym server nasłuchuje
    GameManager message;    // wysyłanie wiadomosc na ekran
    String myName;          // przechowuje nazwę planszy do wysłania
    String secondName;      // przechowuję odebraną nazwę planszy
    BufferedReader in2;      // strumień wyjściowy
    ObjectOutputStream out2;        // tekst do wysłania
    Define define;
    boolean takeArrays;

    public Server(int port, GameManager message) {

        this.socket = null;

        this.connection = null;

        this.out = null;

        this.in = null;

        this.port = port;

        this.message = message;

        this.define = message.getDefine();

        this.takeArrays = false;


    }

    public void run() {

        // utworzenie gniazda serwera
        try {
            socket = new ServerSocket(port);
            message.setTextArea(100, 0);   // wiadomość: utworzono gniazdo serwera

        } catch (IOException e) {

            message.setTextArea(101, 0);   // wiadomość: Nie można utworzyć gniazda 
        }

// łączenie z klientem (z drugim graczem)
        try {
            connection = socket.accept();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            message.setTextArea(102, 0);  // wiadomość: połączono z drugim graczem
            message.enableButton(message.getButton("INSERT"), true, 150);

        } catch (IOException e) {

            message.setTextArea(103, 0);  // wiadomość: nie można połączyć z drugim graczem


        }

// wysłanie swojej nazwy do klienta (drugiego gracza)
        try {
            if (in != null) {

                out = new PrintWriter(connection.getOutputStream(), true);
            }
        } catch (Exception e) {

            message.setTextArea(105, 0);  // wiadomość: Nie moża utworzyć strumienia PrintWriter

        }

        try {

            this.myName = message.getMyname();

            if (out != null) {

                out.println(myName);
            }
        } catch (Exception e) {

            message.setTextArea(106, 0);  //wiadomość: Nie moża wysłać nazwy 

        }

// odebranie jego nazwy od klienta (drugiego gracza) 
        try {
            if (in != null) {

                secondName = in.readLine();
                message.setSecondName(secondName);
            }
        } catch (IOException e) {
            message.setTextArea(153, 0);  // wiadomość: nie można odczytać nazwy od drugiego gracza
        }
// Wysłanie tablicy z pozycją statków na planszy
        while (message.getWyslij()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }

        try {
            int[][] array = message.downloadArray();
            SerialArray serial = new SerialArray();
            serial.setArray(array);
            out2 = new ObjectOutputStream(connection.getOutputStream());

            if (out2 != null) {
                out2.writeObject(serial);
            }
            if (takeArrays) {
                message.setTextArea(300, 0);
            } else {
                message.setTextArea(112, 0);
            }




        } catch (Exception e) {
            message.setTextArea(106, 0);

        }
        try {
            // odebranie tablicy
            ObjectInputStream in2 = new ObjectInputStream(connection.getInputStream());
            SerialArray serial = new SerialArray();
            try {

                serial = (SerialArray) in2.readObject();
                int[][] array = serial.getArray();
                message.seveBoard(array);
                message.setTextArea(111, 0);
                takeArrays = true;
                Thread.sleep(3000);
                message.setTextArea(300, define.lastNumberShip);
                //   message.setActionBoard("secondBoard", ClikInButton.GAME);
              //  System.out.println(array[0][0]);

            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            message.reset(156);
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        while (!define.gameOver) {

            // strzał
            while (!define.sendPosition) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }

            try {
                int[] shut = message.getActualyShut();
                SerialShut serial2 = new SerialShut();
                serial2.setArray(shut);
                out2 = new ObjectOutputStream(connection.getOutputStream());

                if (out2 != null) {
                    out2.writeObject(serial2);
                    define.sendPosition = false;

                }


            } catch (Exception e) {
                message.setTextArea(106, 0);

            }

            try {
                // odebranie tablicy
                ObjectInputStream in3 = new ObjectInputStream(connection.getInputStream());
                SerialShut serial2 = new SerialShut();
                int[] shut;


                try {
                    if (in3 != null) {

                        serial2 = (SerialShut) in3.readObject();
                        shut = serial2.getArray();

                        message.shutSecendPlayer(shut[0], shut[1]);
                        define.setMotion(1);
                        message.setTextArea(300, 0);

                    }
                } catch (ClassNotFoundException ex) {
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                message.reset(156);
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }


        }


        try {
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            message.setTextArea(155, 0);

        }
    }
}
// serializacja tablicy 10 x 10, całej planszy
// serializacja strzału. tablica z pozycją i oraz j danego pola
class SerialArray implements Serializable {

    private int[][] tabel;

    public SerialArray() {
    }

    public void setArray(int array[][]) {
        this.tabel = array;
    }

    public int[][] getArray() {
        return tabel;
    }
}
// serializacja strzału. tablica z pozycją i oraz j danego pola
class SerialShut implements Serializable {

    private int[] tabel;

    public SerialShut() {
    }

    public void setArray(int array[]) {
        this.tabel = array;
    }

    public int[] getArray() {
        return tabel;
    }
}