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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBox implements Initializable {
    @FXML
    private ListView<String> chatBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatBox.setItems(FXCollections.observableArrayList());
    }

    public void clearMessage() {
        chatBox.getItems().clear();
    }

    public void sendMessage(String message) {
        chatBox.getItems().add(message);
        if (chatBox.getItems().size() > 20) {
            chatBox.getItems().remove(0);
        }
        chatBox.scrollTo(chatBox.getItems().size() - 1);
    }

    public void setItems(ObservableList<String> chatMessage) {
        chatBox.setItems(chatMessage);
    }

    public void clear() {
        chatBox.getItems().clear();
    }

    public ListView<String> getChatBox() {
        return chatBox;
    }

}
