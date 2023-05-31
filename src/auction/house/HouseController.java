/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.house;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HouseController {
    public TextField host;
    public TextField port;
    public TextField message;
    public Label serverResponse;
    public Label connectStatus;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * handles what happens when an agent connects to a house
     */
    public void handleConnect(ActionEvent actionEvent) {
        try {
            socket = new Socket(host.getText(), Integer.parseInt(
                    port.getText()));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            connectStatus.setText("Connection successful!");
            connectStatus.setStyle("-fx-text-fill: green;");
        } catch (IOException e) {
            connectStatus.setText("Connection to server failed!");
            connectStatus.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * sends the message to the bank
     */
    public void sendMessageToBank(ActionEvent actionEvent) throws IOException {
        if (socket != null && out != null && in != null) {
            out.println(message.getText());
            serverResponse.setText(in.readLine());
        }
    }
}
