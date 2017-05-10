package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.main.*;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Room room;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private boolean roomOwner = false;
    private boolean begin = false;
    private int turn;

    // Room
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
    private ChessBoard boardController;
    @FXML
    private Button ready;
    @FXML
    private Button surrender;
    @FXML
    private Button judge;

    @FXML
    private ToggleButton step;


    @FXML
    private ChatBox chatBoxController;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private TextField inputField;
    @FXML
    private Button send;
    @FXML
    private Timer player1TimerController;
    @FXML
    private Timer player2TimerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardController.setTimer(player1TimerController, player2TimerController);
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
        turn = Stone.Black;
        if((room.getPlayer1() != null || room.getPlayer1().isEmpty()) && room.getPlayer1() == Client.getUser().getAccount()){
            roomOwner = true;
            boardController.setColor(Stone.White);
        }
        else{
            roomOwner = false;
            boardController.setColor(Stone.Black);
        }
        /********** release ******/
        /***** test *****/
        /*turn = Stone.Black;
        boardController.setColor(Stone.Black);*/
        /***** test *****/
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

    public boolean isBegin() {
        return begin;
    }

    public int getTurn() {
        return turn;
    }

    public void takeTurns() {
        turn = -turn;
    }

    public void setReady(boolean player1, boolean player2) {
        player1Ready = player1;
        player2Ready = player2;
        if (player1Ready && player2Ready) {
            gameStart();
        }
    }

    @FXML
    private void ready() {
        /*************** test *************/
        /*if (player1Ready == false) {
            player1Ready = true;
            begin = true;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            Connect.send(msg);
            ready.setText("取消准备");
            Client.getUser().setState(Type.UserState.GAMING);
            player2TimerController.start();
        } else {
            begin = false;
            ready.setText("准备");
            Client.getUser().setState(Type.UserState.READY);
        }*/
        /*************** test *************/

        /***************** release **************/
        if (player1Ready == false) {
            player1Ready = true;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            Connect.send(msg);
            ready.setText("取消准备");
        } else {
            player1Ready = false;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready msg: " + msg);
            Connect.send(msg);
            ready.setText("准备");
        }
        /***************** release **************/
    }

    @FXML
    public void gameStart() {
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
    public void gameOver() {
        begin = false;
        player1TimerController.pause();
        player2TimerController.pause();
        if (roomOwner) {
            ArrayList<Number> point = boardController.getPlayerPoint();
            double p1 = (double) point.get(0);
            double p2 = (double) point.get(1);
            double diff = p1 - p2;
            if (diff < 0.001) {
                String msg = Encoder.gameOverRequest(room.getId(), 0.0, 0.0, Type.GameResult.DRAW);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            } else if (diff > 0) {
                String msg = Encoder.gameOverRequest(room.getId(), diff, diff, Type.GameResult.WIN);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            } else {
                String msg = Encoder.gameOverRequest(room.getId(), -diff, -diff, Type.GameResult.LOSE);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            }
        }
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

    public void overTime() {
        if (roomOwner) {
            ArrayList<Number> point = boardController.getPlayerPoint();
            double p1 = (double) point.get(0);
            double p2 = (double) point.get(1);
            double diff = p1 - p2;
            boolean lose = player1TimerController.getPeriodTimes() == 0;
            if (diff < 0.001) {
                p1 = lose ? 6.0 : 0.0;
                p2 = lose ? 0.0 : 6.0;
            } else if (diff > 0) {
                p1 = lose ? 3.0 : diff;
                p2 = lose ? diff : 3.0;
            } else {
                p1 = lose ? -diff : 6.0;
                p2 = lose ? 6.0 : -diff;
            }
            String msg = Encoder.gameOverRequest(room.getId(), p1, p2, lose ? Type.GameResult.PLAYER1_OVERTIME : Type.GameResult.PLAYER2_OVERTIME);
            Connect.send(msg);
            System.out.println("game result msg: " + msg);
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
    private void surrender() {
        ArrayList<Number> point = boardController.getPlayerPoint();
        double p1 = (double) point.get(0);
        double p2 = (double) point.get(1);
        double diff = p1 - p2;
        if (diff < 0.001) {
            p1 = roomOwner ? 6.0 : 0.0;
            p2 = roomOwner ? 0.0 : 6.0;
        } else if (diff > 0) {
            p1 = roomOwner ? 3.0 : diff;
            p2 = roomOwner ? diff : 3.0;
        } else {
            p1 = roomOwner ? -diff : 6.0;
            p2 = roomOwner ? 6.0 : -diff;
        }
        String msg = Encoder.gameOverRequest(room.getId(), p1, p2, roomOwner ? Type.GameResult.PLAYER1_SURRENDER : Type.GameResult.PLAYER2_SURRENDER);
        Connect.send(msg);
        System.out.println("game result msg: " + msg);
    }

    @FXML
    public void judge() {
        String msg = Encoder.judgeRequest(room.getId(), roomOwner);
        Connect.send(msg);
        System.out.println("judge request msg: " + msg);
    }

    @FXML
    public void judgeEnable() {
        judge.setDisable(false);
    }

    @FXML
    public void judgeForcedEnable() {
        judge.setText("强制判子");
        judge.setDisable(false);
    }

    public void judgeFromOpponent() {
        int res = JOptionPane.showConfirmDialog(null, "对方请求提前判子\n请问您是否同意？", "请求", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            gameOver();
        }
    }

    // chat windows
    @FXML
    private void hasText() {
        String text = inputField.getText();
        if (text == null || "".equals(text) || text.isEmpty()) {
            send.setDisable(true);
        } else {
            send.setDisable(false);
        }
    }

    @FXML
    private void chat() {
        chatBoxController.sendMessage(Client.getUser().getNickname() + ":" + inputField.getText());
        Connect.send(inputField.getText());
        inputField.clear();
        send.setDisable(true);
    }

}
