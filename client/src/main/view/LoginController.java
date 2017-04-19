package src.main.view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import src.main.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.communication.Connect;
import src.main.communication.Encode;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    private Client client;

    @FXML private TextField     account;
    @FXML private PasswordField password;
    @FXML private Label         emptyAccountTips;
    @FXML private Label         emptyPasswordTips;
    @FXML private Label         invaildMessageTips;

    private Encode  encoder   = new Encode();

    @FXML private void login() throws Exception{
        if(checkInfo()){
            client.gotoLobby();
        }
    }

    @FXML
    private void signUp() throws Exception{
        client.gotoSignUp();
    }

    @FXML
    private boolean checkInfo() throws Exception {
        boolean isValid = true;
        String account = this.account.getText();
        String password = this.password.getText();
        if(account.isEmpty()){
            emptyAccountTips.setVisible(true);
            isValid = false;
        }
        else{
            emptyAccountTips.setVisible(false);
        }
        if(password.isEmpty()){
            emptyPasswordTips.setVisible(true);
            isValid = false;
        }
        else{
            emptyPasswordTips.setVisible(false);
        }
        if(!account.isEmpty() && !password.isEmpty()){
            String json = encoder.loginRequest(this.account.getText(),this.password.getText());
            System.out.println(json);
            //connector.send(json);
        }
        return isValid;
    }

    @FXML
    private void setTipsError(Label tip, String msg){
        tip.setVisible(true);
        tip.setTextFill(Color.RED);
        tip.setText(msg);
    }

    public void setClient(Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        emptyAccountTips.setVisible(false);
        emptyPasswordTips.setVisible(false);
        invaildMessageTips.setVisible(false);
    }

}
