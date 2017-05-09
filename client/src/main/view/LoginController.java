package src.main.view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
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
    private void login() {
        if (checkValid()) {
            client.getPrimaryStage().close();
            client.gotoLobby();
            /***** test *****/
            Client.setUser(new User());
            /***** test *****/
            Client.getUser().setState(Type.UserState.IDLE);
            Client.getLobbyController().addPlayer(Client.getUser());
        }
    }

    @FXML
    private void signup() {
        client.getPrimaryStage().close();
        client.gotoSignup();
    }

    @FXML
    private boolean checkValid() {
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
            if (Connect.interrupted()) {
                return false;
            }
            String json = Encoder.loginRequest(this.account.getText(), this.password.getText());
            Connect.send(json);
            Connect.waitForRec(Type.Response.LOGIN_SUCCESS, Type.Response.LOGIN_FAILED);
            if(Connect.timeout) {
                Connect.timeout = false;
                return false;
            }
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
