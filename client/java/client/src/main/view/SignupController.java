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

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import src.main.Client;
import src.main.Type;
import src.main.User;
import src.main.communication.Connect;
import src.main.communication.Encoder;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    private Client client;

    private User user = new User();

    private boolean validInfo = false;
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

    @FXML
    private AnchorPane signupPane;

    private boolean validAccount = false;
    private boolean validName = false;
    private boolean validPassword = false;

    public static boolean accountCheckSuccess;
    public static boolean registSuccess;
    public static boolean hasCheckedAccount = true;
    private static int[] map = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @FXML
    private void signup() {
        if (!hasCheckedAccount) {
            checkAccountValid();
        }
        signUpCall = true;
        synchronousCheck();
        signUpCall = false;
        if (validInfo) {
            if (Connect.interrupted()) {
                return;
            }
            String ac = this.account.getText();
            String nn = this.nickname.getText();
            String pw = this.password.getText();
            int year = Integer.parseInt(this.year.getValue().toString());
            int month = Integer.parseInt(this.month.getValue().toString());
            int day = Integer.parseInt(this.day.getValue().toString());
            user.setAccount(ac);
            user.setNickname(nn);
            user.setPassword(pw);
            user.setBirthday(year, month, day);
            client.setUser(user);
            String json = Encoder.signupRequest(user);
            client.getConnect().send(json);
            Connect.waitForRec(Type.Response.REGIST_SUCCESS, Type.Response.REGIST_FAILED);
            if (registSuccess) {
                client.getSignupStage().close();
                client.gotoLogin();
            } else {
                JOptionPane.showMessageDialog(null, "服务器发生未知错误，请重试");
            }
        }
    }

    @FXML
    private void synchronousCheck() {
        validInfo = true;
        if (!checkAccountValid()) {
            validInfo = false;
        }
        if (!checkNameValid()) {
            validInfo = false;
        }
        if (!checkPasswordValid()) {
            validInfo = false;
        }
        if (!checkRepeatPasswordValid()) {
            validInfo = false;
        }
        if (!checkDateValid()) {
            validInfo = false;
        }
    }

    @FXML
    public boolean checkAccountValid() {
        String text = this.account.getText();
        if (signUpCall) {
            if (text.isEmpty() || text == null || "".equals(text)) {
                setTipsError(accountFormatTips, "账号不能为空");
                return false;
            }
            return validAccount;
        } else {
            if (text.isEmpty() || text == null || "".equals(text)) {
                accountFormatTips.setVisible(false);
                return false;
            }
            if (!keyPressCheckValid()) {
                return false;
            }
            if (!accountNotExist()) {
                if (Connect.timeout) {
                    Connect.timeout = false;
                    hasCheckedAccount = false;
                    return false;
                }
                setTipsError(accountFormatTips, "账号已被注册");
                validAccount = false;
                return false;
            }
        }
        setTipsOk(accountFormatTips);
        validAccount = true;
        return true;
    }

    @FXML
    private boolean keyPressCheckValid() {
        String text = this.account.getText();
        String regex = "[\\w]+";
        int length = text.length();
        if (!(text.matches(regex))) {
            setTipsError(accountFormatTips, "含有非法字符");
            validAccount = false;
            return false;
        }
        if (!(length >= 6 && length <= 16)) {
            setTipsError(accountFormatTips, "长度为6-16个字符");
            validAccount = false;
            return false;
        }
        setTipsOk(accountFormatTips);
        return true;
    }

    private boolean accountNotExist() {
        if (!Connect.hasConnect()) {
            return false;
        }
        accountCheckSuccess = false;
        String account = this.account.getText();
        String json = Encoder.chechAccountRequest(account);
        Connect.send(json);
        Connect.waitForRec(Type.Response.CHECK_ACCOUNT_SUCCESS, Type.Response.CHECK_ACCOUNT_FAILED);
        return accountCheckSuccess;
    }

    @FXML
    private boolean checkNameValid() {
        String text = this.nickname.getText();
        String regex = "[\u4E00-\u9FA5\\w]+";
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            if (text.substring(i, i + 1).getBytes().length > 1)
                length++;
        }
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
            if (!(text.matches(regex))) {
                setTipsError(nameFormatTips, "含有非法字符");
                validName = false;
                return false;
            }
            if (length > 12) {
                setTipsError(nameFormatTips, "昵称过长");
                validName = false;
                return false;
            }
        }
        setTipsOk(nameFormatTips);
        validName = true;
        return true;
    }

    @FXML
    private boolean checkPasswordValid() {
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
            } else if (password.getText().length() > 16) {
                setTipsError(passwordFormatTips, "密码不超过16位");
                validPassword = false;
                repeatPassword.setText("");
                repeatPassword.setDisable(true);
                repeatPasswordFormatTips.setVisible(false);
                validPassword = false;
                return false;
            } else if (!password.getText().matches("\\w+")) {
                setTipsError(passwordFormatTips, "含有非法字符");
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

    @FXML
    private boolean checkRepeatPasswordValid() {
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

    @FXML
    private boolean checkDateValid() {
        if (signUpCall) {
            if (this.year.getValue() == null || this.month.getValue() == null || this.day.getValue() == null) {
                setTipsError(dateFormatTips, "请选择择生日");
                return false;
            }
        }
        if (this.year.getValue() != null && this.month.getValue() != null && this.day.getValue() != null) {
            int y = Integer.parseInt(this.year.getValue().toString());
            int m = Integer.parseInt(this.month.getValue().toString());
            int d = Integer.parseInt(this.day.getValue().toString());
            int limit = map[m];
            if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
                limit++;
            }
            if (d > limit) {
                setTipsError(dateFormatTips, "日期无效");
                return false;
            }
            setTipsOk(dateFormatTips);
            return true;
        }
        return true;
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
    private void backToLogin() {
        client.getSignupStage().close();
        client.gotoLogin();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void selectMale() {
        user.setSex(true);
    }

    @FXML
    private void selectFemale() {
        user.setSex(false);
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

    @FXML
    public void clearAcountTip() {
        accountFormatTips.setText("");
    }

    @FXML
    public void clearNicknameTip() {
        nameFormatTips.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup tg = new ToggleGroup();
        initComboBox();
        male.setToggleGroup(tg);
        female.setToggleGroup(tg);
        male.setSelected(true);
        selectMale();
        account.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {

            } else {
                try {
                    checkAccountValid();
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
                    checkNameValid();
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
                    checkPasswordValid();
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
                    checkRepeatPasswordValid();
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

        Image image = new Image("resources/image/bg002.jpg", 524, 420, false, true);
        BackgroundSize backgroundSize = new BackgroundSize(524, 420, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        signupPane.setBackground(background);

    }
}
