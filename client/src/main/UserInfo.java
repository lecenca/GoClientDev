package src.main;

/**
 * Created by touhoudoge on 2017/3/25.
 */

public class UserInfo {
    class Birthday{
        int year;
        int month;
        int day;
    }

    String account;
    String nickname;
    String password;
    Birthday birthday = new Birthday();
    String sex;

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
}
