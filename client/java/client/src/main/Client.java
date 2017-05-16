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

package src.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.main.communication.Connect;
import src.main.communication.Encoder;
import src.main.view.*;
import src.util.MessageQueue;
import src.util.UserComparator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Client extends Application {
    public static boolean offlineMode;
    private static User user;
    private Connect connect = null;

    private Stage createRoomStage;
    private Stage gameStage;
    private Stage lobbyStage = null;
    private Stage signupStage = null;
    private Stage loginStage;

    private static LoginController loginController;
    private static LobbyController lobbyController;
    private static GameController gameController;
    private static SignupController signupController;

    public static MessageQueue<Room> rooms = new MessageQueue<>();
    public static MessageQueue<User> players = new MessageQueue<>();
    public static MessageQueue<String> chatMessages = new MessageQueue<>();
    public static MessageQueue<String> privateChatMessages = new MessageQueue<>();

    private static ObservableList<Room> roomData = FXCollections.observableArrayList();
    private static ObservableList<User> playerData = FXCollections.observableArrayList();
    private static ObservableList<String> messageData = FXCollections.observableArrayList();
    private static ObservableList<String> privateMessageData = FXCollections.observableArrayList();

    public static Map<String, User> playersMap = new HashMap<>();
    public static Map<Integer, Room> roomsMap = new HashMap<>();

    private long checkDalay = 10;
    private long keepAliveDalay = 3000;
    private long lastTimeCheck;
    private Thread receiveThread;

    private Thread keepAliveThread = new Thread(new Runnable() {
        @Override
        public void run() {
            lastTimeCheck = System.currentTimeMillis();
            System.out.println("监听服务器连接线程启动！");
            while (true) {
                if (System.currentTimeMillis() - lastTimeCheck > keepAliveDalay) {
                    try {
                        Connect.sendMsgToserver(Encoder.keepAliveRequest());
                    } catch (IOException e) {
                        // e.printStackTrace();
                        System.out.println("与服务器连接失败!");
                        Alert error = new Alert(Alert.AlertType.ERROR, "与服务器连接失败!\\n正在尝试重新连接...");
                        error.show();
                        reConnect();
                    } catch (NullPointerException e) {
                        System.out.println("与服务器连接失败！");
                        Alert error = new Alert(Alert.AlertType.ERROR, "与服务器连接失败!\\n正在尝试重新连接...");
                        error.show();
                        reConnect();
                    }
                    lastTimeCheck = System.currentTimeMillis();
                } else {
                    try {
                        Thread.currentThread().sleep(checkDalay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void reConnect() {
            boolean print = true;
            boolean print2 = true;
            while (!isServerStart()) {
                try {
                    connect.setSocket(new Socket(connect.getIP(), connect.getPORT()));
                    InputStream is = connect.socket.getInputStream();
                    OutputStream os = connect.socket.getOutputStream();
                    connect.setIs(is);
                    connect.setOs(os);
                    System.out.println("重新连接服务器成功！");
                    if (!receiveThread.isAlive())
                        receiveThread.start();
                    if (!messageThread.isAlive())
                        messageThread.start();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "重新连接服务器成功！");
                    alert.show();
                } catch (UnknownHostException e) {
                    if (print2)
                        System.out.println("客户端异常！");
                    print2 = false;
                    // e.printStackTrace();
                } catch (IOException e) {
                    if (print)
                        System.out.println("正在尝试重新连接服务器...");
                    print = false;
                }
            }

        }

        private boolean isServerStart() {
            Socket socket = connect.getSocket();
            try {
                socket.sendUrgentData(0);
                return true;
            } catch (IOException e) {

            } catch (NullPointerException e) {

            }
            return false;
        }
    });

    private Thread chatThread = new Thread(new Runnable() {

        @Override
        public void run() {
            System.out.println("聊天线程启动！");
            while (true) {
                if (!chatMessages.isEmpty())
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (!chatMessages.isEmpty()) {
                                    messageData.add(chatMessages.remove());
                                    lobbyController.getChatBoxController().getChatBox().scrollTo(messageData.size() - 1);
                                }
                            }
                        });

                    } catch (Exception e) {

                    }
                if (!privateChatMessages.isEmpty())
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (!privateChatMessages.isEmpty()) {
                                    privateMessageData.add(privateChatMessages.remove());
                                    gameController.getChatBoxController().getChatBox().scrollTo(privateMessageData.size() - 1);
                                }
                            }
                        });

                    } catch (Exception e) {

                    }
            }
        }

    });
    private Thread messageThread = new Thread(new Runnable() {

        @Override
        public void run() {
            System.out.println("监听信息线程启动！");
            while (true) {
                // players
                if (!players.isEmpty()) {
                    adjustPlayer(players.remove());
                }
                // rooms
                if (!rooms.isEmpty()) {
                    adjustRoom(rooms.remove());
                }

            }
        }
    });

    public Client() {
        loginStage = new Stage();
        loginStage.setTitle("MicroOnlineGo - 登录");
        loginStage.setResizable(false);
        FXMLLoader loader0 = new FXMLLoader();
        loader0.setLocation(getClass().getResource("view/Login.fxml"));
        Pane loginPane = null;
        try {
            loginPane = loader0.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene loginScene = new Scene(loginPane);
        loginStage.setScene(loginScene);
        loginController = loader0.getController();
        loginController.setClient(this);

        lobbyStage = new Stage();
        lobbyStage.setTitle("MicroOnlineGo - 大厅");
        lobbyStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Lobby.fxml"));
        Pane lobbyPane = null;
        try {
            lobbyPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene lobbyScene = new Scene(lobbyPane);
        lobbyStage.setScene(lobbyScene);
        lobbyController = loader.getController();
        lobbyController.setClient(this);
        lobbyController.setAssociation();
        lobbyStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (user.getState() != Type.UserState.IDLE) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("提示");
                    if (user.getState() == Type.UserState.READY) {
                        alert.setHeaderText("您正在房间中，请先退出房间！");
                    } else {
                        alert.setHeaderText("您正在游戏中，请先结束游戏！");
                    }
                    alert.initOwner(lobbyStage);
                    alert.initModality(Modality.WINDOW_MODAL);
                    alert.show();
                    event.consume();
                    return;
                }
                Connect.send(Encoder.logoutRequest());
                playerData.clear();
                roomData.clear();
                playersMap.clear();
                roomsMap.clear();
            }
        });

        gameStage = new Stage();
        gameStage.setResizable(false);
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("view/Game.fxml"));
        Pane gamePane = null;
        try {
            gamePane = loader2.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene gameScene = new Scene(gamePane);
        gameStage.setScene(gameScene);
        gameController = loader2.getController();
        gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (offlineMode) {
                    if (gameController.isBegin()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText("提示");
                        alert.setContentText("您正在游戏中，确认要退出游戏吗？");
                        alert.initOwner(gameStage);
                        alert.initModality(Modality.WINDOW_MODAL);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.CANCEL) {
                            event.consume();
                            return;
                        }
                    }
                    gameController.clear();
                    gameController.stopMusic();
                    return;
                }
                Room room = gameController.getRoom();
                if (room == null) {
                    user.setState(Type.UserState.IDLE);
                    user.setRoom(0);
                    updateUser();
                    gameController.stopMusic();
                    lobbyController.playMusic();
                    return;
                }
                System.out.println("room playernum: " + room.playerNumber());
                System.out.println("room p1: " + room.getPlayer1());
                System.out.println("room p2: " + room.getPlayer2());
                if (user.getState() == Type.UserState.GAMING) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("提示");
                    alert.setContentText("您正在游戏中，确认要退出游戏吗？\n强制退出将会损失较多积分");
                    alert.initOwner(gameStage);
                    alert.initModality(Modality.WINDOW_MODAL);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        gameController.escape();
                        gameController.clear();
                        gameController.leaveRoom();
                    } else {
                        event.consume();
                        return;
                    }
                } else if (room.playerNumber() == 2) {
                    gameController.leaveRoom();
                } else {
                    updateRoom(room, Type.UpdateRoom.DESTROY);
                }
                user.setState(Type.UserState.IDLE);
                user.setRoom(0);
                updateUser();
                gameController.clear();
                lobbyController.playMusic();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        gotoLogin();
        connect = new Connect();
        receiveThread = connect.getReceiveThread();
        /*
         * keepAliveThread.setDaemon(true);
         * keepAliveThread.start();
         */
        if (Connect.hasConnect()) {
            receiveThread.setDaemon(true);
            receiveThread.start();
            messageThread.setDaemon(true);
            messageThread.start();
            chatThread.setDaemon(true);
            chatThread.start();
        }
    }

    public void gotoLogin() {
        loginController.resetAccount();
        loginController.resetPassword();
        loginStage.show();
        if (!loginController.isPlayingMusic()) {
            loginController.playMusic();
        }
    }

    public void gotoSignup() {
        signupStage = new Stage();
        signupStage.setTitle("MicroOnlineGo - 注册");
        signupStage.setResizable(false);
        signupController = (SignupController) changeStage("view/Signup.fxml", signupStage);
        signupController.setClient(this);
    }

    public void gotoLobby() {
        lobbyStage.show();
        lobbyController.fetchLobbyInfo();
        loginController.stopMusic();
        lobbyController.playMusic();
    }

    public void gotoCreateRoom() {
        createRoomStage = new Stage();
        createRoomStage.setTitle("MicroOnlineGo - 创建房间");
        createRoomStage.setResizable(false);
        CreateRoomController createRoomController = (CreateRoomController) changeStage("view/CreateRoom.fxml", createRoomStage);
        createRoomController.setClient(this);
    }

    public void backToLobby() {
        createRoomStage.close();
    }

    public void gotoGame(Room room) {
        gameController.initRoom(room);
        gameController.getChatBoxController().setItems(privateMessageData);
        gameStage.setTitle("MicroOnlineGo - 房间 " + Integer.toString(room.getId()) + " " + room.getName());
        gameStage.show();
        lobbyController.stopMusic();
        if (loginController.isPlayingMusic()) {
            loginController.stopMusic();
        }
        gameController.playMusic();
    }

    private Initializable changeStage(String fxml, Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        try {
            Pane pane = loader.load();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return (Initializable) loader.getController();
        }
    }

    static public void updateUser() {
        String msg = Encoder.updatePlayerRequest();
        Connect.send(msg);
        System.out.println("update player msg: " + msg);
    }

    static public void updateRoom(Room room, int type) {
        String msg = Encoder.updateRoomRequest(room, type);
        Connect.send(msg);
        System.out.println("update room msg: " + msg);
    }

    public static void adjustPlayer(User player) {
        if (playerData.contains(player)) {
            int index = playerData.indexOf(player);
            playerData.set(index, player);
        } else {
            playerData.add(player);
        }
        playerData.sort(new UserComparator());
        playersMap.put(player.getAccount(), player);
    }

    public static void removePlayer(String account) {
        playerData.remove(playersMap.get(account));
        playersMap.remove(account);
    }

    public static void adjustRoom(Room room) {
        System.out.println("player1: " + room.getPlayer1() + ", p2: " + room.getPlayer2());
        if (room.getId() == user.getRoom()) {
            gameController.updatePlayerInfo(room);
        }
        if (roomData.contains(room)) {
            int index = roomData.indexOf(room);
            roomData.set(index, room);
        } else {
            roomData.add(room);
        }
        System.out.println("player1: " + room.getPlayer1() + ", p2: " + room.getPlayer2());
        roomsMap.put(room.getId(), room);
    }

    public static void removeRoom(int id) {
        roomData.remove(roomsMap.get(id));
        roomsMap.remove(id);
    }

    public Connect getConnect() {
        return connect;
    }

    public void resetConnect() {
        connect = new Connect();
    }

    public static void setUser(User self) {
        user = self;
    }

    public static User getUser() {
        return user;
    }

    public Stage getCreateRoomStage() {
        return createRoomStage;
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public Stage getLobbyStage() {
        return lobbyStage;
    }

    public Stage getSignupStage() {
        return signupStage;
    }

    public Stage getLoginStage() {
        return loginStage;
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

    public ObservableList<String> getMessageData() {
        return messageData;
    }

    public ObservableList<String> getPrivateMessageData() {
        return privateMessageData;
    }

    public static MessageQueue<String> getChatMessages() {
        return chatMessages;
    }

    public static MessageQueue<String> getPrivateChatMessages() {
        return privateChatMessages;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static LobbyController getLobbyController() {
        return lobbyController;
    }

    public static GameController getGameController() {
        return gameController;
    }

}
