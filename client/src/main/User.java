package src.main;

public class User {

    class Birthday{
        public int year;
        public int month;
        public int day;
    }

    class GameData{
        public int win;
        public int lose;
        public int draw;
        public int segment;
        public int rank;

        public GameData(){
            win = 0;
            lose = 0;
            draw = 0;
            segment = 0;
            rank = 1000;
        }
    }

    String account;
    String nickname;
    String password;
    Birthday birthday = new Birthday();
    boolean sex;  // true for man, false for woman
    int state;
    int room;
    GameData data = new GameData();

    public User(){
        state = Type.State.OTHER;
        room = 0;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setSex(boolean sex){
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

    public boolean getSex(){
        return this.sex;
    }
}

