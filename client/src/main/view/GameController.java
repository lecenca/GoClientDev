package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private int turn = Stone.Black;

    private int gameResult;
    private int score;

    // Room
    @FXML
    private AnchorPane gamePane;
    @FXML
    private HBox playerPane;
    @FXML
    private AnchorPane scorePane;

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
    private ToggleButton ready;
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

    @FXML
    private Label gameResultShow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameResultShow.setVisible(false);
        surrender.setDisable(true);
        judge.setDisable(true);
        player1Ready = false;
        player2Ready = false;
        begin = false;
        turn = Stone.Black;
        boardController.setTimer(player1TimerController, player2TimerController);

        Image image = new Image("resources/image/bg004.jpg",1161,700,false,true);
        BackgroundSize backgroundSize = new BackgroundSize(1161, 700, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        gamePane.setBackground(background);

        Image image2 = new Image("resources/image/bg009.jpg",371,200,false,true);
        BackgroundSize backgroundSize2 = new BackgroundSize(371, 200, true, true, true, false);
        BackgroundImage backgroundImage2 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize2);
        Background background2 = new Background(backgroundImage2);
        playerPane.setBackground(background2);

        Image image3 = new Image("resources/image/bg012.jpg",484,123,false,true);
        BackgroundSize backgroundSize3 = new BackgroundSize(484, 123, true, true, true, false);
        BackgroundImage backgroundImage3 = new BackgroundImage(image3, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize3);
        Background background3 = new Background(backgroundImage3);
        scorePane.setBackground(background3);
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
        if(Client.offlineMode){
            turn = Stone.Black;
            boardController.setColor(Stone.Black);
            return;
        }
        /********** release ******/
        if ((room.getPlayer1() != null || room.getPlayer1().isEmpty()) && room.getPlayer1() == Client.getUser().getAccount()) {
            roomOwner = true;
            boardController.setColor(Stone.White);
        } else {
            roomOwner = false;
            boardController.setColor(Stone.Black);
        }
        /********** release ******/
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
        gameResultShow.setVisible(false);
        boardController.clear();
        chatBox.refresh();
        chatBoxController.clear();
        player1TimerController.stop();
        player2TimerController.stop();
    }

    @FXML
    private void ready() {
        if(Client.offlineMode){
            /*************** test *************/
            gameResultShow.setVisible(false);
            if (ready.isSelected()) {
                player1Ready = true;
                String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
                System.out.println("ready request msg: " + msg);
                Connect.send(msg);
                ready.setText("取消准备");
                gameStart();
            } else {
                player1Ready = false;
                String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
                System.out.println("ready request msg: " + msg);
                Connect.send(msg);
                ready.setText("准备");
            }
            return;
            /*************** test *************/
        }
        /***************** release **************/
        gameResultShow.setVisible(false);
        if (ready.isSelected()) {
            player1Ready = true;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready request msg: " + msg);
            Connect.send(msg);
            ready.setText("取消准备");
        } else {
            player1Ready = false;
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready request msg: " + msg);
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
                String msg = Encoder.gameOverRequest(room.getId(), diff, -diff, Type.GameResult.WIN);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            } else {
                String msg = Encoder.gameOverRequest(room.getId(), diff, -diff, Type.GameResult.LOSE);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            }
        }
    }

    public void showGameResult() {
        player1TimerController.pause();
        player2TimerController.pause();
        ready.setDisable(false);
        if ((gameResult & 0x40) != 0) {
            // escape
            if (roomOwner ^ gameResult == Type.GameResult.PLAYER2_ESCAPE) {
                // self
                Client.getUser().setRoom(0);
                Client.getUser().setState(Type.UserState.IDLE);
                Client.getUser().updateGameDate(score);
                Client.updateUser();
                return;
            } else {
                gameResultShow.setText("对方逃跑，你赢了！");
            }
        } else if ((gameResult & 0x20) != 0) {
            // surrender
            if (roomOwner ^ gameResult == Type.GameResult.PLAYER2_SURRENDER) {
                // self
                gameResultShow.setText("你投降了，对方赢了！");
            } else {
                gameResultShow.setText("对方投降，你赢了！");
            }
        } else if ((gameResult & 0x10) != 0) {
            if (roomOwner ^ gameResult == Type.GameResult.PLAYER2_OVERTIME) {
                gameResultShow.setText("你超时了，对方赢了！");
            } else {
                gameResultShow.setText("对方超时，你赢了！");
            }
        } else if (gameResult == Type.GameResult.WIN) {
            gameResultShow.setText("你赢了！");
        } else if (gameResult == Type.GameResult.LOSE) {
            gameResultShow.setText("你输了！");
        } else {
            gameResultShow.setText("双方打平，平局！");
        }
        if ((gameResult ^ 1) == 0 || ((gameResult & 0xF0) != 0) && (roomOwner ^ (gameResult & 1) == 0)) {
            gameResultShow.setTextFill(Color.color(0.9, 0.2, 0.2));
        } else {
            gameResultShow.setTextFill(Color.color(0.3, 0.75, 0.3));
        }
        gameResultShow.setVisible(true);
        Client.getUser().updateGameDate(score);
        Client.getUser().setState(Type.UserState.READY);
        Client.updateUser();
        room.setState(Type.RoomState.READY);
        Client.updateRoom(room, Type.UpdateRoom.STATE_CHANGE);
    }

    public void place(int x, int y, int color) {
        boardController.place(x, y, color);
    }

    public void kill(ArrayList<Stone> deadList) {
        Board.addDead(deadList);
        boardController.remove();
    }

    public void overTime() {
        if (roomOwner) {
            ArrayList<Number> point = boardController.getPlayerPoint();
            double p1 = (double) point.get(0);
            double p2 = (double) point.get(1);
            double diff = p1 - p2;
            boolean lose = player1TimerController.getPeriodTimes() == 0;
            if (diff < 0.001) {
                p1 = lose ? -6.0 : 6.0;
                p2 = lose ? 6.0 : -6.0;
            } else if (diff > 0) {
                p1 = lose ? -3.0 : 6.0;
                p2 = lose ? 6.0 : -3.0;
            } else {
                p1 = lose ? diff - 6.0 : -diff;
                p2 = lose ? -diff : diff - 6.0;
            }
            String msg = Encoder.gameOverRequest(room.getId(), p1, p2, lose ? Type.GameResult.PLAYER1_OVERTIME : Type.GameResult.PLAYER2_OVERTIME);
            Connect.send(msg);
            System.out.println("game result msg: " + msg);
        }
    }

    @FXML
    private void surrender() {
        ArrayList<Number> point = boardController.getPlayerPoint();
        double p1 = (double) point.get(0);
        double p2 = (double) point.get(1);
        double diff = p1 - p2;
        if (diff < 0.001) {
            p1 = roomOwner ? -9.0 : 6.0;
            p2 = roomOwner ? 6.0 : -9.0;
        } else if (diff > 0) {
            p1 = roomOwner ? -6.0 : 6.0;
            p2 = roomOwner ? 6.0 : -6.0;
        } else {
            p1 = roomOwner ? diff - 9.0 : -diff;
            p2 = roomOwner ? -diff : diff - 9.0;
        }
        String msg = Encoder.gameOverRequest(room.getId(), p1, p2, roomOwner ? Type.GameResult.PLAYER1_SURRENDER : Type.GameResult.PLAYER2_SURRENDER);
        Connect.send(msg);
        System.out.println("game result msg: " + msg);
    }

    public void escape() {
        ArrayList<Number> point = boardController.getPlayerPoint();
        double p1 = (double) point.get(0);
        double p2 = (double) point.get(1);
        double diff = p1 - p2;
        if (diff < 0.001) {
            p1 = roomOwner ? -15.0 : 6.0;
            p2 = roomOwner ? 6.0 : -15.0;
        } else if (diff > 0) {
            p1 = roomOwner ? -12.0 : 6.0;
            p2 = roomOwner ? 6.0 : -12.0;
        } else {
            p1 = roomOwner ? diff - 15.0 : -diff;
            p2 = roomOwner ? -diff : diff - 15.0;
        }
        String msg = Encoder.gameOverRequest(room.getId(), p1, p2, roomOwner ? Type.GameResult.PLAYER1_ESCAPE : Type.GameResult.PLAYER2_ESCAPE);
        Connect.send(msg);
        System.out.println("game result msg: " + msg);
    }

    public void setReady(boolean player1, boolean player2) {
        player1Ready = player1;
        player2Ready = player2;
        if (player1Ready && player2Ready) {
            gameStart();
        }
    }

    public boolean isBegin() {
        return begin;
    }

    public boolean isRoomOwner() {
        return roomOwner;
    }

    public int getTurn() {
        return turn;
    }

    public void takeTurns() {
        turn = -turn;
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
    public void judge() {
        String msg = Encoder.judgeRequest(room.getId(), roomOwner);
        Connect.send(msg);
        System.out.println("judge request msg: " + msg);
    }

    @FXML
    public void judgeEnable() {
        judge.setDisable(false);
    }

    public void judgeFromOpponent() {
        int res = JOptionPane.showConfirmDialog(null, "对方请求提前判子\n请问您是否同意？", "请求", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            gameOver();
        }
    }

    public void setGameResult(int result) {
        this.gameResult = result;
    }

    public void setScore(int score) {
        this.score = score;
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
        String msg = Encoder.roomMessageRequest(room.getId(), inputField.getText());
        Connect.send(msg);
        inputField.clear();
        send.setDisable(true);
    }

    public ChatBox getChatBoxController() {
        return chatBoxController;
    }
}
