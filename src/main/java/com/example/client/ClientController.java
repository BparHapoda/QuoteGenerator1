package com.example.client;

import javafx.fxml.FXML;
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
    private Text citate;

    private DataOutputStream out;
    private DataInputStream in;
    private String name;
    private boolean connected;

    @FXML
    public void onClick() {
        if (!connected) {
            connectWithServer();
        }
        try {
            out.writeUTF("@getCitate ");
            String string;
            while (true) {
                string = in.readUTF();
                if (string.startsWith("@citate ")) {
                    citate.setText(string.substring(8));
                    break;
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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