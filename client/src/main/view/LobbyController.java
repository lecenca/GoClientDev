package src.main.view;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import src.main.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.Room;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import javax.swing.*;
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
    private TableView<Room> roomList;
    @FXML
    private TableColumn roomIdCol;
    @FXML
    private TableColumn roomNameCol;
    @FXML
    private TableColumn player01Col;
    @FXML
    private TableColumn player02Col;
    @FXML
    private TableColumn stateCol;

    @FXML
    private ChatBox chatBoxController;

    /*@FXML
    private ProgressBar progress = new ProgressBar();*/

    private static ArrayList<Room> rooms;
    private static ArrayList<User> players;

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
            Room room = roomList.getSelectionModel().getSelectedItem();
            User player02 = new User();
            player02.setNickname("玩家二");
            room.setPlayer2(player02);
            room.setPlayer02Property("玩家二");
            room.setState(1);
            room.setStateProperty(1);
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

        roomList.setItems(FXCollections.observableArrayList());
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

        player01Col.setCellValueFactory(new PropertyValueFactory("player01Property"));
        player01Col.setCellFactory(column -> {
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

        player02Col.setCellValueFactory(new PropertyValueFactory("player02Property"));
        player02Col.setCellFactory(column -> {
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

        /************* test ********************/
    }
}
