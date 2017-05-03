package src.main.view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import src.main.Client;
import src.main.Type;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Client client;

    @FXML
    private TextField account;
    @FXML
    private PasswordField password;
    @FXML
    private Label emptyAccountTips;
    @FXML
    private Label emptyPasswordTips;
    @FXML
    private Label invaildMessageTips;
    @FXML
    private AnchorPane loginPane;

    public static boolean correct;

    @FXML
    private void login() throws Exception {
        if (checkValid()) {
            client.getPrimaryStage().close();
            client.gotoLobby();
            Client.getUser().setState(Type.UserState.IDLE);
            Client.getLobbyController().addPlayer(Client.getUser());
        }
    }

    @FXML
    private void signup() throws Exception {
        client.getPrimaryStage().close();
        client.gotoSignup();
    }

    @FXML
    private boolean checkValid() throws Exception {
        correct = false;
        String account = this.account.getText();
        String password = this.password.getText();
        if (account.isEmpty()) {
            emptyAccountTips.setVisible(true);
            return false;
        } else {
            emptyAccountTips.setVisible(false);
        }
        if (password.isEmpty()) {
            emptyPasswordTips.setVisible(true);
            return false;
        } else {
            emptyPasswordTips.setVisible(false);
        }
        if (!account.isEmpty() && !password.isEmpty()) {
            String json = Encoder.loginRequest(this.account.getText(), this.password.getText());
            //System.out.println(json);
            client.getConnect().send(json);
            //Connect.waitForRec();
            client.getConnect().waitForRec(Type.Response.LOGIN_SUCCESS,Type.Response.LOGIN_FAILED);
            /*json = Encoder.fetchRoomsRequest();
            client.getConnect().send(json);
            Connect.waitForRec();
            json = Encoder.fetchPlayersRequest();
            client.getConnect().send(json);
            Connect.waitForRec();*/
            if (!correct) {
                setTipsError(invaildMessageTips, "账号或密码错误");
            }
        }
        return correct;
    }

    @FXML
    private void setTipsError(Label tip, String msg) {
        tip.setVisible(true);
        tip.setTextFill(Color.RED);
        tip.setText(msg);
    }

    @FXML
    public void resetAccount() {
        account.setText("");
    }

    @FXML
    public void resetPassword() {
        account.setText("");
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emptyAccountTips.setVisible(false);
        emptyPasswordTips.setVisible(false);
        invaildMessageTips.setVisible(false);

        Image image = new Image("resources/image/bg001.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(500, 285, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        loginPane.setBackground(background);
    }

}
