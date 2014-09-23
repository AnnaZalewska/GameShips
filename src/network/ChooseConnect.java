/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import gameships.Define;
import gameships.GameManager;
import gameships.Who;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 *
 * @author Anna Zalewska
 */
public class ChooseConnect extends JDialog {

    JRadioButton server;
    JRadioButton client;
    JTextField host;
    JTextField port;
    JLabel namePort;
    JLabel nameHost;
    Define define;

    public ChooseConnect(Frame owner, GameManager message) {

        super(owner, true);

        this.define = message.getDefine();

        this.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

        this.setLocationRelativeTo(null);

        this.setPreferredSize(new Dimension(270, 120));

        this.setTitle("Wybór połączenia");

        this.setResizable(false);

        this.server = new JRadioButton("Stwórz serwer");

        this.client = new JRadioButton("Dołącz do gry      ");

        this.namePort = new JLabel("Port: ");

        this.port = new JTextField(Integer.toString(define.PORT));

        this.nameHost = new JLabel("Host: ");

        this.host = new JTextField(define.HOST);


        this.host.setEnabled(false);

        this.getContentPane().add(server);

        this.getContentPane().add(client);

        this.getContentPane().add(namePort);

        this.getContentPane().add(port);

        this.getContentPane().add(nameHost);

        this.getContentPane().add(host);

        ButtonGroup group = new ButtonGroup();

        group.add(server);

        group.add(client);

        JButton ok = new JButton("OK");

        ok.setAlignmentX(CENTER_ALIGNMENT);

        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                setVisible(false);
                dispose();
            }
        });
        client.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                host.setEnabled(true);
            }
        });

        this.getContentPane().add(ok);
        pack();

    }

    public Who showDialog() {
        String foo;
        Who choose = Who.UNKNOWN;

        setVisible(true);


        if (server.isSelected()) {
            choose = Who.SERVER;

        } else if (client.isSelected()) {

            choose = Who.CLIENT;
        }

        foo = port.getText();
        define.PORT = Integer.valueOf(foo.trim()).intValue();

        foo = host.getText();
        if (foo != "") {
            define.HOST = foo;
        }
        return choose;
    }
}
