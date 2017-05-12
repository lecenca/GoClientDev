package src.main.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import src.main.Room;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBox implements Initializable {
    @FXML
    private ListView<String> chatBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatBox.setItems(FXCollections.observableArrayList());
        chatBox.getItems().addListener((ListChangeListener.Change<? extends String> change) -> {
            while (change.next()) {
                if (change.getList().size() > 10.0) {
                    change.getList().remove(0);
                }
            }
        });
    }

    public void clearMessage(){
        chatBox.getItems().clear();
    }

    public void sendMessage(String message) {
        chatBox.getItems().add(message);
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
