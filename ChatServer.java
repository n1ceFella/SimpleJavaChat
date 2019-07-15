package chat.server;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer extends Application implements TCPConnectionListener {

        public static void main(String[] args) {

            new ChatServer();
            //launch(args);
        }

        private final ArrayList<TCPConnection> connections = new ArrayList<>();

        private ChatServer(){
            /**
            JFrame j = new JFrame();
            JPanel p = new JPanel(new BorderLayout());
            JLabel l = new JLabel("Server is running");
            p.add(l, "Center");
            j.setSize(200,100);
            j.setContentPane(p);
            j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            j.setVisible(true);
             */
                System.out.println("Server running...");
                try(ServerSocket serverSocket = new ServerSocket(8189)) {
                    while (true){
                        try {
                            new TCPConnection(this,serverSocket.accept());
                        }catch (IOException e){
                            System.out.println("TCPConnection exception: " + e);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value){
            System.out.println(value);
        for (TCPConnection connection : connections)
            connection.sendString(value);
    }
}


