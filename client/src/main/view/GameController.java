package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import src.main.Client;
import src.main.Room;
import src.main.Stone;
import src.main.communication.Encoder;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Client client;
    private Room room;
    private static boolean player1Ready =false;
    private static boolean player2Ready = false;
    private static int turn;
    @FXML
    private ChessBoard boardController;
    @FXML
    private TextField inputField;
    @FXML
    private Button send;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private Timer player1TimerController;
    @FXML
    private Timer player2TimerController;
    @FXML
    private ChatBox chatBoxController;
    @FXML
    private Button ready;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRoom(Room room) {
        this.room = room;
        if(room.getPlayer1() != null && room.getPlayer1() == client.getUser().getAccount()){
            turn = Stone.White;
            //boardController.setColor(Stone.White);
        }
        else{
            turn = Stone.Black;
            //boardController.setColor(Stone.Black);
        }
        // TODO 设置Label 和时间
        player1TimerController.init(2, 10, 3);
        player2TimerController.init(2, 10, 3);
        //boardController.setTimer(player1TimerController, player2TimerController);
    }

    public static boolean isBegin(){
        return player1Ready;
        //return player1Ready && player2Ready;
    }

    public static int getTurn(){
        return turn;
    }

    public static void takeTurns(){
        turn = -turn;
    }

    public static void setPlayer1Ready(boolean ready){
        player1Ready = ready;
    }

    public static void setPlayer2Ready(boolean ready){
        player2Ready = ready;
    }

    public void clear(){
        ready.setText("准备");
        ready.setDisable(false);
        boardController.clear();
        chatBox.refresh();
        chatBoxController.clear();
    }

    @FXML
    private void chat() {
        chatBoxController.sendMessage(inputField.getText());
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
        player1TimerController.start();
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
        player1TimerController.init(1, 10, 3);
        player2TimerController.init(1, 10, 3);
        boardController.setTimer(player1TimerController, player2TimerController);
    }
}
