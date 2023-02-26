package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientController {
    @FXML
    private VBox citateWindow;
    @FXML
    private VBox loginWindow;
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
    private TextField loginW;
    @FXML
    private PasswordField password;

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
            throw new RuntimeException("Подключение невозможно");

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

        this.connected = true;


    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
    }

    @FXML
    public void checkLogin() {

        if (!this.connected) {
            connectWithServer();
        }


        String login = loginW.getText();
        String pass = password.getText();
        try {
            out.writeUTF("@checkLogin " + login + " " + pass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String req;
        while (true) {
            try {
                req = in.readUTF();
                System.out.println(req);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (req.equals("@login_ok")) {
                loginWindow.setManaged(false);
                loginWindow.setVisible(false);
                citateWindow.setManaged(true);
                citateWindow.setVisible(true);
                break;
            }

            if (req.equals("@login_bad")) {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Неправильный пароль");
alert.showAndWait();
                return;
            }

        }
    }

    @FXML
    public void register() {
        String pass = password.getText();
        String login = loginW.getText();
        try {
            out.writeUTF("@regLogin " + login + " " + pass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}