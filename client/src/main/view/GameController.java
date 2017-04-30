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

    @FXML private ChessBoard chessPaneController;
    @FXML private TextField inputField;
    @FXML private Button sentBtn;
    @FXML private ListView<String> chatBox;
    @FXML private Timer timeLabel01Controller;
    @FXML private Timer timeLabel02Controller;
    @FXML private ChatBox chatBoxController;

    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    private void sent(){
        chatBoxController.sentSentence(inputField.getText());
    }

    @FXML
    private void ready(){
        chessPaneController.setReady(true);
        // TODO: 向服务器发送准备信息

        /*********** test **********/
        timeLabel01Controller.start();
        /*********** test **********/
    }

    @FXML
    private void surrender(){
        // TODO: 向服务器发送认输信息
    }

    @FXML
    private void judge(){
        // TODO: 向服务器发送判子请求信息
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        chessPaneController.setTimer(timeLabel01Controller,timeLabel02Controller);
        timeLabel01Controller.initTimer(1,10,3);
        timeLabel02Controller.initTimer(1,10,3);
    }
}
