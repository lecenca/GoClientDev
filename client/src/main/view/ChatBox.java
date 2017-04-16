package src.main.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/9.
 */
public class ChatBox implements Initializable
{
    @FXML private ListView<String> chatBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatBox.setItems(FXCollections.observableArrayList());
        chatBox.getItems().addListener((ListChangeListener.Change<? extends String> change)->{
            while (change.next()) {
                if (change.getList().size() > 10.0) {
                    change.getList().remove(0);
                }
            }
        });
    }

    public void sentSentence(String sentence){
        chatBox.getItems().add(sentence);
    }
}
