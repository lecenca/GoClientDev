package src.main;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import src.main.communication.Connect;
import src.main.communication.Encoder;
import src.main.view.CreateRoomController;
import src.main.view.GameController;
import src.main.view.LobbyController;
import src.main.view.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.main.view.SignupController;
import src.util.MessageQueue;
import src.util.UserComparator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;

public class Client extends Application {
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
    private ObservableList<Room> roomData = FXCollections.observableArrayList();
    private ObservableList<User> playerData = FXCollections.observableArrayList();
    private ObservableList<String> messageData = FXCollections.observableArrayList();
    private ObservableList<String> privateMessageData = FXCollections.observableArrayList();
    public static MessageQueue<Room> rooms = new MessageQueue<>();
    public static MessageQueue<User> players = new MessageQueue<>();
    public static MessageQueue<String> chatMessages = new MessageQueue<>();
    public static MessageQueue<String> privateChatMessages = new MessageQueue<>();
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
                    if(!receiveThread.isAlive())
                        receiveThread.start();
                    if(!messageThread.isAlive())
                        messageThread.start();
                    /*if(!SignupController.hasCheckedAccount) {
                       signupController.checkAccountValid();
                       SignupController.hasCheckedAccount = true;
                    }*/
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
    /*private Thread listenPlayerList = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("监听玩家列表线程启动！");
            while (true) {
                if (!players.isEmpty()) {
                    User player = players.remove();
                    playerData.add(player);
                    playersMap.put(player.getAccount(), player);
                }
                
                 * try { Thread.currentThread().sleep(1000); playerData.add(new
                 * User("tom", 10, 100, 60, 1)); playerData.sorted(comparator);
                 * } catch (InterruptedException e) { // TODO Auto-generated
                 * catch block e.printStackTrace(); }
                 
            }
        }
    });
    private Thread listenRoomList = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("监听房间列表线程启动！");
            while (true) {
                if (!rooms.isEmpty()) {
                    Room room = rooms.remove();
                    roomData.add(room);
                    roomsMap.put(room.getId(), room);
                }
                
                 * roomData.add(new Room(2, "room..", "player1", "player2", 1));
                 * try { Thread.currentThread().sleep(2000); } catch
                 * (InterruptedException e) { // TODO Auto-generated catch block
                 * e.printStackTrace(); }
                 
            }
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
                                if (!chatMessages.isEmpty())
                                    messageData.add(chatMessages.remove());
                            }
                        });

                    } catch (Exception e) {

                    }
                
                 * messageData.add("hello"); try {
                 * Thread.currentThread().sleep(2000); } catch
                 * (InterruptedException e) { // TODO Auto-generated catch block
                 * e.printStackTrace(); }
                 
            }

        }

        
         * @Override public void run() { System.out.println("聊天线程启动"); // TODO
         * Auto-generated method stub while (true) {
         * 
         * if(!chatMessage.isEmpty()) {
         * chatBoxController.sendMessage(chatMessage.remove()); }
         * 
         * // chatBoxController.sendMessage("hello"); if
         * (!chatMessages.isEmpty()) messageData.add(chatMessages.remove()); try
         * { messageData.add("hello"); Thread.currentThread().sleep(2000); }
         * catch (InterruptedException e) { catch block e.printStackTrace(); } }
         * }
         
    });*/
    private Thread messageThread = new Thread(new Runnable() {

        @Override
        public void run() {
            System.out.println("监听信息线程启动！");
            while (true) {
                // players
                if (!players.isEmpty()) {
                    User player = players.remove();
                    playerData.add(player);
                    playersMap.put(player.getAccount(), player);
                }
                // rooms
                if (!rooms.isEmpty()) {
                    Room room = rooms.remove();
                    roomData.add(room);
                    roomsMap.put(room.getId(), room);
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
                if(!privateChatMessages.isEmpty()) 
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
        /********* 这是要的 ***********/
        connect = new Connect();
        receiveThread = connect.getReceiveThread();
        /*****************************/
        signupStage = new Stage();
        lobbyStage = new Stage();
        gameStage = new Stage();
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
                /*****************************************************/
                /*
                if(getUser().getState() == Type.UserState.GAMING){
                    int res = JOptionPane.showConfirmDialog(null,"您正在游戏中，确认要退出游戏吗？\n强制退出将会损失较多积分","提示",JOptionPane.YES_NO_OPTION);
                    if(res == JOptionPane.YES_OPTION){
                        getUser().gameResult(Type.GameResult.LOSE, 30.0);
                        // TODO: 另一个玩家
                    }
                    else{
                        event.consume();
                        return;
                    }
                }
                */
                /**************************************************************/
                /************************* test *****************************/
                if(getUser().getState() == Type.UserState.GAMING){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText("您正在游戏中，确认要退出游戏吗？\n强制退出将会损失较多积分");
                    alert.initOwner(gameStage);
                    alert.initModality(Modality.WINDOW_MODAL);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        getUser().gameResult(Type.GameResult.LOSE, 30.0);
                    }else{
                        event.consume();
                        return;
                    }
                }
                /************************* test *****************************/
                // need connect
                /*
                 * Room room = roomsMap.get(getUser().getRoom());
                 * if(room.playerNum() == 1){ String msg =
                 * Encoder.updateRoomRequest(room, Type.UpdateRoom.DESTROY);
                 * System.out.println("update player msg: " + msg);
                 * Connect.send(msg); } room.setState(Type.RoomState.WATING);
                 * if(room.getPlayer1() == getUser().getAccount()){
                 * room.setPlayer1(null); String msg =
                 * Encoder.updateRoomRequest(room, Type.UpdateRoom.PLAYER1OUT);
                 * System.out.println("update player msg: " + msg);
                 * Connect.send(msg); }else { room.setPlayer2(null); String msg
                 * = Encoder.updateRoomRequest(room,
                 * Type.UpdateRoom.PLAYER2OUT);
                 * System.out.println("update player msg: " + msg);
                 * Connect.send(msg); }
                 */
                getUser().setRoom(0);
                getUser().setState(Type.UserState.IDLE);
                updateUser();
                gameController.clear();
            }
        });
        privateMessageData.add("good night");
        privateMessageData.add("Do you have dinner?");
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MicroOnlineGo");
        gotoLogin();
        keepAliveThread.setDaemon(true);
        keepAliveThread.start();
        if (Connect.hasConnect()) {
            receiveThread.setDaemon(true);
            receiveThread.start();
            messageThread.setDaemon(true);
            messageThread.start();
            /*listenPlayerList.setDaemon(true);
            listenPlayerList.start();
            listenRoomList.setDaemon(true);
            listenRoomList.start();
            chatThread.setDaemon(true);
            chatThread.start();*/
        }
    }

    public void gotoLogin() {
        LoginController loginController = (LoginController) changeStage("view/Login.fxml", primaryStage);
        loginController.setClient(this);
        loginController.resetAccount();
        loginController.resetPassword();
    }

    public void gotoLobby() {
        lobbyStage.show();
        /*
         * Thread listenPlayerList = lobbyController.getListenPlayerList();
         * listenPlayerList.setDaemon(true); listenPlayerList.start(); Thread
         * listenRoomList = lobbyController.getListenRoomList();
         * listenRoomList.setDaemon(true); listenRoomList.start();
         */
        //Thread chatThread = lobbyController.getChatThread();
        /*
         * chatThread.setDaemon(true); chatThread.start();
         */
        lobbyController.fetchLobbyInfo();
    }

    public void gotoSignup() {
        signupController = (SignupController) changeStage("view/Signup.fxml", signupStage);
        signupController.setClient(this);
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
        CreateRoomController createRoomController = (CreateRoomController) loader.getController();
        createRoomController.setClient(this);
    }

    public void gotoGame(Room room) {
        gameController.setRoom(room);
        gameController.getChatBoxController().setItems(privateMessageData);
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
    // get the connection

    static public void updateUser(){
        String msg = Encoder.updatePlayerRequest(user);
        Connect.send(msg);
        System.out.println("update player msg: " + msg);
    }

    static public void updateRoom(Room room, int type){
        String msg = Encoder.updateRoomRequest(room, type);
        Connect.send(msg);
        System.out.println("update room msg: " + msg);
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
