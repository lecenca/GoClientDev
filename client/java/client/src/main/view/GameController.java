/******************************************************************************
 * This file is licensed under the Unlicense. See License.txt for details.
 *
 * Author:
 *   Alinshans (https://github.com/Alinshans/GoClientDev)
 *   zengxingbin (https://github.com/zengxingbin/GoClientDev)
 *   lecenca (https://github.com/lecenca/GoClientDev)
 *
 * Copyright (c) 2017. All rights reserved.
 *****************************************************************************/

package src.main.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import src.main.*;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Room room;

    private boolean roomOwner;
    private boolean player1Ready;
    private boolean player2Ready;
    private boolean begin;
    private int turn;

    private int gameResult;
    private double score;
    private double player1Point;
    private double player2Point;

    private MediaPlayer music;

    // Pane
    @FXML
    private AnchorPane gamePane;
    @FXML
    private HBox playerPane;
    @FXML
    private AnchorPane scorePane;
    @FXML
    private ChessBoard boardController;

    // Player information label
    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;
    @FXML
    private Label player1Level;
    @FXML
    private Label player2Level;
    @FXML
    private Label player1Record;
    @FXML
    private Label player2Record;

    // Game rule label
    @FXML
    private Label komi;
    @FXML
    private Label mainTime;
    @FXML
    private Label periodTime;
    @FXML

    // Game process label
    private Label player1OverTimeRemain;
    @FXML
    private Label player2OverTimeRemain;
    @FXML
    private Label player1Kill;
    @FXML
    private Label player2Kill;
    @FXML
    private Timer player1TimerController;
    @FXML
    private Timer player2TimerController;

    // Control button
    @FXML
    private ToggleButton ready;
    @FXML
    private Button surrender;
    @FXML
    private Button judge;
    @FXML
    private ToggleButton step;
    @FXML
    private ToggleButton axis;

    // Chat windows
    @FXML
    private ChatBox chatBoxController;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private TextField inputField;
    @FXML
    private Button send;


    @FXML
    private Label gameResultShow;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label[] axisLable = new Label[38];

    public GameController() {
        music = new MediaPlayer(new Media(getClass().getResource("/resources/music/bgm2.mp3").toExternalForm()));
        music.setOnEndOfMedia(new Runnable() {
            public void run() {
                music.seek(Duration.ZERO);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAxis();
        hideAxis();
        gameResultShow.setVisible(false);
        player1TimerController.setPlayerOverTimeRemain(player1OverTimeRemain);
        player2TimerController.setPlayerOverTimeRemain(player2OverTimeRemain);

        Image image = new Image("resources/image/bg004.jpg", 1160, 700, false, true);
        BackgroundSize backgroundSize = new BackgroundSize(1161, 700, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        gamePane.setBackground(background);

        Image image2 = new Image("resources/image/bg014.jpg", 420, 200, false, true);
        BackgroundSize backgroundSize2 = new BackgroundSize(390, 200, true, true, true, false);
        BackgroundImage backgroundImage2 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize2);
        Background background2 = new Background(backgroundImage2);
        playerPane.setBackground(background2);

        Image image3 = new Image("resources/image/bg013.jpg", 470, 125, false, true);
        BackgroundSize backgroundSize3 = new BackgroundSize(484, 123, true, true, true, false);
        BackgroundImage backgroundImage3 = new BackgroundImage(image3, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize3);
        Background background3 = new Background(backgroundImage3);
        scorePane.setBackground(background3);
        inputField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String text = inputField.getText();
                if (text == null || "".equals(text) || text.length() == 0) {
                    send.setDisable(true);
                } else {
                    send.setDisable(false);
                    if (event.getCode() == KeyCode.ENTER)
                        chat();
                }
            }
        });

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        music.setVolume(volumeSlider.getValue());
                    }
                });
            }
        });
    }

    private void initAxis() {
        for (int i = 0; i < 38; ++i) {
            axisLable[i] = new Label();
            gamePane.getChildren().add(axisLable[i]);
        }
        for (int i = 1; i <= 19; ++i) {
            axisLable[i - 1].setText(String.valueOf(i));
            axisLable[i - 1].setTextFill(Color.color(0.9, 0.1, 0.1));
            axisLable[i - 1].setLayoutX(25 + i * 30);
            axisLable[i - 1].setLayoutY(612.0);
            axisLable[i - 1].setPrefSize(15, 15);
            axisLable[i - 1].setAlignment(Pos.BASELINE_CENTER);
        }
        for (int i = 0; i < 19; ++i) {
            axisLable[i + 19].setText(String.valueOf((char) ('A' + i)));
            axisLable[i + 19].setTextFill(Color.color(0.9, 0.1, 0.1));
            axisLable[i + 19].setLayoutX(38);
            axisLable[i + 19].setLayoutY(592 - i * 30);
            axisLable[i + 19].setPrefSize(15, 15);
            axisLable[i + 19].setAlignment(Pos.BASELINE_CENTER);
        }
    }

    private void hideAxis() {
        for (Label label : axisLable) {
            label.setVisible(false);
        }
    }

    private void showAxis() {
        for (Label label : axisLable) {
            label.setVisible(true);
        }
    }

    public void initRoom(Room room) {
        this.room = room;
        resetState();
        initRuleLable();
        if (!Client.offlineMode) {
            updatePlayerInfo(room);
        }
        resetGameProcessLabel();
        step.setSelected(false);
        axis.setSelected(false);
        volumeSlider.setValue(1.0);
        if (Client.offlineMode) {
            ready.setText("开始对局");
        } else {
            ready.setText("准备");
            if (!room.getPlayer1().isEmpty() && room.getPlayer1() == Client.getUser().getAccount()) {
                roomOwner = true;
                boardController.setColor(Stone.White);
            } else {
                roomOwner = false;
                boardController.setColor(Stone.Black);
            }
        }
    }

    private void resetState() {
        player1Ready = false;
        player2Ready = false;
        begin = false;
        turn = Stone.Black;
        ready.setDisable(false);
        ready.setSelected(false);
        judge.setDisable(true);
        surrender.setDisable(true);
    }

    private void initRuleLable() {
        komi.setText(room.getKomiString());
        mainTime.setText(room.getMainTime() + "分");
        periodTime.setText(room.getPeriodTime() + "秒" + room.getPeriodTimes() + "次");
    }

    private void resetGameProcessLabel() {
        player1OverTimeRemain.setText(room.getPeriodTimes() + "次");
        player2OverTimeRemain.setText(room.getPeriodTimes() + "次");
        player1Kill.setText("0次");
        player2Kill.setText("0次");
        player1TimerController.init(room.getMainTime(), room.getPeriodTime(), room.getPeriodTimes());
        player2TimerController.init(room.getMainTime(), room.getPeriodTime(), room.getPeriodTimes());
        player1TimerController.stop();
        player2TimerController.stop();
    }

    @FXML
    public void clear() {
        resetGameData();
        ready.setSelected(false);
        player1Ready = false;
        player2Ready = false;
        begin = false;
        step.setSelected(false);
        axis.setSelected(false);
        gameResultShow.setVisible(false);
        chatBox.refresh();
        chatBoxController.clear();
        music.stop();
    }

    private void resetGameData() {
        boardController.clear();
        gameResultShow.setVisible(false);
        resetGameProcessLabel();
    }

    @FXML
    private void ready() {
        if (Client.offlineMode) {
            gameResultShow.setVisible(false);
            if (ready.isSelected()) {
                resetGameData();
                gameStart();
            } else {
                gameOver();
            }
            return;
        }
        /***************** online **************/
        gameResultShow.setVisible(false);
        if (ready.isSelected()) {
            resetGameData();
            if (roomOwner) {
                player1Ready = true;
            } else {
                player2Ready = true;
            }
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready request msg: " + msg);
            Connect.send(msg);
            ready.setText("取消准备");

        } else {
            if (roomOwner) {
                player1Ready = false;
            } else {
                player2Ready = false;
            }
            String msg = Encoder.readyRequest(room.getId(), player1Ready, player2Ready);
            System.out.println("ready request msg: " + msg);
            Connect.send(msg);
            ready.setText("准备");
        }
        /***************** online **************/
    }

    @FXML
    public void gameStart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                begin = true;
                player2TimerController.start();
                if (Client.offlineMode) {
                    ready.setText("结束对局");
                    return;
                }
                ready.setText("游戏中");
                ready.setDisable(true);
                surrender.setDisable(false);
                Client.getUser().setState(Type.UserState.GAMING);
                Client.updateUser();
                room.setState(Type.RoomState.GAMING);
                Client.updateRoom(room, Type.UpdateRoom.STATE_CHANGE);
            }
        });
    }

    @FXML
    public void gameOver() {
        resetState();
        getPlayerPoint();
        double diff = Math.abs(player1Point - player2Point);
        if (Client.offlineMode) {
            ready.setText("开始对局");
            ready.setSelected(false);
            if (diff < 0.01) {
                gameResultShow.setText("对局结束，双方打平！");
            } else {
                int p = (int) diff;
                diff -= (double) p;
                gameResultShow.setText("对局结束，" + (player1Point > player2Point ? "白" : "黑") + "胜"
                        + p + "目" + (Math.abs(diff - 0.5) < 0.01 ? "半" : ""));
            }
            gameResultShow.setTextFill(Color.color(0.2, 0.9, 0.2));
            gameResultShow.setVisible(true);
            player1TimerController.stop();
            player2TimerController.stop();
            return;
        }
        if (roomOwner) {
            if (diff < 0.01) {
                String msg = Encoder.gameOverRequest(room.getId(), 0.0, 0.0, Type.GameResult.DRAW);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            } else if (player1Point > player2Point) {
                String msg = Encoder.gameOverRequest(room.getId(), diff, -diff, Type.GameResult.WIN);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            } else {
                String msg = Encoder.gameOverRequest(room.getId(), -diff, diff, Type.GameResult.LOSE);
                Connect.send(msg);
                System.out.println("game result msg: " + msg);
            }
        }
    }

    public void showGameResult() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                player1TimerController.stop();
                player2TimerController.stop();
                ready.setText("准备");
                resetState();
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
                    int p = (int) score;
                    score -= (double) p;
                    gameResultShow.setText("对局结束，你赢了" + Math.abs(p) + "目" + (Math.abs(score - 0.5) < 0.01 ? "半" : "") + "！");
                } else if (gameResult == Type.GameResult.LOSE) {
                    int p = (int) score;
                    score -= (double) p;
                    gameResultShow.setText("对局结束，你输了" + Math.abs(p) + "目" + (Math.abs(score - 0.5) < 0.01 ? "半" : "") + "！");
                } else {
                    gameResultShow.setText("对局结束，双方打平！");
                }
                if ((gameResult ^ 1) == 0 || ((gameResult & 0xF0) != 0) && (roomOwner ^ (gameResult & 0x1) == 1)) {
                    gameResultShow.setTextFill(Color.color(0.9, 0.2, 0.2));
                } else {
                    gameResultShow.setTextFill(Color.color(0.2, 0.9, 0.2));
                }
                gameResultShow.setVisible(true);
                Client.getUser().updateGameDate(score);
                Client.getUser().setState(Type.UserState.READY);
                Client.updateUser();
                room.setState(Type.RoomState.READY);
                Client.updateRoom(room, Type.UpdateRoom.STATE_CHANGE);
            }
        });
    }

    public void getPlayerPoint() {
        ArrayList<Number> point = Core.scoring();
        player1Point = (double) point.get(0);
        player2Point = (double) point.get(1);
        if (room.getKomi() == Type.KOMI.THREE_FIVE) {
            player1Point += 3.5;
        } else if (room.getKomi() == Type.KOMI.SIX_FIVE) {
            player1Point += 6.5;
        }
    }

    public void playAction(int action, int x, int y, int color) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boardController.playAction(action, x, y, color);
            }
        });
    }

    public void overTime() {
        if (Client.offlineMode) {
            begin = false;
            ready.setText("开始对局");
            ready.setSelected(false);
            if (player1TimerController.getPeriodTimes() == 0) {
                gameResultShow.setText("白方超时，黑方胜利！");
            } else {
                gameResultShow.setText("黑方超时，白方胜利！");
            }
            gameResultShow.setTextFill(Color.color(0.9, 0.2, 0.2));
            gameResultShow.setVisible(true);
            player1TimerController.stop();
            player2TimerController.stop();
            return;
        }
        if (roomOwner) {
            getPlayerPoint();
            double diff = Math.abs(player1Point - player2Point);
            boolean lose = player1TimerController.getPeriodTimes() == 0;
            if (diff < 0.001) {
                player1Point = lose ? -6.0 : 6.0;
                player2Point = lose ? 6.0 : -6.0;
            } else if (player1Point > player2Point) {
                player1Point = lose ? -3.0 : 6.0;
                player2Point = lose ? 6.0 : -3.0;
            } else {
                player1Point = lose ? diff - 6.0 : -diff;
                player2Point = lose ? -diff : diff - 6.0;
            }
            String msg = Encoder.gameOverRequest(room.getId(), player1Point, player2Point,
                    lose ? Type.GameResult.PLAYER1_OVERTIME : Type.GameResult.PLAYER2_OVERTIME);
            Connect.send(msg);
            System.out.println("game result msg: " + msg);
        }
    }

    @FXML
    private void surrender() {
        getPlayerPoint();
        double diff = Math.abs(player1Point - player2Point);
        if (diff < 0.001) {
            player1Point = roomOwner ? -9.0 : 6.0;
            player2Point = roomOwner ? 6.0 : -9.0;
        } else if (diff > 0) {
            player1Point = roomOwner ? -6.0 : 6.0;
            player2Point = roomOwner ? 6.0 : -6.0;
        } else {
            player1Point = roomOwner ? diff - 9.0 : -diff;
            player2Point = roomOwner ? -diff : diff - 9.0;
        }
        String msg = Encoder.gameOverRequest(room.getId(), player1Point, player2Point,
                roomOwner ? Type.GameResult.PLAYER1_SURRENDER : Type.GameResult.PLAYER2_SURRENDER);
        Connect.send(msg);
        System.out.println("game result msg: " + msg);
    }

    public void escape() {
        getPlayerPoint();
        double diff = Math.abs(player1Point - player2Point);
        if (diff < 0.001) {
            player1Point = roomOwner ? -15.0 : 6.0;
            player2Point = roomOwner ? 6.0 : -15.0;
        } else if (diff > 0) {
            player1Point = roomOwner ? -12.0 : 6.0;
            player2Point = roomOwner ? 6.0 : -12.0;
        } else {
            player1Point = roomOwner ? diff - 15.0 : -diff;
            player2Point = roomOwner ? -diff : diff - 15.0;
        }
        String msg = Encoder.gameOverRequest(room.getId(), player1Point, player2Point,
                roomOwner ? Type.GameResult.PLAYER1_ESCAPE : Type.GameResult.PLAYER2_ESCAPE);
        Connect.send(msg);
        System.out.println("game result msg: " + msg);
    }

    public void leaveRoom() {
        System.out.println("room p1 in game: " + room.getPlayer1());
        System.out.println("room p2 in game: " + room.getPlayer2());
        room.setState(Type.RoomState.WATING);
        if (room.getPlayer1() == Client.getUser().getAccount()) {
            room.setPlayer1("");
            Client.updateRoom(room, Type.UpdateRoom.PLAYER1OUT);
        } else {
            room.setPlayer2("");
            Client.updateRoom(room, Type.UpdateRoom.PLAYER2OUT);
        }
    }

    public void setReady(boolean player1, boolean player2) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                player1Ready = player1;
                player2Ready = player2;
                if (player1Ready && player2Ready) {
                    gameStart();
                }
            }
        });
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

    public void reverseTurn() {
        if (turn == Stone.Black) {
            player2TimerController.pause();
            player1TimerController.start();
        } else {
            player1TimerController.pause();
            player2TimerController.start();
        }
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
    public void displayAxis() {
        if (axis.isSelected()) {
            showAxis();
        } else {
            hideAxis();
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int res = JOptionPane.showConfirmDialog(null, "对方请求提前判子\n请问您是否同意？", "请求", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    gameOver();
                }
            }
        });
    }

    public void setGameResult(int result) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameResult = result;
            }
        });
    }

    public void setScore(double point) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                score = point;
            }
        });
    }

    @FXML
    public void setPlayer1Kill(int times) {
        player1Kill.setText(times + "次");
    }

    @FXML
    public void setPlayer2Kill(int times) {
        player2Kill.setText(times + "次");
    }

    @FXML
    private void chat() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = String.format(" (" + format.format(date) + "):");
        chatBoxController.sendMessage(Client.getUser().getNickname() + time + inputField.getText());
        String msg = Encoder.roomMessageRequest(room.getId(), inputField.getText());
        Connect.send(msg);
        inputField.clear();
        send.setDisable(true);
    }

    public void playMusic() {
        music.play();
    }

    public void stopMusic() {
        music.stop();
    }

    public ChatBox getChatBoxController() {
        return chatBoxController;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void updatePlayerInfo(Room room) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setRoom(room);
                if (room.getPlayer1().isEmpty()) {
                    player1Name.setText("???");
                    player1Level.setText("?级");
                    player1Record.setText("?胜?负?平");
                } else {
                    User player1 = Client.playersMap.get(room.getPlayer1());
                    player1Name.setText(player1.getNickname());
                    player1Level.setText(player1.getLevelString());
                    player1Record.setText(player1.getWin() + "胜" + player1.getLose() + "负" + player1.getDraw() + "平");
                }
                if (room.getPlayer2().isEmpty()) {
                    player2Name.setText("???");
                    player2Level.setText("?级");
                    player2Record.setText("?胜?负?平");
                } else {
                    User player2 = Client.playersMap.get(room.getPlayer2());
                    player2Name.setText(player2.getNickname());
                    player2Level.setText(player2.getLevelString());
                    player2Record.setText(player2.getWin() + "胜" + player2.getLose() + "负" + player2.getDraw() + "平");
                }
            }
        });
    }

}
