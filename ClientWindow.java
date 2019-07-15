package chat.client;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;


public class ClientWindow extends Application implements TCPConnectionListener {

    private static final String IP_ADDRES = "192.168.1.190";
    private static final int PORT = 8189;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField writeArea;
    @FXML
    private TextField nameArea;
    @FXML
    private Button send;
    TCPConnection connection;

    public void buttonClickHandler(ActionEvent evt){
        String msg = writeArea.getText();
        if(msg.equals("") || msg.equals(null)) return;
        writeArea.setText(null);
        connection.sendString(nameArea.getText() + ": " + msg);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Java Chat");
        primaryStage.setScene(new Scene(root, 590, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
               new ClientWindow();
            }
        });
    }


    public ClientWindow(){
        try {
            connection = new TCPConnection(this, IP_ADDRES, PORT);
        } catch (IOException e) {
            printMsg("Connection Exception" + e);
        }
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
        SwingUtilities.invokeLater(() -> textArea.appendText(msg + "\n"));
    }

}
