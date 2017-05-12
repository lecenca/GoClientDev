package src.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
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

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Client extends Application {
    public static boolean offlineMode;
    private UserComparator comparator = new UserComparator();
    private Stage primaryStage;
    private Stage createRoomStage;
    private Stage gameStage;
    private Stage lobbyStage = null;
    private Stage signupStage = null;
    private static User user;
    private Connect connect = null;
    private ArrayList playerList = new ArrayList();
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
                        JOptionPane.showMessageDialog(null, "与服务器连接失败!\n正在尝试重新连接...", "连接错误",
                                JOptionPane.INFORMATION_MESSAGE);
                        reConnect();
                    } catch (NullPointerException e) {
                        System.out.println("与服务器连接失败！");
                        JOptionPane.showMessageDialog(null, "与服务器连接失败!\n正在尝试重新连接...", "连接错误",
                                JOptionPane.INFORMATION_MESSAGE);
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
                    /*
                     * if(!SignupController.hasCheckedAccount) {
                     * signupController.checkAccountValid();
                     * SignupController.hasCheckedAccount = true; }
                     */
                    JOptionPane.showMessageDialog(null, "重新连接服务器成功！", "连接提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (UnknownHostException e) {
                    if (print2)
                        System.out.println("客户端异常！");
                    print2 = false;
                    // e.printStackTrace();
                } catch (IOException e) {
                    if (print)
                        System.out.println("正在尝试重新连接服务器。。。。");
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
    /*
     * private Thread listenPlayerList = new Thread(new Runnable() {
     * 
     * @Override public void run() { System.out.println("监听玩家列表线程启动！"); while
     * (true) { if (!players.isEmpty()) { User player = players.remove();
     * playerData.add(player); playersMap.put(player.getAccount(), player); }
     * 
     * try { Thread.currentThread().sleep(1000); playerData.add(new User("tom",
     * 10, 100, 60, 1)); playerData.sorted(comparator); } catch
     * (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); }
     * 
     * } } }); private Thread listenRoomList = new Thread(new Runnable() {
     * 
     * @Override public void run() { System.out.println("监听房间列表线程启动！"); while
     * (true) { if (!rooms.isEmpty()) { Room room = rooms.remove();
     * roomData.add(room); roomsMap.put(room.getId(), room); }
     * 
     * roomData.add(new Room(2, "room..", "player1", "player2", 1)); try {
     * Thread.currentThread().sleep(2000); } catch (InterruptedException e) { //
     * TODO Auto-generated catch block e.printStackTrace(); }
     * 
     * } } });
     * 
     * private Thread chatThread = new Thread(new Runnable() {
     * 
     * @Override public void run() { System.out.println("聊天线程启动！"); while (true)
     * { if (!chatMessages.isEmpty()) try { Platform.runLater(new Runnable() {
     * 
     * @Override public void run() { // TODO Auto-generated method stub if
     * (!chatMessages.isEmpty()) messageData.add(chatMessages.remove()); } });
     * 
     * } catch (Exception e) {
     * 
     * }
     * 
     * messageData.add("hello"); try { Thread.currentThread().sleep(2000); }
     * catch (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); }
     * 
     * }
     * 
     * }
     * 
     * 
     * @Override public void run() { System.out.println("聊天线程启动"); // TODO
     * Auto-generated method stub while (true) {
     * 
     * if(!chatMessage.isEmpty()) {
     * chatBoxController.sendMessage(chatMessage.remove()); }
     * 
     * // chatBoxController.sendMessage("hello"); if (!chatMessages.isEmpty())
     * messageData.add(chatMessages.remove()); try { messageData.add("hello");
     * Thread.currentThread().sleep(2000); } catch (InterruptedException e) {
     * catch block e.printStackTrace(); } } }
     * 
     * });
     */
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
                // chat
                if (!chatMessages.isEmpty())
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (!chatMessages.isEmpty())
                                    messageData.add(chatMessages.remove());
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
                                if (!privateChatMessages.isEmpty())
                                    privateMessageData.add(privateChatMessages.remove());
                            }
                        });

                    } catch (Exception e) {

                    }
            }
        }
    });

    public Client() {
        playerData.sort(comparator);
        signupStage = new Stage();
        signupStage.setTitle("MicroOnlineGo - 注册");
        signupStage.setResizable(false);

        lobbyStage = new Stage();
        lobbyStage.setTitle("MicroOnlineGo - 大厅");
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

        gameStage = new Stage();
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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("您正在游戏中，确认要退出游戏吗？");
                    alert.initOwner(gameStage);
                    alert.initModality(Modality.WINDOW_MODAL);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.CANCEL) {
                        event.consume();
                    }
                    gameController.clear();
                    return;
                }
                if (getUser().getState() == Type.UserState.GAMING) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("您正在游戏中，确认要退出游戏吗？\n强制退出将会损失较多积分");
                    alert.initOwner(gameStage);
                    alert.initModality(Modality.WINDOW_MODAL);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        gameController.escape();
                        gameController.clear();
                        Room room = roomsMap.get(user.getRoom());
                        room.setState(Type.RoomState.READY);
                        if (room.getPlayer1() == user.getAccount()) {
                            room.setPlayer1("");
                            updateRoom(room, Type.UpdateRoom.PLAYER1OUT);
                        } else {
                            room.setPlayer2("");
                            updateRoom(room, Type.UpdateRoom.PLAYER2OUT);
                        }
                        user.setState(Type.UserState.IDLE);
                        user.setRoom(0);
                        updateUser();
                    } else {
                        event.consume();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MicroOnlineGo - 登录");
        primaryStage.setResizable(false);
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
            /*
             * listenPlayerList.setDaemon(true); listenPlayerList.start();
             * listenRoomList.setDaemon(true); listenRoomList.start();
             * chatThread.setDaemon(true); chatThread.start();
             */
        }
    }

    public void gotoLogin() {
        LoginController loginController = (LoginController) changeStage("view/Login.fxml", primaryStage);
        loginController.setClient(this);
        loginController.resetAccount();
        loginController.resetPassword();
    }

    public void gotoSignup() {
        signupController = (SignupController) changeStage("view/Signup.fxml", signupStage);
        signupController.setClient(this);
    }

    public void gotoLobby() {
        lobbyStage.show();
        lobbyController.fetchLobbyInfo();
    }

    public void backToLobby() {
        createRoomStage.close();
    }

    public void gotoCreateRoom() {
        createRoomStage = new Stage();
        createRoomStage.initModality(Modality.APPLICATION_MODAL);
        createRoomStage.initOwner(primaryStage);
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource("view/CreateRoom.fxml"));
        InputStream in = getClass().getResourceAsStream("view/CreateRoom.fxml");
        try {
            createRoomStage.setScene(new Scene(loader.load(in)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        createRoomStage.show();
        CreateRoomController createRoomController = loader.getController();
        createRoomController.setClient(this);
    }

    public void gotoGame(Room room) {
        gameController.setRoom(room);
        gameController.getChatBoxController().setItems(privateMessageData);
        gameStage.setTitle("MicroOnlineGo - 房间 " + Integer.toString(room.getId()) + " " + room.getName());
        gameStage.show();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource(fxml));
        InputStream in = getClass().getResourceAsStream(fxml);
        primaryStage.close();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(loader.load(in)));
        primaryStage.show();
        return (Initializable) loader.getController();
    }

    private Initializable changeStage(String fxml, Stage stage) {
        // create a new stage
        /*
         * if(stage == null) stage = new Stage();
         */
        // create the fxml loader
        FXMLLoader loader = new FXMLLoader();
        // set the location of the fxml
        loader.setLocation(getClass().getResource(fxml));
        // get the pane
        try {
            Pane pane = loader.load();
            // create a new scene with the pane
            Scene scene = new Scene(pane);
            // set the scene
            stage.setScene(scene);
            // show the stage
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
            // TODO: 如何修改现有player的属性
            int index = playerData.indexOf(player);
            /*User user = playerData.get(index);
            user.setData(player.getData());
            user.setRoom(player.getRoom());
            user.setState(player.getState());*/
            playerData.set(index, player);

        } else {
            playerData.add(player);
        }
        if (playerData.size() > 1) {
            List<User> subList = playerData.subList(1, playerData.size() - 1);
            subList.sort(new UserComparator());
            subList.add(0, playerData.get(0));
            playerData.setAll(subList);
            
        }
        playersMap.put(player.getAccount(), player);
    }

    public static void removePlayer(String account) {
        playerData.remove(playersMap.get(account));
        playersMap.remove(account);
    }

    public static void adjustRoom(Room room) {
        if (roomData.contains(room)) {
            // TODO: 如何修改现有 room 的属性
            int index = roomData.indexOf(room);
            /*Room room2 = roomData.get(index);
            room2.setId(room.getId());
            room2.setName(room.getName());
            room2.setPlayer1(room.getPlayer1());
            room2.setPlayer2(room.getPlayer2());
            room2.setState(room.getState());*/
            roomData.set(index, room);
        } else {
            roomData.add(room);
        }
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

    public Stage getPrimaryStage() {
        return primaryStage;
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

    public Stage getsignupStage() {
        return signupStage;
    }

    public ArrayList getPlayerList() {
        return playerList;
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
