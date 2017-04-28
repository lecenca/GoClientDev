package src.main.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import src.main.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.Room;
import src.main.RoomListCell;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;
import src.util.MessageQueue;

import java.io.IOException;
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
    private Button sentBtn;
    @FXML
    private Button createRoomBtn;
    @FXML
    private Button autoMatch;
    @FXML
    private ListView<String> chatBox;
    @FXML
    private TableView<RoomListCell> roomList;
    @FXML
    private TableColumn roomIdCol;
    @FXML
    private TableColumn roomNameCol;
    @FXML
    private TableColumn player1Col;
    @FXML
    private TableColumn player2Col;
    @FXML
    private TableColumn stateCol;
    @FXML
    private TableView<User> playerList;
    @FXML
    private TableColumn<User,String> nickname;
    @FXML
    private TableColumn<User,String> level;
    @FXML
    private TableColumn<User,String> integral;
    @FXML
    private TableColumn<User,String> state;
    @FXML
    private ChatBox chatBoxController;
    private ObservableList<RoomListCell> roomData = FXCollections.observableArrayList();
    private ObservableList<User> playerData = FXCollections.observableArrayList();
    /*@FXML
    private ProgressBar progress = new ProgressBar();*/

    /*private static ArrayList<Room> rooms;
    private static ArrayList<User> players;*/
    public static MessageQueue<Room> rooms = new MessageQueue<>();
    public static MessageQueue<User> players = new MessageQueue<>();
    @FXML
    private void send() {
        /************* test ********************/
        chatBoxController.sentSentence(inputField.getText());
        client.getConnect().send(inputField.getText());
        /************* test ********************/
    }

    @FXML
    private void logout() throws Exception {
        /************* release *****************/
        client.getLobbyStage().close();
        //client.getPrimaryStage().show();
        client.gotoLogin();
        /************* release *****************/
    }

    @FXML
    private void fastMatch() throws Exception {
        client.gotoGame();
    }

    @FXML
    private void gotoCreateRoom() throws IOException {
        client.gotoCreateRoom(roomList);
    }

    @FXML
    private void clickRoom(MouseEvent mouseEvent) throws Exception {
        if (mouseEvent.getClickCount() == 2) {
            RoomListCell room = roomList.getSelectionModel().getSelectedItem();
            User player02 = new User();
            player02.setNickname("玩家二");
            //room.setPlayer2(player02);
            room.setPlayer02("玩家二");
            room.setState(1);
            room.setState(1);
            System.out.println("you click");
            client.gotoGame();
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private void updateRoom() {
        String json = Encoder.updateRoomRequest();
        client.getConnect().send(json);
        Connect.waitForRec();
    }

    private void updatePlayer() {
        String json = Encoder.updatePlayersRequest();
        client.getConnect().send(json);
        Connect.waitForRec();
    }

    public void fetchLobbyInfo() {
        //progress.setProgress(0.1);
        String json = Encoder.updateRoomRequest();
        client.getConnect().send(json);
        //progress.setProgress(0.2);
        Connect.waitForRec();
        //progress.setProgress(0.4);
        String json2 = Encoder.updatePlayersRequest();
        client.getConnect().send(json2);
        //progress.setProgress(0.6);
        Connect.waitForRec();
        //progress.setProgress(1.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //fetchLobbyInfo();
        //progress.setProgress(1.0);
        /************* test ********************/

        roomList.setItems(roomData);
        roomIdCol.setCellValueFactory(new PropertyValueFactory("roomId"));
        roomIdCol.setCellFactory(column -> {
            return new TableCell<Room, Integer>() {
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        roomNameCol.setCellValueFactory(new PropertyValueFactory("roomName"));
        roomNameCol.setCellFactory(column -> {
            return new TableCell<Room, String>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                    } else {
                        setText(item);
                    }
                }
            };
        });

        player1Col.setCellValueFactory(new PropertyValueFactory("player01Property"));
        player1Col.setCellFactory(column -> {
            return new TableCell<Room, String>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                    } else {
                        setText(item);
                    }
                }
            };
        });

        player2Col.setCellValueFactory(new PropertyValueFactory("player02Property"));
        player2Col.setCellFactory(column -> {
            return new TableCell<Room, String>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                    } else {
                        setText(item);
                    }
                }
            };
        });

        stateCol.setCellValueFactory(new PropertyValueFactory("stateProperty"));
        stateCol.setCellFactory(column -> {
            return new TableCell<Room, Integer>() {
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                    } else {
                        if (item == 0) {
                            setText("待挑战");
                        } else if (item == 1) {
                            setText("对战中");
                        }
                    }
                }
            };
        });
        
        playerList.setItems(playerData);
       /* firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());*/
       // nickname.setCellValueFactory(cellData -> cellData.getValue().getNickname());
        /************* test ********************/
        nickname.setCellValueFactory(cellData -> cellData.getValue().getNicknameProperty());
        level.setCellValueFactory(cellData -> cellData.getValue().getLevelProperty());
        //integral.setCellValueFactory(cellData -> cellData.getValue().getIntegral2());
        state.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());

        
    }

	public ObservableList<RoomListCell> getRoomData() {
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

    
}
