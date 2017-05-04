package src.main.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import src.main.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.Room;
import src.main.Type;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;
import src.util.MessageQueue;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.net.URL;
import java.util.*;

/**
 * Created by touhoudoge on 2017/3/20.
 */
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
    private ChatBox chatBoxController;
    private ObservableList<Room> roomData = FXCollections.observableArrayList();
    private ObservableList<User> playerData = FXCollections.observableArrayList();

    /*
     * private static ArrayList<Room> rooms; private static ArrayList<User>
     * players;
     */
    public static MessageQueue<Room> rooms = new MessageQueue<>();
    public static MessageQueue<User> players = new MessageQueue<>();
    public static MessageQueue<String> chatMessage = new MessageQueue<>();

    private Thread listenPlayerList = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("监听玩家列表线程启动！");
            while (true) {
                /*if(!players.isEmpty()) {
					playerData.add(players.remove());
				}*/
                try {
                    Thread.currentThread().sleep(1000);
                    playerData.add(new User("tom", 10, 100, 60, 1));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    });
    private Thread listenRoomList = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("监听房间列表线程启动！");
            // TODO Auto-generated method stub
            while (true) {
				/*if(!rooms.isEmpty()) {
					roomData.add(rooms.remove());
				}*/
                roomData.add(new Room(2, "room..", "player1", "player2", 1));
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    });

    private Thread chatThread = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("聊天线程启动");
            // TODO Auto-generated method stub
            while (true) {
				/*if(!chatMessage.isEmpty()) {
					 chatBoxController.sendMessage(chatMessage.remove());
				}*/

                try {
                    chatBoxController.sendMessage("hello");
                    Thread.currentThread().sleep(2000);
                } catch (IllegalStateException e) {
                    System.out.println("异常了");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    public LobbyController() {

    }

    @FXML
    private void send() {
        /************* test ********************/
        chatBoxController.sendMessage(Client.getUser().getNickname()+":"+ inputField.getText());
        //client.getConnect().send("无名:" + inputField.getText());
        inputField.clear();
        sendbtn.setDisable(true);
        /************* test ********************/
    }

    @FXML
    private void logout() {
        /************* release *****************/
        Connect.send(Encoder.logoutRequest());
        client.getLobbyStage().close();
        client.gotoLogin();
        /************* release *****************/
    }

    @FXML
    private void fastMatch() {
        // TODO: find a room that is most match
        if (Client.getUser().getState() != Type.UserState.IDLE){
            JOptionPane.showMessageDialog(null,"您已经在房间中");
            return;
        }
        Room match = null;
        for(Room room : Client.roomsMap.values()){
            if(room.hasSeat()){
                match = room;
                if(match.getPlayer1() == null || match.getPlayer1().isEmpty()){
                    match.setPlayer1(Client.getUser().getAccount());
                    match.setState(Type.RoomState.READY);
                    String msg = Encoder.updateRoomRequest(match, Type.UpdateRoom.PLAYER1IN);
                    System.out.println("update room msg: " + msg);
                    Connect.send(msg);
                }
                else{
                    match.setPlayer2(Client.getUser().getAccount());
                    match.setState(Type.RoomState.READY);
                    String msg = Encoder.updateRoomRequest(match, Type.UpdateRoom.PLAYER2IN);
                    System.out.println("update room msg: " + msg);
                    Connect.send(msg);
                }
                Client.getUser().setRoom(room.getId());
                Client.getUser().setState(Type.UserState.READY);
                String msg = Encoder.updatePlayerRequest(Client.getUser(), Type.UpdatePlayer.IN);
                System.out.println("update player msg: " + msg);
                Connect.send(msg);
                break;
            }
        }
        if(match != null){
            client.gotoGame(match);
        }
        else{
            int res = JOptionPane.showConfirmDialog(null , "暂时找不到匹配的房间哦，是否要自己创建一个呢？","未找到匹配的房间",JOptionPane.YES_NO_OPTION) ;
            if(res == JOptionPane.YES_OPTION){
                client.gotoCreateRoom();
            }
        }
    }

    @FXML
    private void gotoCreateRoom() {
        if (Client.getUser().getState() != Type.UserState.IDLE){
            JOptionPane.showMessageDialog(null,"您已经在房间中");
            return;
        }
        client.gotoCreateRoom();
    }

    @FXML
    private void clickRoom(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Room room = roomList.getSelectionModel().getSelectedItem();
            if(!room.hasSeat()){
                JOptionPane.showMessageDialog(null,"房间已经满人");
                return;
            }
            if (Client.getUser().getState() != Type.UserState.IDLE){
                JOptionPane.showMessageDialog(null,"您已经在房间中");
                return;
            }
            room.setState(Type.RoomState.READY);
            if (room.getPlayer1() == null || room.getPlayer1().isEmpty()) {
                room.setPlayer1(Client.getUser().getAccount());
                String msg = Encoder.updateRoomRequest(room, Type.UpdateRoom.PLAYER1IN);
                System.out.println("update room msg: " + msg);
                Connect.send(msg);
            } else {
                room.setPlayer2(Client.getUser().getAccount());
                String msg = Encoder.updateRoomRequest(room, Type.UpdateRoom.PLAYER2IN);
                System.out.println("update room msg: " + msg);
                Connect.send(msg);
            }
            client.gotoGame(room);
            Client.getUser().setRoom(room.getId());
            Client.getUser().setState(Type.UserState.READY);
            String msg = Encoder.updatePlayerRequest(Client.getUser(), Type.UpdatePlayer.IN);
            System.out.println("update player msg: " + msg);
            Connect.send(msg);
            //test
            //client.gotoGame(room);
        }
    }
    @FXML
    private void hasText() {
    	String text = inputField.getText();
    	if(text == null || "".equals(text) || text.length() == 0)
    		sendbtn.setDisable(true);
    	else
    		sendbtn.setDisable(false);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void fetchLobbyInfo() {
        Connect.send(Encoder.fetchRoomsRequest());
        Connect.send(Encoder.fetchPlayersRequest());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setAssociation() {
    	chatBoxController.setItems(client.getMessageData());
        roomList.setItems(client.getRoomData());
        roomIdCol.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        roomNameCol.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        player1Col.setCellValueFactory(cellData -> cellData.getValue().getPlayer1Property());
        player2Col.setCellValueFactory(cellData -> cellData.getValue().getPlayer2Property());
        roomStateCol.setCellValueFactory(cellData -> cellData.getValue().getStatesProperty());
        configCol.setCellValueFactory(cellDate->cellDate.getValue().getConfigProperty());
        playerList.setItems(client.getPlayerData());
        nicknameCol.setCellValueFactory(cellData -> cellData.getValue().getNicknameProperty());
        levelCol.setCellValueFactory(cellData -> cellData.getValue().getLevelProperty());
        winCol.setCellValueFactory(cellData -> cellData.getValue().getWinProperty());
        loseCol.setCellValueFactory(cellData -> cellData.getValue().getLoseProperty());
        playerStateCol.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
    }

    public void addPlayer(User user){
        this.playerList.getItems().add(user);
    }

    public void addRoom(Room room){
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

    public Thread getListenPlayerList() {
        return listenPlayerList;
    }

    public Thread getListenRoomList() {
        return listenRoomList;
    }

    public Thread getChatThread() {
        return chatThread;
    }

}
