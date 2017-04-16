package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.main.Client;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/4.
 */
public class GameController implements Initializable {

    private Client client;

    @FXML private ChessBox chessPaneController;
    @FXML private TextField inputField;
    @FXML private Button sentBtn;
    @FXML private ListView<String> chatBox;
    @FXML private Timer timeLabelController;
    @FXML private ChatBox chatBoxController;

    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    private void sent(){
        chatBoxController.sentSentence(inputField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        chessPaneController.setTimer(timeLabelController);

        chatBox.setLayoutX(602);
        chatBox.setLayoutY(24);
        chatBox.setPrefWidth(200);
        chatBox.setPrefHeight(393);
    }
}
