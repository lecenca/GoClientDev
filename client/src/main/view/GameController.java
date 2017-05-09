package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import src.main.*;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Client client;
    private Room room;
    private static boolean player1Ready = false;
    private static boolean player2Ready = false;
    private static int turn;

    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;
    @FXML
    private Label komi;
    @FXML
    private Label mainTime;
    @FXML
    private Label periodTime;
    @FXML
    private ToggleButton step;

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
        player1Name.setText(room.getPlayer1Name());
        player2Name.setText(room.getPlayer2Name());
        komi.setText(room.getKomi());
        mainTime.setText(room.getMainTime() + "分");
        periodTime.setText(room.getPeriodTime() + "秒" + room.getPeriodTimes() + "次");
        player1TimerController.init(room.getMainTime(), room.getPeriodTime(), room.getPeriodTimes());
        player2TimerController.init(room.getMainTime(), room.getPeriodTime(), room.getPeriodTimes());

        turn = Stone.White;
        /*if(room.getPlayer1() != null && room.getPlayer1() == client.getUser().getAccount()){
            turn = Stone.White;
            boardController.setColor(Stone.White);
        }
        else{
            turn = Stone.Black;
            boardController.setColor(Stone.Black);
        }*/

        //boardController.setTimer(player1TimerController, player2TimerController);
    }

    public static boolean isBegin() {
        return player1Ready;
        //return player1Ready && player2Ready;
    }

    public static int getTurn() {
        return turn;
    }

    public static void takeTurns() {
        turn = -turn;
    }

    public static void setPlayer1Ready(boolean ready) {
        player1Ready = ready;
    }

    public static void setPlayer2Ready(boolean ready) {
        player2Ready = ready;
    }

    public void clear() {
        ready.setText("准备");
        ready.setDisable(false);
        player1Ready = false;
        player2Ready = false;
        step.setSelected(false);
        boardController.clear();
        chatBox.refresh();
        chatBoxController.clear();
        player1TimerController.stop();
        player2TimerController.stop();
    }

    public void place(int x, int y, int color) {
        boardController.place(x, y, color);
    }

    public void kill(ArrayList<Stone> deadList) {
        Board.addDead(deadList);
        boardController.remove();
    }

    public void checkKo(int x, int y, ArrayList<Stone> deadList) {
        if (deadList.size() == 1) {
            Board.setKoPoint(x, y, deadList);
        }
    }

    @FXML
    public boolean isShowStep() {
        return step.isSelected();
    }

    @FXML
    private void showStep() {
        if (step.isSelected()) {
            boardController.showStep();
        } else {
            boardController.hideStep();
        }
    }

    @FXML
    private void chat() {
        chatBoxController.sendMessage(client.getUser().getNickname() + ":" + inputField.getText());
        client.getConnect().send(inputField.getText());
        inputField.clear();
        send.setDisable(true);
    }

    @FXML
    private void ready() {
        if (player1Ready == false) {
            player1Ready = true;
            String msg = Encoder.readyRequest(room, player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            client.getConnect().send(msg);
            ready.setText("取消准备");
            Client.getUser().setState(Type.UserState.GAMING);
            /*********** test **********/
            player1TimerController.start();
            /*********** test **********/
        } else {
            /*************** test *************/
            ready.setText("准备");
            Client.getUser().setState(Type.UserState.READY);
            /*************** test *************/
        }
    }

    @FXML
    private void surrender() {
        String msg = Encoder.surrenderRequest(room);
        client.getConnect().send(msg);
        Connect.waitForRec();
    }

    @FXML
    private void judge() {
        String msg = Encoder.judgeRequest(room);
        client.getConnect().send(msg);
        Connect.waitForRec();
    }

    @FXML
    private void hasText() {
        String text = inputField.getText();
        if (text == null || "".equals(text) || text.length() == 0)
            send.setDisable(true);
        else
            send.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player1TimerController.init(1, 10, 3);
        player2TimerController.init(1, 10, 3);
        boardController.setTimer(player1TimerController, player2TimerController);
    }
}
