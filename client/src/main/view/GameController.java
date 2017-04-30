package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import src.main.Client;
import src.main.Room;
import src.main.communication.Encoder;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Client client;
    private Room room;
    private static boolean player1Ready =false;
    private static boolean player2Ready = false;
    @FXML
    private ChessBoard chessPaneController;
    @FXML
    private TextField inputField;
    @FXML
    private Button sentBtn;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private Timer timeLabel01Controller;
    @FXML
    private Timer timeLabel02Controller;
    @FXML
    private ChatBox chatBoxController;
    @FXML
    private Button ready;
    public void setClient(Client client) {
        this.client = client;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public static boolean isBegin(){
        return player1Ready;
        //return player1Ready && player2Ready;
    }

    public static void setPlayer1Ready(boolean ready){
        player1Ready = ready;
    }

    public static void setPlayer2Ready(boolean ready){
        player2Ready = ready;
    }

    @FXML
    private void chat() {
        chatBoxController.sentSentence(inputField.getText());
    }

    @FXML
    private void ready() {
        player1Ready = true;
        String msg = Encoder.readyRequest(room,player1Ready,player2Ready);
        System.out.println("ready msg: "+msg);
        client.getConnect().send(msg);
        ready.setText("取消准备");
        ready.setDisable(true);
        /*********** test **********/
        timeLabel01Controller.start();
        /*********** test **********/
    }

    @FXML
    private void surrender() {
        // TODO: 向服务器发送认输信息
    }

    @FXML
    private void judge() {
        // TODO: 向服务器发送判子请求信息
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chessPaneController.setTimer(timeLabel01Controller, timeLabel02Controller);
        timeLabel01Controller.initTimer(1, 10, 3);
        timeLabel02Controller.initTimer(1, 10, 3);
    }
}
