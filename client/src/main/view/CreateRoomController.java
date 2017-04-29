package src.main.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import src.main.Client;
import src.main.Room;
import src.main.RoomListCell;
import src.main.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/9.
 */
public class CreateRoomController implements Initializable {

    private Client client;
    private TableView<RoomListCell> roomList;

    @FXML private TextField roomNameField;
    @FXML private TextField passwordField;
    @FXML private Button createRoomBtn;
    @FXML private Button backBtn;
    @FXML private ComboBox mainTime;
    @FXML private ComboBox poriod;
    @FXML private ComboBox periodTimes;
    @FXML private ComboBox komi;

    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    private void createRoom() {
        /************* test ********************/
        Room room = new Room();
        room.setId(1111);
        String name = roomNameField.getText();
        if (name != null)
            room.setName(name);
        String password = passwordField.getText();
        if (password != null)
            room.setPassword(password);
        room.setPlayer1("玩家一");
        room.setState(0);
        RoomListCell cell = new RoomListCell(room);
        cell.setPlayer1("玩家一");
        cell.setState(0);
        cell.setRoomId(1111);
        cell.setRoomName(room.getName());
        roomList.getItems().add(cell);
        client.backToLobby();
        System.out.println("you create");
        /************* test ********************/
    }

    private void initComboBox(){
        mainTime.setItems(FXCollections.observableArrayList());
        poriod.setItems(FXCollections.observableArrayList());
        periodTimes.setItems(FXCollections.observableArrayList());
        komi.setItems(FXCollections.observableArrayList());
        mainTime.getItems().add("1分");
        for (int i = 5; i <= 20; i = i +5) {
            mainTime.getItems().add(String.valueOf(i)+"分");
        }
        mainTime.getItems().add("30分");
        mainTime.getItems().add("40分");
        mainTime.getItems().add("60分");
        mainTime.getItems().add("90分");
        poriod.getItems().add("15秒");
        for (int i = 20; i <= 60; i = i + 10){
            poriod.getItems().add(String.valueOf(i)+"秒");
        }
        for (int i = 1; i <= 7; i = i + 2){
           periodTimes.getItems().add(String.valueOf(i)+"次");
        }
        komi.getItems().add("让先");
        komi.getItems().add("黑贴3.5目");
        komi.getItems().add("黑贴6.5目");
        periodTimes.getItems().add("10次");
        mainTime.setVisibleRowCount(6);
        mainTime.getSelectionModel().select(5);
        poriod.setVisibleRowCount(6);
        poriod.getSelectionModel().select(2);
        periodTimes.setVisibleRowCount(6);
        periodTimes.getSelectionModel().select(1);
        komi.getSelectionModel().select(1);
    }

    @FXML
    private void back(){
        client.backToLobby();
    }

    public void setRoomList(TableView<RoomListCell> roomList) {this.roomList = roomList;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBox();
    }
}
