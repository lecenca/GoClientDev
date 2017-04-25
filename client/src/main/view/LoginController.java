package src.main.view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import src.main.Client;

import src.main.ThreadLock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import src.main.communication.Encoder;

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

    private Encoder encoder   = new Encoder();

    @FXML private void login() throws Exception{
        if(checkInfo()){
        	String msg = encoder.getPlayerListRequest();
        	//String resMsg = client.getConnect().sendAndReceive(msg);
        	//ArrayList list = Decoder.parseJsontoArray(msg);
        	//client.getPlayerList().addAll(list);
        	client.getPrimaryStage().close();
            client.gotoLobby();
           
        }
    }

    @FXML
    private void signUp() throws Exception{
    	client.getPrimaryStage().close();
        client.gotoSignUp();
    }

    @FXML
    private boolean checkInfo() throws Exception {
      //  boolean isValid = true;
        String account = this.account.getText();
        String password = this.password.getText();
        if(account.isEmpty()){
            emptyAccountTips.setVisible(true);
            //isValid = false;
            return false;
            
        }
        else{
            emptyAccountTips.setVisible(false);
        }
        if(password.isEmpty()){
            emptyPasswordTips.setVisible(true);
//            isValid = false;
            return false;
        }
        else{
            emptyPasswordTips.setVisible(false);
        }
        if(!account.isEmpty() && !password.isEmpty()){
            String json = encoder.loginRequest(this.account.getText(),this.password.getText());
            System.out.println(json);
            //connector.send(json);
//            String msg = client.getConnect().sendAndReceive(json);
//            if(!"true".equals(msg))
//            	return false;
        }
        return true;
    }

    @FXML
    private void setTipsError(Label tip, String msg){
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
