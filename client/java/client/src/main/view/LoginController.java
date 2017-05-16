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

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import src.main.Client;
import src.main.Type;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Client client;

    @FXML
    private Button offlineMode;
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

    private MediaPlayer music;
    private boolean playing;

    public static boolean correct;

    public LoginController() {
        music = new MediaPlayer(new Media(getClass().getResource("/resources/music/login.mp3").toExternalForm()));
        music.setOnEndOfMedia(new Runnable() {
            public void run() {
                music.seek(Duration.ZERO);
            }
        });
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
        password.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    login();
                }
            }
        });
    }

    @FXML
    private void offline() {
        Client.offlineMode = true;
        User player1 = new User("1", "玩家一");
        Client.setUser(player1);
        client.gotoCreateRoom();
    }

    @FXML
    private void login() {
        if (checkValid()) {
            Client.offlineMode = false;
            client.getLoginStage().close();
            client.gotoLobby();
        }
    }

    @FXML
    private void signup() {
        client.getLoginStage().close();
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
            if (Connect.timeout) {
                Connect.timeout = false;
                return false;
            }
            if (!correct) {
                setTipsError(invaildMessageTips, "账号或密码错误");
            }
        }
        return correct;
    }

    public void playMusic() {
        playing = true;
        music.play();
    }

    public void stopMusic() {
        playing = false;
        music.stop();
    }

    public boolean isPlayingMusic() {
        return playing;
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
        password.setText("");
    }

    @FXML
    public void clearTip() {
        setTipsError(invaildMessageTips, "");
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
