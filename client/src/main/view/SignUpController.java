package src.main.view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import src.main.UserInfo;
import src.main.Client;
import src.main.communication.Encode;
//import src.main.communication.Connect;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.prism.paint.Paint;
import com.sun.xml.internal.ws.spi.db.RepeatedElementBridge;


/**
 * Created by touhoudoge on 2017/3/23.
 */
public class SignUpController implements Initializable{

    private Client client;

    private UserInfo user       = new UserInfo();
    private Encode   encoder   = new Encode();
    //private Connect  connector = new Connect();

    private boolean validInfo = true;
    private boolean signUpCall = false;

    // User input information
    @FXML private TextField     account;

    @FXML private TextField     nickname;

    @FXML private PasswordField password;

    @FXML private PasswordField repeatPassword;

    @FXML private ComboBox      year;
    @FXML private ComboBox      month;
    @FXML private ComboBox      day;

    @FXML private RadioButton   male;
    @FXML private RadioButton   female;

    // Hint tag
    @FXML private Label         accountFormatTips;
    @FXML private Label         nameFormatTips;
    @FXML private Label         passwordFormatTips;
    @FXML private Label         repeatPasswordFormatTips;
    @FXML private Label         dateFormatTips;
    private boolean validAccount = false;
    private boolean validName = false;
    private boolean validPassword = false;
    private boolean validRepeatPassowrd = false;
    @FXML
    private void goToLogin() throws Exception{
        /************* test ********************/
        //client.gotoLobby();
        /************* test ********************/

        /************* release *****************/
        signUpCall = true;
        synchronousCheck();
        signUpCall = false;
        if(validInfo){
            String ac = this.account.getText();
            String nn = this.nickname.getText();
            String pw = this.password.getText();
            String rpw = this.repeatPassword.getText();
            int year = Integer.parseInt(this.year.getValue().toString());
            int month = Integer.parseInt(this.month.getValue().toString());
            int day = Integer.parseInt(this.day.getValue().toString());
            //gameServe = client.getGameServe();
            user.setAccount(ac);
            user.setNickname(nn);
            user.setPassword(pw);
            user.setBirthday(year,month,day);
            //gameServe.signIn(user);
            client.setAccount(user);
            String json = encoder.signUpRequest(user);
            System.out.println("user info: " + json);
            String msg = client.getConnect().sendAndReceive(json);
            if("true".equals(msg)) {
            	
            	client.getsignupStage().close();
            	client.getPrimaryStage().show();
            }
            else {
            	accountFormatTips.setText("帐号已经被注册！");
            	nameFormatTips.setText("昵称已经被使用！");
            }
            //connector.send(json);
        }
        /************* release *****************/
    }

    // TODO: �?查账号格式：16个字符，只能是字母�?�数字或下划�?'_'。若无效，设置相�? Lable 的文本并返回 false
    @FXML
    private boolean checkAccount() throws Exception {
        if(this.account.getText().isEmpty()){
            if(signUpCall){
                setTipsError(accountFormatTips,"账号不能为空");
            }
            else{
                accountFormatTips.setVisible(false);
                
            }
            return false;
        }
        else{
            if(!checkAccountExist()){
                setTipsError(accountFormatTips,"该账号已被注�?");
                return false;
            }
            // TODO
        }
        setTipsOk(accountFormatTips);
        return true;
    }
    @FXML
    private boolean checkAccount2() throws Exception{
    	if(signUpCall) {
    		if(this.account.getText().isEmpty() || this.account.getText() == null || "".equals(this.account.getText())) {
    			setTipsError(accountFormatTips,"账号不能为空");
    			return false;
    		}
    		return validAccount;
    	}else {
    		if(this.account.getText().isEmpty() ||  this.account.getText() == null || "".equals(this.account.getText())) {
    			accountFormatTips.setVisible(false);
    			return false;
    		}
    		if(checkAccountExist()){
                setTipsError(accountFormatTips,"该账号已被注�?");
                validAccount = false;
                return false;
            }
    	}
    	 setTipsOk(accountFormatTips);
    	 validAccount = true;
		return true;
    }
    // TODO: �?查昵称：不超�?12个显示字符（�?个汉字占2个字符，非汉字占1个字符）只支持汉字�?�字母�?�数字和下划�?'_'。若无效，设置相�? Lable 的文本并返回 false
    @FXML
    private boolean checkName(){
        if(this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())){
            if(signUpCall){
                setTipsError(nameFormatTips,"昵称不能为空");
            }
            else{
                nameFormatTips.setVisible(false);
            }
            return false;
        }
        else{
            // TODO
        }
        setTipsOk(nameFormatTips);
        return true;
    }
    @FXML
    private boolean checkName2() throws Exception {
    	if(signUpCall) {
    		if(this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())) {
    			setTipsError(nameFormatTips,"昵称不能为空");
    			return false;
    		}
    		return validName;
    	}
    	else {
    		if(this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())) {
    			nameFormatTips.setVisible(false);
    			return false;
    		}
    		if(isNameExist()){
                setTipsError(nameFormatTips,"该昵称已经被使用�?");
                validName = false;
                return false;
            }
    	}
    	setTipsOk(nameFormatTips);
    	validName = true;
    	return true;
    }
    // TODO: �?查密码格式：长度 8-16 位，若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkPassword(){
        /*if(this.password.getText().isEmpty()){
            if(signUpCall){
                // TODO
            }
            else{
                // TODO
            }
            return false;
        }
        else{

        }*/
    	if(signUpCall) {
    		if(password.getText().isEmpty()) {
    			setTipsError(passwordFormatTips,"密码不能为空");
    			passwordFormatTips.setVisible(true);
  
    			return false;
    		}
    		return validPassword;
    	}else {
    		repeatPasswordFormatTips.setVisible(false);
        	repeatPassword.setText("");
    		if(password.getText().isEmpty() || password.getText() == null || "".equals(password.getText())) {
    			passwordFormatTips.setVisible(false);
    			repeatPassword.setText("");
    			repeatPassword.setDisable(true);
    			repeatPasswordFormatTips.setVisible(false);
    			validPassword = false;
    			return false;
    		}
    		if(password.getText().length() < 6) {
    			setTipsError(passwordFormatTips, "密码至少6位");
    			validPassword  = false;
    			repeatPassword.setText("");
    			repeatPassword.setDisable(true);
    			repeatPasswordFormatTips.setVisible(false);
    			validPassword = false;
    			return false;
    		}else if(!password.getText().matches("[a-zA-Z0-9]+")) {
    			setTipsError(passwordFormatTips, "密码由数字或者字母组成");
    			validPassword = false;
    			repeatPassword.setText("");
    			repeatPassword.setDisable(true);
    			repeatPasswordFormatTips.setVisible(false);
    			return false;
    		}
    	}
        setTipsOk(passwordFormatTips);
        validPassword = true;
        repeatPassword.setDisable(false);
        return true;
    }

    // TODO: �?查重复密码是否一致，若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkRepeatPassword(){
        /*if(this.repeatPassword.getText().isEmpty()){
            if(signUpCall){
                // TODO
            }
            else{
                // TODO
            }
            return false;
        }
        else{
            // TODO
        }*/
    	if(signUpCall) {
    		if(validPassword && (repeatPassword.getText().isEmpty() || repeatPassword.getText() == null || "".equals(repeatPassword.getText()))) {
    			setTipsError(repeatPasswordFormatTips,"请确认密码！");
    			passwordFormatTips.setVisible(true);
    			return false;
    		}
    		if(validPassword && (!password.getText().isEmpty() && !repeatPassword.getText().equals(password.getText()))) {
    			setTipsError(repeatPasswordFormatTips,"重复密码错误�?");
    			return false;
    		}
    		return true;
    	}else {
    		if(repeatPassword.getText().isEmpty() ||  repeatPassword.getText() == null || "".equals(repeatPassword.getText())) {
    			repeatPasswordFormatTips.setVisible(false);
    			return false;
    		}
    		if(!password.getText().isEmpty() && !repeatPassword.getText().equals(password.getText())) {
    			setTipsError(repeatPasswordFormatTips,"重复密码错误！");
    			
    			return false;
    		}
    	}
        setTipsOk(repeatPasswordFormatTips);
        return true;
    }

    // TODO: �?查生日格式：月份天数是否正常。若无效，设置相�? Lable 的文本并返回 false
    @FXML
    private boolean checkDate(){
       /* if (this.year.getValue() == null
                || this.month.getValue() == null
                || this.day.getValue() == null){
            if(signUpCall){
                // TODO
            }
            else{
                // TODO
            }
            return false;
        }
        else {
            // TODO
        }*/
        
        	if(this.year.getValue() == null || this.month.getValue() == null || this.day.getValue() == null) {
        		setTipsError(dateFormatTips, "请选择择生日!");
        		return false;
        	}	
        setTipsOk(dateFormatTips);
        return true;
    }

    private boolean checkAccountExist() throws Exception {
        String account = this.account.getText();
        String json = encoder.chechAccountRequest(account);
        System.out.println("account check: " + json);
        String msg = client.getConnect().sendAndReceive(json);
        // TODO: chech
        if("true".equals(msg))
        	return false;
        return true;
    }
    private boolean isNameExist() throws Exception {
    	String name = this.nickname.getText();
    	String json =encoder.checkNameRequest(name);
    	System.out.println("name_check: " + json);
    	String msg = client.getConnect().sendAndReceive(json);
    	if("true".equals(msg))
    		return false;
    	return true;
    }
    @FXML
    private void synchronousCheck() throws Exception {
        validInfo = true;
        if (!checkAccount2()){
            validInfo = false;
        }
        if (!checkName2()){
            validInfo = false;
        }
        if (!checkPassword()){
            validInfo = false;
        }
        if (!checkRepeatPassword()){
            validInfo = false;
        }
        if (!checkDate()){
            validInfo = false;
        }
    }

    @FXML
    private void setTipsOk(Label tip){
        tip.setVisible(true);
        tip.setTextFill(Color.GREEN);
        tip.setText("✔");
    }

    @FXML
    private void setTipsError(Label tip, String msg){
        tip.setVisible(true);
        tip.setTextFill(Color.RED);
        tip.setText(msg);
    }

    @FXML
    private void selectMale(){
        /************* test ********************/
        //System.out.println("male");
        /************* test ********************/

        /************* release *****************/
        user.setSex("male");
        /************* release *****************/
    }

    @FXML
    private void selectFemale(){
        /************* test ********************/
        //System.out.println("female");
        /************* test ********************/

        /************* release *****************/
        user.setSex("female");
        /************* release *****************/
    }

    @FXML
    private void backToLogin() throws Exception{
    	client.getsignupStage().close();
        client.gotoLogin();
    }

    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    private void initComboBox(){
        year.setItems(FXCollections.observableArrayList());
        month.setItems(FXCollections.observableArrayList());
        day.setItems(FXCollections.observableArrayList());
        for(int y = 1970; y <= 2017; ++y){
            year.getItems().add(y);
        }
        for(int m = 1; m <= 12; ++m){
            month.getItems().add(m);
        }
        for(int d = 1; d <= 31; ++d){
            day.getItems().add(d);
        }
        year.setVisibleRowCount(8);
        month.setVisibleRowCount(8);
        day.setVisibleRowCount(8);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ToggleGroup tg = new ToggleGroup();
        initComboBox();
        male.setToggleGroup(tg);
        female.setToggleGroup(tg);
        male.setSelected(true);
        user.setSex("male");
        //year.getSelectionModel().select(30);
        //month.getSelectionModel().select(0);
        //day.getSelectionModel().select(0);
        /*account.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				 statusBarLabel.setText("状�?�：当前字符数为�?" + textField.getText().length());
			}

		});*/
        /*textField.hoverProperty().addListener(new InvalidationListener() {
        	@Override
        	public void invalidated(Observable observable) {
        	if(textField.isHover()){
        	textField.setText("textField listener added");
        	}else{
        	textField.setText("textField listener remove");
        	}
        	}
        	});*/
       /* account.hoverProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				// TODO Auto-generated method stub
				if(account.isHover()){
					account.setText("textField listener added");
					}else{
					account.setText("textField listener remove");
					}
			}
		});*/
       account.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
           if (newPropertyValue)
           {
        	   
           }
           else
           {
               /*if(account.getText() == "" && Integer.parseInt(account.getText()) > 12)
               {

               }*/
               /*account.setText("12");
               System.out.println("Textfield 1 out focus");*/
        	   try {
				checkAccount2();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }


       });
       nickname.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
           if (newPropertyValue)
           {
        	   
           }
           else
           {
              
        	   try {
        		   checkName2();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }

       });
       password.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
           if (newPropertyValue)
           {
        	   
           }
           else
           {
              
        	   try {
        		   checkPassword();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }

       });
       repeatPassword.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
           if (newPropertyValue)
           {
        	   
           }
           else
           {
              
        	   try {
        		   checkRepeatPassword();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }

       });
       repeatPassword.setDisable(true);
        accountFormatTips.setVisible(false);
        nameFormatTips.setVisible(false);
        passwordFormatTips.setVisible(false);
        repeatPasswordFormatTips.setVisible(false);
        dateFormatTips.setVisible(false);
    }
}
