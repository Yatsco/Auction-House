/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.bank;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BankController {
    public TextField host;
    public TextField port;
    public TextField message;
    public Label serverResponse;
    public Label connectStatus;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
}

