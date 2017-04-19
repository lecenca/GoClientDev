package src.main.view;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import src.main.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.Room;


import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by touhoudoge on 2017/3/20.
 */
public class LobbyController implements Initializable{

    private Client client;

    @FXML private TextField inputField;
    @FXML private Button sentBtn;
    @FXML private Button createRoomBtn;
    @FXML private Button autoMatch;
    @FXML private ListView<String> chatBox;
    @FXML private TableView<Room> roomList;
    @FXML private TableColumn roomIdCol;
    @FXML private TableColumn roomNameCol;
    @FXML private TableColumn player01Col;
    @FXML private TableColumn player02Col;
    @FXML private TableColumn stateCol;

    @FXML private ChatBox chatBoxController;

    @FXML private void sent(){
        /************* test ********************/
        chatBoxController.sentSentence(inputField.getText());
        client.getConnect().sendMessage(inputField.getText());
        /************* test ********************/
    }

    @FXML
    private void logout() throws Exception{
        /************* release *****************/
    	client.getLobbyStage().close();
        client.getPrimaryStage().show();
        /************* release *****************/
    }

    @FXML
    private void autoMatchPlayer() throws Exception {
        client.gotoGame();
    }

    @FXML
    private void gotoCreateRoom() throws IOException {
        client.gotoCreateRoom(roomList);
    }

    public void setClient(Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        /************* test ********************/
        StringConverter<Object> sc = new StringConverter<Object>() {
            @Override
            public String toString(Object t) {
                return t == null ? null : t.toString();
            }

            @Override
            public Object fromString(String string) {
                return string;
            }
        };

        roomList.setItems(FXCollections.observableArrayList());
        roomIdCol.setCellValueFactory(new PropertyValueFactory("roomId"));
        roomIdCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        roomNameCol.setCellValueFactory(new PropertyValueFactory("roomName"));
        roomNameCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        player01Col.setCellFactory(new PropertyValueFactory("player01"));
        player01Col.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        /************* test ********************/
    }
}
