/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import gameships.Define;
import gameships.GameManager;
import java.io.*;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anna Zalewska
 */
public class Client extends Thread {

    Socket socket;      // połączenie
    BufferedReader in;      // strumień wyjściowy
    PrintWriter out;        // tekst do wysłania
    int port;               // PORT na którym server nasłuchuje
    String host;
    GameManager message;    // wysyłanie wiadomosc na ekran
    String sendMyName;          // przechowuje nazwę planszy do wysłania
    String readSecondName;      // przechowuję odebraną nazwę planszy
    BufferedReader in2;      // strumień wyjściowy
    ObjectOutputStream out2;        // tekst do wysłania
    Define define;                  // klasa ze stałymi
    boolean takeArrays;           // czy tablica oderana 

    public Client(String host, int port, GameManager message) {

        this.socket = null;

        this.out = null;

        this.in = null;

        this.host = host;

        this.port = port;

        this.message = message;

        this.define = message.getDefine();

        this.takeArrays = false;

    }

    public void run() {

        try {

            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            message.setTextArea(150, 0);

            message.enableButton(message.getButton("INSERT"), true, 150);

        } catch (UnknownHostException e) {


            message.reset(151);
        } catch (IOException e) {


            message.reset(152);
        }

// odebranie nazwy drugiego gracza
        try {

            if (in != null) {

                readSecondName = in.readLine();
                message.setSecondName(readSecondName);
                in = null;
            }
        } catch (IOException e) {
            message.setTextArea(153, 0);

        }

// wysłanie własnej nazwy do drugiego gracza
        try {
            this.sendMyName = message.getMyname();
            if (out != null) {

                out.println(sendMyName);
                out = null;
            }
        } catch (Exception e) {
            message.setTextArea(106, 0);

        }

        // odebranie tablicy z pozycją statków
        try {

            ObjectInputStream in2 = new ObjectInputStream(socket.getInputStream());
            SerialArray serial = new SerialArray();



            serial = (SerialArray) in2.readObject();
            int[][] array = serial.getArray();
            message.seveBoard(array);
            message.setTextArea(111, 0);
            takeArrays = true;
            Thread.sleep(3000);
            message.setTextArea(202, define.lastNumberShip);

        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            message.reset(156);
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
            out2 = new ObjectOutputStream(socket.getOutputStream());

            if (out2 != null) {
                out2.writeObject(serial);
                out2 = null;
            }
            if (takeArrays) {
                message.setTextArea(301, 0);
            } else {
                message.setTextArea(112, 0);
            }

        } catch (Exception e) {
            message.setTextArea(106, 0);


        }
        while (!define.gameOver) {  // pętla dopóki nie ma końca gry
            try {
                // odebranie strzału drugiego gracza
                ObjectInputStream in3 = new ObjectInputStream(socket.getInputStream());
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
            }

            // strzał
            while (!define.sendPosition) {  // pętla czeka na zadanie strzału. zmienna sendPosition zmienia się na true kiedy będzie wcisnięte pole na plnaszy
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
// strzał już sam w sobie
            try {
                int[] shut = message.getActualyShut();
                SerialShut serial2 = new SerialShut();
                serial2.setArray(shut);
                out2 = new ObjectOutputStream(socket.getOutputStream());

                if (out2 != null) {
                    out2.writeObject(serial2);
                    define.sendPosition = false;
                }

            } catch (Exception e) {
                message.setTextArea(106, 0);

            }

        }
// zamknięcie połączenia
        try {
            in.close();
            out.close();
            socket.close();
            message.setTextArea(154, 0);

        } catch (IOException e) {
            message.setTextArea(155, 0);

        }
    }
}
