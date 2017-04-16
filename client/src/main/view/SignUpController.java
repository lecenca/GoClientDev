package src.main.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import src.main.UserInfo;
import src.main.Client;
import src.main.communication.Encode;
import src.main.communication.Connect;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by touhoudoge on 2017/3/23.
 */
public class SignUpController implements Initializable{

    private Client client;

    private UserInfo user       = new UserInfo();
    private Encode   encoder   = new Encode();
    private Connect  connector = new Connect();

    private boolean validInfo;
    private boolean signUpCall;

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

    @FXML
    private void signIn() throws Exception{
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
            //connector.send(json);
        }
        /************* release *****************/
    }

    // TODO: 检查账号格式：16个字符，只能是字母、数字或下划线'_'。若无效，设置相关 Lable 的文本并返回 false
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
                setTipsError(accountFormatTips,"该账号已被注册");
                return false;
            }
            // TODO
        }
        setTipsOk(accountFormatTips);
        return true;
    }

    // TODO: 检查昵称：不超过12个显示字符（一个汉字占2个字符，非汉字占1个字符）只支持汉字、字母、数字和下划线'_'。若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkName(){
        if(this.nickname.getText().isEmpty()){
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

    // TODO: 检查密码格式：长度 8-16 位，若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkPassword(){
        if(this.password.getText().isEmpty()){
            if(signUpCall){
                // TODO
            }
            else{
                // TODO
            }
            return false;
        }
        else{

        }
        setTipsOk(passwordFormatTips);
        return true;
    }

    // TODO: 检查重复密码是否一致，若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkRepeatPassword(){
        if(this.repeatPassword.getText().isEmpty()){
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
        }
        setTipsOk(repeatPasswordFormatTips);
        return true;
    }

    // TODO: 检查生日格式：月份天数是否正常。若无效，设置相关 Lable 的文本并返回 false
    @FXML
    private boolean checkDate(){
        if (this.year.getValue() == null
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
        }
        setTipsOk(dateFormatTips);
        return true;
    }

    private boolean checkAccountExist() throws Exception {
        String account = this.account.getText();
        String json = encoder.chechAccountRequest(account);
        System.out.println("account check: " + json);
        //connector.send(json);
        // TODO: chech
        return true;
    }

    @FXML
    private void synchronousCheck() throws Exception {
        validInfo = true;
        if (!checkAccount()){
            validInfo = false;
        }
        if (!checkName()){
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
        accountFormatTips.setVisible(false);
        nameFormatTips.setVisible(false);
        passwordFormatTips.setVisible(false);
        repeatPasswordFormatTips.setVisible(false);
        dateFormatTips.setVisible(false);
    }
}
