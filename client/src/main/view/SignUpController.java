package src.main.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import src.main.UserInfo;
import src.main.Client;
import src.main.communication.Connect;
import src.main.communication.Encoder;
//import src.main.communication.Connect;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by touhoudoge on 2017/3/23.
 */
public class SignUpController implements Initializable {

    private Client client;

    private UserInfo user = new UserInfo();
    private Encoder encoder = new Encoder();
    //private Connect  connector = new Connect();

    private boolean validInfo = true;
    private boolean signUpCall = false;

    // User input information
    @FXML
    private TextField account;

    @FXML
    private TextField nickname;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField repeatPassword;

    @FXML
    private ComboBox year;
    @FXML
    private ComboBox month;
    @FXML
    private ComboBox day;

    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;

    // Hint tag
    @FXML
    private Label accountFormatTips;
    @FXML
    private Label nameFormatTips;
    @FXML
    private Label passwordFormatTips;
    @FXML
    private Label repeatPasswordFormatTips;
    @FXML
    private Label dateFormatTips;
    private boolean validAccount = false;
    private boolean validName = false;
    private boolean validPassword = false;
    private boolean validRepeatPassowrd = false;

    public static boolean accountExist;
    public static boolean registSuccess;

    @FXML
    private void goToLogin() throws Exception {
        /************* test ********************/
        //client.gotoLobby();
        /************* test ********************/

        /************* release *****************/
        signUpCall = true;
        synchronousCheck();
        signUpCall = false;
        if (validInfo) {
            String ac = this.account.getText();
            String nn = this.nickname.getText();
            String pw = this.password.getText();
            String rpw = this.repeatPassword.getText();
            int year = Integer.parseInt(this.year.getValue().toString());
            int month = Integer.parseInt(this.month.getValue().toString());
            int day = Integer.parseInt(this.day.getValue().toString());
            user.setAccount(ac);
            user.setNickname(nn);
            user.setPassword(pw);
            user.setBirthday(year, month, day);
            client.setAccount(user);
            String json = encoder.signUpRequest(user);
            System.out.println("user signup info: " + json);
            //client.getConnect().send(json);
            while (!Connect.recv){
                Thread.currentThread().sleep(100);
            }
            if(registSuccess){
                client.getsignupStage().close();
                client.getPrimaryStage().show();
            }
        }
        /************* release *****************/
    }

    // TODO: �?查账号格式：16个字符，只能是字母�?�数字或下划�?'_'。若无效，设置相�? Lable 的文本并返回 false
    @FXML
    private boolean checkAccount() throws Exception {
        if (this.account.getText().isEmpty()) {
            if (signUpCall) {
                setTipsError(accountFormatTips, "账号不能为空");
            } else {
                accountFormatTips.setVisible(false);
            }
            return false;
        } else {
            if (!accountIsExist()) {
                setTipsError(accountFormatTips, "该账号已被注册");
                return false;
            }
            // TODO
        }
        setTipsOk(accountFormatTips);
        return true;
    }

    @FXML
    private boolean checkAccountOK() throws Exception {
        if (signUpCall) {
            if (this.account.getText().isEmpty() || this.account.getText() == null || "".equals(this.account.getText())) {
                setTipsError(accountFormatTips, "账号不能为空");
                return false;
            }
            return validAccount;
        } else {
            if (this.account.getText().isEmpty() || this.account.getText() == null || "".equals(this.account.getText())) {
                accountFormatTips.setVisible(false);
                return false;
            }
            if (accountIsExist()) {
                setTipsError(accountFormatTips, "账号已被注册");
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
    private boolean checkName() {
        if (this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())) {
            if (signUpCall) {
                setTipsError(nameFormatTips, "昵称不能为空");
            } else {
                nameFormatTips.setVisible(false);
            }
            return false;
        } else {
            // TODO
        }
        setTipsOk(nameFormatTips);
        return true;
    }

    @FXML
    private boolean checkNameOK2() throws Exception {
        if (signUpCall) {
            if (this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())) {
                setTipsError(nameFormatTips, "昵称不能为空");
                return false;
            }
            return validName;
        } else {
            if (this.nickname.getText().isEmpty() || this.nickname.getText() == null || "".equals(this.nickname.getText())) {
                nameFormatTips.setVisible(false);
                return false;
            }
//            if (isNameExist()) {
//                setTipsError(nameFormatTips, "该昵称已经被使用�?");
//                validName = false;
//                return false;
//            }
        }
        setTipsOk(nameFormatTips);
        validName = true;
        return true;
    }

    // TODO: �?查密码格式：长度 8-16 位，若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkPasswordOK() {
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
        if (signUpCall) {
            if (password.getText().isEmpty()) {
                setTipsError(passwordFormatTips, "密码不能为空");
                passwordFormatTips.setVisible(true);

                return false;
            }
            return validPassword;
        } else {
            repeatPasswordFormatTips.setVisible(false);
            repeatPassword.setText("");
            if (password.getText().isEmpty() || password.getText() == null || "".equals(password.getText())) {
                passwordFormatTips.setVisible(false);
                repeatPassword.setText("");
                repeatPassword.setDisable(true);
                repeatPasswordFormatTips.setVisible(false);
                validPassword = false;
                return false;
            }
            if (password.getText().length() < 6) {
                setTipsError(passwordFormatTips, "密码至少6位");
                validPassword = false;
                repeatPassword.setText("");
                repeatPassword.setDisable(true);
                repeatPasswordFormatTips.setVisible(false);
                validPassword = false;
                return false;
            } else if (!password.getText().matches("[a-zA-Z0-9]+")) {
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
    private boolean checkRepeatPasswordOK() {
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
        if (signUpCall) {
            if (validPassword && (repeatPassword.getText().isEmpty() || repeatPassword.getText() == null || "".equals(repeatPassword.getText()))) {
                setTipsError(repeatPasswordFormatTips, "请确认密码");
                passwordFormatTips.setVisible(true);
                return false;
            }
            if (validPassword && (!password.getText().isEmpty() && !repeatPassword.getText().equals(password.getText()))) {
                setTipsError(repeatPasswordFormatTips, "两次密码不一致");
                return false;
            }
            return true;
        } else {
            if (repeatPassword.getText().isEmpty() || repeatPassword.getText() == null || "".equals(repeatPassword.getText())) {
                repeatPasswordFormatTips.setVisible(false);
                return false;
            }
            if (!password.getText().isEmpty() && !repeatPassword.getText().equals(password.getText())) {
                setTipsError(repeatPasswordFormatTips, "两次密码不一致");

                return false;
            }
        }
        setTipsOk(repeatPasswordFormatTips);
        return true;
    }

    // TODO: �?查生日格式：月份天数是否正常。若无效，设置相�? Lable 的文本并返回 false
    @FXML
    private boolean checkDateOK() {
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

        if (this.year.getValue() == null || this.month.getValue() == null || this.day.getValue() == null) {
            setTipsError(dateFormatTips, "请选择择生日");
            return false;
        }
        setTipsOk(dateFormatTips);
        return true;
    }

    private boolean accountIsExist() throws Exception {
        accountExist = false;
        String account = this.account.getText();
        String json = encoder.chechAccountRequest(account);
        System.out.println("account check: " + json);
        return accountExist;
    }

//    private boolean isNameExist() throws Exception {
//        String name = this.nickname.getText();
//        String json = encoder.checkNameRequest(name);
//        System.out.println("name_check: " + json);
//        String msg = client.getConnect().sendAndReceive(json);
//        if ("true".equals(msg))
//            return false;
//        return true;
//    }

    @FXML
    private void synchronousCheck() throws Exception {
        validInfo = true;
        if (!checkAccountOK()) {
            validInfo = false;
        }
        if (!checkNameOK2()) {
            validInfo = false;
        }
        if (!checkPasswordOK()) {
            validInfo = false;
        }
        if (!checkRepeatPasswordOK()) {
            validInfo = false;
        }
        if (!checkDateOK()) {
            validInfo = false;
        }
    }

    @FXML
    private void setTipsOk(Label tip) {
        tip.setVisible(true);
        tip.setTextFill(Color.GREEN);
        tip.setText("✔");
    }

    @FXML
    private void setTipsError(Label tip, String msg) {
        tip.setVisible(true);
        tip.setTextFill(Color.RED);
        tip.setText(msg);
    }

    @FXML
    private void selectMale() {
        /************* test ********************/
        //System.out.println("male");
        /************* test ********************/

        /************* release *****************/
        user.setSex("male");
        /************* release *****************/
    }

    @FXML
    private void selectFemale() {
        /************* test ********************/
        //System.out.println("female");
        /************* test ********************/

        /************* release *****************/
        user.setSex("female");
        /************* release *****************/
    }

    @FXML
    private void backToLogin() throws Exception {
        client.getsignupStage().close();
        client.gotoLogin();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void initComboBox() {
        year.setItems(FXCollections.observableArrayList());
        month.setItems(FXCollections.observableArrayList());
        day.setItems(FXCollections.observableArrayList());
        for (int y = 1970; y <= 2017; ++y) {
            year.getItems().add(y);
        }
        for (int m = 1; m <= 12; ++m) {
            month.getItems().add(m);
        }
        for (int d = 1; d <= 31; ++d) {
            day.getItems().add(d);
        }
        year.setVisibleRowCount(8);
        month.setVisibleRowCount(8);
        day.setVisibleRowCount(8);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            if (newPropertyValue) {

            } else {
               /*if(account.getText() == "" && Integer.parseInt(account.getText()) > 12)
               {

               }*/
               /*account.setText("12");
               System.out.println("Textfield 1 out focus");*/
                try {
                    checkAccountOK();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        });
        nickname.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {

            } else {

                try {
                    checkNameOK2();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        password.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {

            } else {

                try {
                    checkPasswordOK();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        repeatPassword.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {

            } else {

                try {
                    checkRepeatPasswordOK();
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
