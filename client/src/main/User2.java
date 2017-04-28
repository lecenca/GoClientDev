package src.main;

import com.sun.xml.internal.fastinfoset.algorithm.IntegerEncodingAlgorithm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringProperty;
import src.main.User.Birthday;

public class User2 {
	class Birthday {
		int year;
		int month;
		int day;
	}

	private StringProperty account2;
	private StringProperty nickname2;
	private StringProperty password2;
	private Birthday birthday = new Birthday();
	private StringProperty sex2;
	private StringProperty level2;
	private StringProperty integral2;
	private StringProperty status2;
	public User2(){}
	public User2(String nickname,String level2,String integral2,String status2){
		this.nickname2 = new SimpleStringProperty(nickname);
		this.level2 = new SimpleStringProperty(level2);
		this.integral2 = new SimpleStringProperty(integral2);
		this.status2 = new SimpleStringProperty(status2);
	}
	public StringProperty getAccount2() {
		return account2;
	}
	public void setAccount2(String account2) {
		this.account2.set(account2);
	}
	public StringProperty getNickname2() {
		return nickname2;
	}
	public void setNickname2(String nickname2) {
		this.nickname2.set(nickname2);
	}
	public StringProperty getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2.set(password2);
	}
	public StringProperty getSex2() {
		return sex2;
	}
	public void setSex2(String sex2) {
		this.sex2.set(sex2);
	}
	public StringProperty getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2.set(level2);
	}
	public StringProperty getIntegral2() {
		return integral2;
	}
	public void setIntegral2(String integral2) {
		this.integral2.set(integral2);
	}
	public StringProperty getStatus2() {
		return status2;
	}
	public void setStatus2(String status2) {
		this.status2.set(status2);
	}
}
