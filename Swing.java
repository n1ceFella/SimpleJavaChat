package chat.swing;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Swing extends JFrame implements TCPConnectionListener, ActionListener
{

    private static final String IP_ADDRES = "192.168.1.190";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private JTextArea textArea = new JTextArea();

    private JTextField writeArea = new JTextField();

    private JTextField name = new JTextField();

    //private JButton send;

    TCPConnection connection;


    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                new Swing();
            }
        });
    }

    private Swing(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        add(textArea, BorderLayout.CENTER);

        writeArea.addActionListener(this);
        add(writeArea, BorderLayout.SOUTH);
        add(name, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this,IP_ADDRES,PORT);
        } catch (IOException e) {
            printMsg("Exception: " + e);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = writeArea.getText();
        if(msg.equals("")) return;
        writeArea.setText(null);
        connection.sendString(name.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close...");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection Exception" + e);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                textArea.append(msg + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }

}
