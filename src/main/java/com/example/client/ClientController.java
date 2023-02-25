package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientController {

    @FXML
    private Button btn;
    @FXML
    private HBox div;
    @FXML
    private Text citate;

    private DataOutputStream out;
    private DataInputStream in;
    private String name;
    private boolean connected;

    @FXML
    public void onClick() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        btn.setDisable(true);
        if (!connected) {
            connectWithServer();
        }
        try {
            out.writeUTF("@getCitate ");
            String string;
            while (true) {
                string = in.readUTF();
                if (string.startsWith("@break")) {
                    citate.setStyle("-fx-text-fill: red");
                    citate.setText("Количество бесплатных цитат превышено");
                    in.close();
                    out.close();
                    return;
                }
                if (string.startsWith("@citate ")) {
                    out.writeUTF("@logMessage " + string.substring(8));
                    citate.setText(string.substring(8));
                    break;
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        btn.setDisable(false);
    }

    public void connectWithServer() {

        Socket socket = null;
        try {
            socket = new Socket("localhost", 7777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connected = true;


    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
    }

}