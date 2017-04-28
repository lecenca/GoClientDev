
package src.main;

/**
 * Created by touhoudoge on 2017/3/25.
 */

public class User {

    class Birthday{
        int year;
        int month;
        int day;
    }

    private String account;
    private String nickname;
    private String password;
    private Birthday birthday = new Birthday();
    private String sex;
    private int level;
    private int integral;
    private String status;
    //int state;
    //int roomId;


    public void setAccount(String account) {
        this.account = account;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setBirthday(int year,int month,int day){
        this.birthday.year = year;
        this.birthday.month = month;
        this.birthday.day = day;
    }

    public String getAccount() {
        return this.account;
    }
    public String getNickname(){
        return this.nickname;
    }

    public String getPassword(){
        return this.password;
    }

    public String getBirthday(){
        return String.format("%4d-%02d-%02d",this.birthday.year,this.birthday.month,this.birthday.day);
    }

    public String getSex(){
        return this.sex;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}

