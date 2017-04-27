package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    public void setClient(Client client){
        this.client = client;
    }


    @FXML
    private void createRoom() {
        /************* test ********************/
        RoomListCell room = new RoomListCell();
        room.setRoomId(1111);
        String name = roomNameField.getText();
        if (name != null)
            room.setRoomName(name);
        String password = passwordField.getText();
        if (password != null)
            room.setPassword(password);
        User player01 = new User();
        player01.setNickname("玩家一");
        //room.setPlayer1(player01);
        room.setState(0);
        room.setState(0);
        room.setPlayer01("玩家一");
        roomList.getItems().add(room);
        client.backToLobby();
        /************* test ********************/
    }

    @FXML
    private void back(){
        client.backToLobby();
    }

    public void setRoomList(TableView<RoomListCell> roomList2) {this.roomList = roomList2;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
