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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import src.main.Client;
import src.main.Room;
import src.main.Type;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;
import src.util.MessageQueue;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    private Client client;

    @FXML
    private TextField inputField;
    @FXML
    private Button sendbtn;
    @FXML
    private Button createRoomBtn;
    @FXML
    private Button autoMatch;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private TableView<Room> roomList;
    @FXML
    private TableColumn<Room, String> roomIdCol;
    @FXML
    private TableColumn<Room, String> roomNameCol;
    @FXML
    private TableColumn<Room, String> player1Col;
    @FXML
    private TableColumn<Room, String> player2Col;
    @FXML
    private TableColumn<Room, String> roomStateCol;
    @FXML
    private TableColumn<Room, String> configCol;
    @FXML
    private TableView<User> playerList;
    @FXML
    private TableColumn<User, String> nicknameCol;
    @FXML
    private TableColumn<User, String> levelCol;
    @FXML
    private TableColumn<User, String> winCol;
    @FXML
    private TableColumn<User, String> loseCol;
    @FXML
    private TableColumn<User, String> playerStateCol;
    @FXML
    private Slider volumeSlider;
    @FXML
    private AnchorPane lobbyPane;
    @FXML
    private ChatBox chatBoxController;
    private ObservableList<Room> roomData = FXCollections.observableArrayList();
    private ObservableList<User> playerData = FXCollections.observableArrayList();

    private MediaPlayer music;

    public static MessageQueue<Room> rooms = new MessageQueue<>();
    public static MessageQueue<User> players = new MessageQueue<>();
    public static MessageQueue<String> chatMessage = new MessageQueue<>();

    private boolean canSitDown;

    public LobbyController() {
        music = new MediaPlayer(new Media(getClass().getResource("/resources/music/bgm1.mp3").toExternalForm()));
        music.setOnEndOfMedia(new Runnable() {
            public void run() {
                music.seek(Duration.ZERO);
            }
        });
    }

    @FXML
    private void logout() {
        Connect.send(Encoder.logoutRequest());
        client.getLobbyStage().close();
        client.gotoLogin();
        client.getPlayerData().clear();
        client.getRoomData().clear();
        client.playersMap.clear();
        client.roomsMap.clear();
        chatBoxController.clear();
        music.stop();
    }

    @FXML
    private void chat() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = String.format(" (" + format.format(date) + "):");
        chatBoxController.sendMessage(Client.getUser().getNickname() + time + inputField.getText());
        String msg = Encoder.lobbyMessageRequest(inputField.getText());
        Connect.send(msg);
        inputField.clear();
        sendbtn.setDisable(true);
    }

    @FXML
    private void fastMatch() {
        // TODO: find a room that is most match
        if (Client.getUser().getState() != Type.UserState.IDLE) {
            JOptionPane.showMessageDialog(null, "您已经在房间中");
            return;
        }
        Room match = null;
        for (Room room : Client.roomsMap.values()) {
            if (room.hasSeat()) {
                match = room;
                Client.getUser().setRoom(match.getId());
                Client.getUser().setState(Type.UserState.READY);
                Client.updateUser();
                match.setState(Type.RoomState.READY);
                if (match.getPlayer1() == null || match.getPlayer1().isEmpty()) {
                    match.setPlayer1(Client.getUser().getAccount());
                    Client.updateRoom(room, Type.UpdateRoom.PLAYER1IN);
                } else {
                    match.setPlayer2(Client.getUser().getAccount());
                    Client.updateRoom(room, Type.UpdateRoom.PLAYER2IN);
                }
                break;
            }
        }
        if (match != null) {
            client.gotoGame(match);
        } else {
            int res = JOptionPane.showConfirmDialog(null, "暂时找不到匹配的房间哦，是否要自己创建一个呢？", "未找到匹配的房间", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                client.gotoCreateRoom();
            }
        }
    }

    @FXML
    private void gotoCreateRoom() {
        if (Client.getUser().getState() != Type.UserState.IDLE) {
            JOptionPane.showMessageDialog(null, "您已经在房间中");
            return;
        }
        client.gotoCreateRoom();
    }

    @FXML
    private void clickRoom(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Room room = roomList.getSelectionModel().getSelectedItem();
            System.out.println("click room id: "+room.getId()+" empty? "+room.hasSeat());
            if (!room.hasSeat()) {
                JOptionPane.showMessageDialog(null, "房间已经满人");
                return;
            }
            if (Client.getUser().getState() != Type.UserState.IDLE) {
                JOptionPane.showMessageDialog(null, "您已经在房间中");
                return;
            }
            Connect.send(Encoder.sitdownRequest(room.getId()));
            Connect.waitForRec(Type.Response.SITDOWN_SUCCESS, Type.Response.SITDOWN_FAILED);
            if (canSitDown) {
                Client.getUser().setRoom(room.getId());
                Client.getUser().setState(Type.UserState.READY);
                Client.updateUser();
                room.setState(Type.RoomState.READY);
                if (room.getPlayer1() == null || room.getPlayer1().isEmpty()) {
                    room.setPlayer1(Client.getUser().getAccount());
                    Client.updateRoom(room, Type.UpdateRoom.PLAYER1IN);
                } else {
                    room.setPlayer2(Client.getUser().getAccount());
                    Client.updateRoom(room, Type.UpdateRoom.PLAYER2IN);
                }
                client.gotoGame(room);
            } else {
                JOptionPane.showMessageDialog(null, "房间已经满人");
            }
        }
    }

    public void setSitDown(boolean canSitDown) {
        this.canSitDown = canSitDown;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void fetchLobbyInfo() {
        Connect.send(Encoder.fetchPlayersRequest());
        Connect.send(Encoder.fetchRoomsRequest());
    }

    public void playMusic() {
        music.play();
    }

    public void stopMusic() {
        music.stop();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("resources/image/bg004.jpg", 1149, 660, false, true);
        BackgroundSize backgroundSize = new BackgroundSize(1149, 660, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        lobbyPane.setBackground(background);
        inputField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                String text = inputField.getText();
                if (text == null || "".equals(text) || text.length() == 0) {
                    sendbtn.setDisable(true);
                } else {
                    sendbtn.setDisable(false);
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

    public void setAssociation() {
        chatBoxController.setItems(client.getMessageData());
        roomList.setItems(client.getRoomData());
        roomIdCol.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        roomNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        player1Col.setCellValueFactory(cellData -> cellData.getValue().getPlayer1Property());
        player2Col.setCellValueFactory(cellData -> cellData.getValue().getPlayer2Property());
        roomStateCol.setCellValueFactory(cellData -> cellData.getValue().getStatesProperty());
        configCol.setCellValueFactory(cellDate -> cellDate.getValue().getConfigProperty());
        playerList.setItems(client.getPlayerData());
        nicknameCol.setCellValueFactory(cellData -> cellData.getValue().getNicknameProperty());
        levelCol.setCellValueFactory(cellData -> cellData.getValue().getLevelProperty());
        winCol.setCellValueFactory(cellData -> cellData.getValue().getWinProperty());
        loseCol.setCellValueFactory(cellData -> cellData.getValue().getLoseProperty());
        playerStateCol.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
    }

    public void addPlayer(User user) {
        user.setPriority(1);
        this.playerList.getItems().add(user);
    }

    public void addRoom(Room room) {
        this.roomList.getItems().add(room);
    }

    public ObservableList<Room> getRoomData() {
        return roomData;
    }

    public ObservableList<User> getPlayerData() {
        return playerData;
    }

    public static MessageQueue<Room> getRooms() {
        return rooms;
    }

    public static MessageQueue<User> getPlayers() {
        return players;
    }

    public static MessageQueue<String> getChatMessage() {
        return chatMessage;
    }

    public ChatBox getChatBoxController() {
        return chatBoxController;
    }

}
