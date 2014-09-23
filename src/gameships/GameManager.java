/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameships;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import network.ChooseConnect;
import network.Client;
import network.Server;

/**
 *
 * @author Anna Zalewska
 */
public class GameManager extends JComponent implements KeyListener {
    
    private Define define;  // stałe w programie
    private int WIDTH;
    private int HAIGHT;
    private Board myBoard;   // plansza głównego gracza
    private Board secondBoard; // plansza przeciwnika
    private JLabel myName;    // nazwa głównego gracza
    private JLabel secondName;
    private JButton gameComputer;   // przycisk do gry z komputerem
    private JButton gameNetwork;    // przycisk do gry przez sieć
    private JButton setShip;       // przycisk do ustawiania statków na głównej planszy
    private JButton start;         //  przycisk do rozpoczęcia gry
    private MenuManager menu;     // panel odpowidzialny za boczne menu
    private JTextArea text;
    private boolean send;
    private boolean gameOver;
    private boolean play;
    private Who who;
    private int oneClik;
    private TypeGame game;
    private int[] actualyShut;
    private int lastMessage;
    private JTextField cheat;
    
    public GameManager(int w, int h) {
        
        this.define = new Define();
        
        this.WIDTH = w;
        
        this.HAIGHT = h;
        
        this.gameOver = false;
        
        this.play = false;
        
        this.send = true;
        
        this.oneClik = 1;
        
        this.myBoard = new Board(50, 75, 250, 300, "myBoard", this);
        
        this.secondBoard = new Board(330, 75, 250, 300, "secondBoard", this);
        
        this.actualyShut = new int[2];
        
        this.myName = new JLabel("Ja");
        
        this.secondName = new JLabel("Przeciwnik");
        
        this.cheat = new MyTextField(secondBoard);
        
        this.menu = new MenuManager(610, 75, 145, 300);
        
        this.setLayout(null);
        
        this.setSize(WIDTH, HEIGHT);
        
        this.setBounds(0, 0, w, h);
        
        this.gameComputer = menu.addOneButton("Graj z komputerem", menu, 145, 30);
        
        this.gameNetwork = menu.addOneButton("Graj przez sieć", menu, 145, 30);
        
        this.setShip = menu.addOneButton("Ustaw statki", menu, 145, 30);
        
        this.start = menu.addOneButton("Start", menu, 145, 30);
        
        this.start.setEnabled(false);
        
        this.setShip.setEnabled(false);
        
        this.setFocusable(true);
        
        
        gameComputer.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GameComputerMouseClicked(evt);
            }
        });
        gameNetwork.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GameNetworkMouseClicked(evt);
            }
        });
        start.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StartMouseClicked(evt);
            }
        });
        setShip.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SetShipMouseClicked(evt);
            }
        });
        this.setVisible(true);
    }
    
    public void play() {
        
        this.printBoards();
        
        String name = JOptionPane.showInputDialog(null, "Podaj swoje imie:", "Podaj swoje imie", 2);
        
        
        insertMyName(myName, name, 50, 45, 200, 30);
        
        addKeyListener(this);
        
        createTextArea();
        menu.add(text);
        add(menu);
        add(cheat);
        text.setVisible(true);
        repaint();
    }
    
    public void insertMyName(JLabel title, String name, int x, int y, int w, int h) {
        
        title.setText(name);
        title.setBounds(x, y, w, h); // 200, 30
        title.setForeground(new Color(246, 80, 80));
        Font f = title.getFont();
        title.setFont(new Font(f.getFontName(), f.getStyle(), 27));
        this.add(title);
    }
    
    public void createTextArea() {
        text = new JTextArea("Witaj!\nWybierz rodzaj gry.", 145, 30);
        text.setBounds(0, 0, 145, 30);
        text.setPreferredSize(new Dimension(145, 30));
        text.setVisible(true);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    public void setTextArea(int numberText, int numberToDisplay) {
        
        lastMessage = numberText;
        text.setBackground(null);
        
        switch (numberText) {
            case 1:
                text.setText("Witaj!\nWybierz rodzaj gry.");
                break;
            // komunikaty servera
            case 100:
                text.setText("Utworzono gniazdo serwera. Oczekiwanie na połączenie z drugim graczem...");
                break;
            case 101:
                text.setText("Nie można utworzyć gniazda serwera.");
                break;
            case 102:
                text.setText("Połączenie z drugim graczem nawiązane. Kliknij 'Ustaw statki' aby ustawić statki na lewej planszy");
                break;
            case 103:
                text.setText("Nie można nawiązać połączenia z drugim graczem.");
                break;
            case 104:
                text.setText("Nie można pobrać strumienia wyjściowego.");
                break;
            case 105:
                text.setText("Nie można utworzyć strumienia wyjściowego PrintWriter.");
                break;
            case 106:
                text.setText("Nie można wysłać nazwy.");
                break;
            case 107:
                text.setText("zamknięcie strumieni");
                break;
            case 108:
                text.setText("Nie można zamknąć nstrumieni wyjściowych.");
                break;
            case 109:
                text.setText("zamknięcie połączenia");
                break;
            case 110:
                text.setText("Nie można zamknąć połączenia.");
                break;
            case 111:
                text.setText("Drugi gracz już ustawił swoje statki.");
                text.setBackground(new Color(237, 234, 85));
                break;
            case 112:
                text.setText("Oczekiwanie na kliknięcie 'start' przez drugiego gracza");
                break;
            // komunikty klienta
            case 150:
                text.setText("Połączenie z drugim graczem nawiązane. Klinij 'Ustaw statki', aby ustawić staki na planszy po lewej");
                break;
            case 151:
                text.setText("Nieznana nazwa hosta. Sprawdź czy wpisywałeś dobrą nazwę host i spróbuj jeszcze raz się połączyć.");
                break;
            case 152:
                text.setText("Nie można utworzyć gniazda klienta. Serwer gry, nie został jeszcze utworzony lub podano zły numer portu. Spróbuj jeszcze raz");
                break;
            case 153:
                text.setText("Nie można odczytać danych z serwera.");
                break;
            case 154:
                text.setText("Zakończenie połączenia");
                break;
            case 155:
                text.setText("Nie można zamknąć gniazda sieciowego klienta.");
                break;
            case 156:
                text.setText("Połączenie zostało zerwane. Sprobuj zacząć grę od nowa");
                text.setBackground(new Color(237, 85, 126));
                break;
            //komunikaty wyboru opcji gry
            case 200:
                text.setText("Wybrałeś grę z komputerem. Kliknij 'Ustaw statki' aby ustawić statki na swojej planszy (plansza po lewej stronie)");
                break;
            case 201:
                text.setText("Wybrałeś grę przez sieć.");
                break;
            case 202:
                String word = "";
                if (numberToDisplay == 5) {
                    word = "kratek";
                } else {
                    word = "kratki";
                }
                text.setText("Ustawiasz " + numberToDisplay + " masztowiec. Kliknij na " + numberToDisplay + " " + word + " w pionie lub poziomie aby ustawić statek");
                break;
            case 203:
                text.setText("Ustawiasz drugi " + numberToDisplay + " masztowiec. Kliknij na " + numberToDisplay + " kratki w pionie lub poziomie aby ustawić statek");
                break;
            case 204:
                text.setText("Klnikj Start aby rozpocząć grę");
                break;
            // komunikaty z gry
            case 300:
                text.setText("Twój ruch");
                break;
            case 301:
                text.setText("Ruch przeciwnika");
                break;
            case 302:
                text.setText("Trafiony, zatopiony");
                break;
            case 303:
                text.setText("trafiony, nie zatopiony");
                break;
            case 304:
                text.setText("Pudło");
            case 305:
                text.setText("Ustawiłeś " + numberToDisplay + " masztowiec");
                break;
            case 306:
                text.setText("W tym polu nie można ustawić .Między jednym a drugim statkiem musi być przynajmniej jedna kratka odstępu. Kliknij w dobre pole.");
                text.setBackground(new Color(237, 85, 126));
                
                break;
            case 307:
                text.setText("To nie jest Twój ruch");
                break;
            case 308:
                text.setText("Pola statków muszą być klikane po kolej, jeden obok drugiego. Klknij w dobre pole.");
                text.setBackground(new Color(237, 85, 126));
                break;
        }
        repaint();
    }
    
    private void GameComputerMouseClicked(MouseEvent evt) {
        setTextArea(200, 0);
        setShip.setEnabled(true);
        gameComputer.setEnabled(false);
        gameNetwork.setEnabled(false);
        setSecondName("Komputer");
        game = TypeGame.PC;
        
    }
    
    private void GameNetworkMouseClicked(MouseEvent evt) {
        setTextArea(201, 0);
        
        gameComputer.setEnabled(false);
        gameNetwork.setEnabled(false);
        game = TypeGame.NETWORK;
        Conncet();
        
    }
    
    private void StartMouseClicked(MouseEvent evt) {
        if (who == Who.SERVER) {
            define.setMotion(1);
            this.setTextArea(300, 0);
        } else if (who == Who.CLIENT) {
            define.setMotion(0);
            this.setTextArea(301, 0);
        }
        
        this.send = false;
        this.setActionBoard("myBoard", ClikInButton.PAUSE);
        
        secondBoard.setAction(ClikInButton.GAME);
        if (game == TypeGame.PC && oneClik == 1) {
            secondBoard.RandomInsertShip();
            oneClik++;
            define.setMotion(1);
            setTextArea(300, 0);
            
        }        
        addKeyListener(this);
    }
    
    private void SetShipMouseClicked(MouseEvent evt) {
        setTextArea(202, 5);
        setShip.setEnabled(false);
        gameComputer.setEnabled(false);
        gameNetwork.setEnabled(false);
        myBoard.insertShips(define.maxFiledShip);
        
        
    }
    
    public void enableButton(JButton button, boolean enabled, int message) {
        button.setEnabled(enabled);
        setTextArea(message, 0);
    }
    
    public void setStatusInsertShip(String text) {
        
        repaint();
    }
    
    public void printBoards() {
        this.add(myBoard).setVisible(true);
        this.add(secondBoard);
        myBoard.printBoard(1);
        secondBoard.printBoard(2);
    }
    
    public void setSecondName(String name) {
        this.insertMyName(secondName, name, 330, 45, 200, 30);
        repaint();
    }
    
    public String getMyname() {
        return myName.getText();
    }
    
    public void Conncet() {
        ChooseConnect isServer = new ChooseConnect(null, this);
        Who who = isServer.showDialog();
        if (who == Who.SERVER) {
            who = Who.SERVER;
            Server server = new Server(define.PORT, this);
            setTextArea(100, 0);
            server.start();
            repaint();
            
        } else if (who == Who.CLIENT) {
            who = Who.CLIENT;
            Client klient = new Client(define.HOST, define.PORT, this);
            klient.start();
            repaint();
        } else if (who == Who.UNKNOWN) {
            this.reset(1);
        }
    }
    
    public boolean getWyslij() {
        return send;
    }
    
    public TypeGame getGame() {
        return game;
    }
    
    public Board gettt() {
        return myBoard;
    }
    
    public int[][] downloadArray() {
        int[][] tt = myBoard.seveShips();
        return tt;
    }
    
    public void seveBoard(int[][] array) {
        secondBoard.downloadShips(array);
    }
    
    public void shutSecendPlayer(int i, int j) {
        
        if (game == TypeGame.PC) {
            
            do {
                Random r = new Random();
                i = r.nextInt(10);
                j = r.nextInt(10);
                
                
            } while (myBoard.checkField(i, j));
        }
        
        myBoard.shutt(i, j);
        
        define.setMotion(1);
    }
    
    public Define getDefine() {
        return define;
    }
    
    public void getPositionShipAndSendNetwork(int[] array) {
        this.actualyShut = array;
    }
    
    public int[] getActualyShut() {
        return actualyShut;
    }
    
    public TypeGame getTypeGame() {
        return game;
    }
    
    public int getNumberLastMessage() {
        return lastMessage;
    }
    
    public JButton getButton(String text) {
        JButton exit = new JButton();
        switch (text) {
            case "START":
                return start;
            case "INSERT":
                return setShip;
        }
        return exit;
    }
    
    public void setActionBoard(String name, ClikInButton a) {
        if (name == myBoard.getName()) {
            myBoard.setAction(a);
        } else {
            secondBoard.setAction(a);
        }
    }
    
    public void showWhoWin(int win) {
        String text;
        
        if (win == 2) {
            text = "Brawo, Wygrałeś! :)";
        } else if (win == 1) {
            text = "Wygrał przeciwnik :(";
        } else {
            text = "";
        }
        define.gameOver = true;
        JOptionPane.showMessageDialog(null, text, "koniec gry", 2);
        reset(1);
    }
    
    public void reset(int message) {
        setShip.setEnabled(false);
        gameComputer.setEnabled(true);
        gameNetwork.setEnabled(true);
        start.setEnabled(false);
        myBoard.setAllField(TypeField.EMPTY);
        secondBoard.setAllField(TypeField.EMPTY);
        define.lastNumberShip = 5;
        define.myShips = 0;
        define.secondShips = 0;
        define.motion = 1;
        oneClik = 1;
        secondName.setText(" ");
        setTextArea(message, 0);
    }
    
    public JTextField createCheat(JTextField c) {
        c = new JTextField("");
        c.setBounds(0, 0, 100, 30);
        c.setBackground(Color.WHITE);
        c.setVisible(false);
        
        return c;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_C) {
            cheat.setVisible(true);
        }
        
    }
}

class MyTextField extends JTextField implements KeyListener {
    
    private Board board;
    
    public MyTextField(Board board) {
        this.board = board;
        
        this.setBounds(0, 0, 100, 25);
        this.setBackground(Color.WHITE);
        this.setVisible(false);
        this.setFocusable(true);
        addKeyListener(this);
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER) {
            this.setVisible(false);
            String text = "Show_on";
            String text2 = this.getText();
            if (text.equals(text2)) {
                board.show();
            }
            
        }
    }
}
