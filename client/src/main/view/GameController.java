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
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Client client;
    private Room room;
    private static boolean player1Ready = false;
    private static boolean player2Ready = false;
    private static boolean roomOwner = false;
    private static boolean begin = false;
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
    @FXML
    private Button surrender;
    @FXML
    private Button judge;

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
        /********** release ******/
        /*turn = Stone.Black;
        if(room.getPlayer1() != null && room.getPlayer1() == client.getUser().getAccount()){
            roomOwner = true;
            boardController.setColor(Stone.White);
        }
        else{
            roomOwner = false;
            boardController.setColor(Stone.Black);
        }*/
        /********** release ******/
        /***** test *****/
        turn = Stone.Black;
        boardController.setColor(Stone.Black);
        /***** test *****/
    }

    public boolean isBegin() {
        return begin;
    }

    public int getTurn() {
        return turn;
    }

    public void takeTurns() {
        turn = -turn;
    }

    public void clear() {
        ready.setText("准备");
        ready.setDisable(false);
        surrender.setDisable(true);
        judge.setDisable(true);
        player1Ready = false;
        player2Ready = false;
        begin = false;
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
    public void judgeEnable(){
        judge.setDisable(false);
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

    public void setReady(boolean player1, boolean player2){
        player1Ready = player1;
        player2Ready = player2;
        if(player1Ready && player2Ready){
            gameStart();
        }
    }

    @FXML
    private void ready() {
        /*************** test *************/
        if (player1Ready == false) {
            player1Ready = true;
            begin = true;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            client.getConnect().send(msg);
            ready.setText("取消准备");
            Client.getUser().setState(Type.UserState.GAMING);
            player2TimerController.start();
        } else {
            begin = false;
            ready.setText("准备");
            Client.getUser().setState(Type.UserState.READY);
        }
        /*************** test *************/

        /***************** release **************/
        /*if (player1Ready == false) {
            player1Ready = true;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            client.getConnect().send(msg);
            ready.setText("取消准备");
        } else {
            player1Ready = false;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            client.getConnect().send(msg);
            ready.setText("准备");
            Client.getUser().setState(Type.UserState.READY);
        }*/
        /***************** release **************/
    }

    @FXML
    public void gameStart(){
        ready.setText("游戏中");
        ready.setDisable(true);
        begin = true;
        surrender.setDisable(false);
        player2TimerController.start();
        Client.getUser().setState(Type.UserState.GAMING);
        Client.updateUser();
        room.setState(Type.RoomState.GAMING);
        Client.updateRoom(room, Type.UpdateRoom.STATE_CHANGE);
    }

    @FXML
    public void gameOver(){
        begin = false;
        ArrayList<Number> point = boardController.getPlayerPoint();
        double p1 = (double)point.get(0);
        double p2 = (double)point.get(1);
        double diff = p1 - p2;
        if(diff < 0.001){
            String msg = Encoder.gameOverRequest(room.getId(), 0.0, 0.0,Type.GameResult.WIN);
            client.getConnect().send(msg);
            System.out.println("game result msg: "+ msg);
        }
        else if(diff > 0){
            String msg = Encoder.gameOverRequest(room.getId(), diff, diff,Type.GameResult.WIN);
            client.getConnect().send(msg);
            System.out.println("game result msg: "+ msg);
        }
        else{
            String msg = Encoder.gameOverRequest(room.getId(), -diff, -diff,Type.GameResult.LOSE);
            client.getConnect().send(msg);
            System.out.println("game result msg: "+ msg);
        }
    }

    @FXML
    private void surrender() {
        ArrayList<Number> point = boardController.getPlayerPoint();
        double p1 = (double)point.get(0);
        double p2 = (double)point.get(1);
        double diff = p1 - p2;
        if(diff < 0.001){
            p1 = roomOwner ? 6.0 : 0.0;
            p2 = roomOwner ? 0.0 : 6.0;
        }
        else if(diff > 0){
            p1 = roomOwner ? 3.0 : diff;
            p2 = roomOwner ? diff : 3.0;
        }
        else{
            p1 = roomOwner ? -diff : 6.0;
            p2 = roomOwner ? 6.0 : -diff;
        }
        String msg = Encoder.gameOverRequest(room.getId(), p1,p2,roomOwner ? Type.GameResult.PLAYER1_SURRENDER : Type.GameResult.PLAYER2_SURRENDER);
        client.getConnect().send(msg);
        System.out.println("game result msg: "+ msg);
    }

    @FXML
    private void judge() {
        String msg = Encoder.judgeRequest(room.getId(), roomOwner);
        client.getConnect().send(msg);
        System.out.println("judge request msg: "+msg);
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
        boardController.setTimer(player1TimerController, player2TimerController);
    }
}
