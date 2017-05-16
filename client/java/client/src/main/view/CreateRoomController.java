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

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import src.main.Client;
import src.main.Room;
import src.main.Type;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import javax.swing.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

public class CreateRoomController implements Initializable {

    private Client client;
    private TableView<Room> roomList;

    @FXML
    private TextField roomNameField;
    @FXML
    private Button createRoomBtn;
    @FXML
    private Button backBtn;
    @FXML
    private ComboBox mainTime;
    @FXML
    private ComboBox period;
    @FXML
    private ComboBox periodTimes;
    @FXML
    private ComboBox komi;

    private Set roomId = new HashSet<>();
    private Random random = new Random();

    private static String[] defaultName = {
            "我爱下围棋",
            "大家一起来下棋",
            "棋逢对手",
            "快来挑战我",
            "孤独求败"
    };

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void createRoom() {
        /************* release *****************/
        Room room = new Room();
        if (Client.offlineMode) {
            room.setId(88888);
        } else {
            room.setId(getRoomId());
        }
        String name = roomNameField.getText();
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "房间名称不能为空");
        } else {
            room.setName(name);
        }
        room.setPlayer1(Client.getUser().getAccount());
        room.setState(Type.RoomState.WATING);
        room.setConfig(
                this.komi.getSelectionModel().getSelectedIndex(),
                this.mainTime.getSelectionModel().getSelectedIndex(),
                this.period.getSelectionModel().getSelectedIndex(),
                this.periodTimes.getSelectionModel().getSelectedIndex()
        );
        client.backToLobby();
        client.gotoGame(room);
        if (Client.offlineMode) {
            return;
        }
        Client.getUser().setRoom(room.getId());
        Client.getUser().setState(Type.UserState.READY);
        Client.updateUser();
        String msg = Encoder.updateRoomRequest(room, Type.UpdateRoom.PLAYER1IN);
        System.out.println("update room msg: " + msg);
        Connect.send(msg);
        /************* release ********************/
    }

    private int getRoomId() {
        int res = random.nextInt(65535) % 65536;
        while (roomId.contains(res)) {
            res = random.nextInt(65535) % 65536;
        }
        roomId.add(res);
        return res;
    }

    private void initComboBox() {
        komi.setItems(FXCollections.observableArrayList());
        mainTime.setItems(FXCollections.observableArrayList());
        period.setItems(FXCollections.observableArrayList());
        periodTimes.setItems(FXCollections.observableArrayList());

        komi.getItems().add("让先");
        komi.getItems().add("黑贴3.5目");
        komi.getItems().add("黑贴6.5目");
        komi.setVisibleRowCount(6);
        komi.getSelectionModel().select(2);

        mainTime.getItems().add("1分");
        for (int i = 5; i <= 20; i = i + 5) {
            mainTime.getItems().add(String.valueOf(i) + "分");
        }
        mainTime.getItems().add("30分");
        mainTime.getItems().add("40分");
        mainTime.getItems().add("60分");
        mainTime.getItems().add("90分");
        mainTime.setVisibleRowCount(6);
        mainTime.getSelectionModel().select(5);

        period.getItems().add("15秒");
        for (int i = 20; i <= 60; i = i + 10) {
            period.getItems().add(String.valueOf(i) + "秒");
        }
        period.setVisibleRowCount(6);
        period.getSelectionModel().select(2);

        for (int i = 1; i <= 7; i = i + 2) {
            periodTimes.getItems().add(String.valueOf(i) + "次");
        }
        periodTimes.getItems().add("10次");
        periodTimes.setVisibleRowCount(6);
        periodTimes.getSelectionModel().select(1);
    }

    @FXML
    private void back() {
        client.backToLobby();
    }

    public void setRoomList(TableView<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomNameField.setText(defaultName[random.nextInt(5) % 5]);
        initComboBox();
    }
}
