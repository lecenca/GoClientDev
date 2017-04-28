package src.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    class Birthday {
        public int year;
        public int month;
        public int day;
    }

    class GameData {
        public int win;
        public int lose;
        public int draw;
        public int level;
        public int rank;

        public GameData() {
            win = 0;
            lose = 0;
            draw = 0;
            level = 0;
            rank = 1000;
        }

		public void setWin(int win) {
			this.win = win;
		}

		public void setLose(int lose) {
			this.lose = lose;
		}

		public void setLevel(int level) {
			this.level = level;
		}
        
    }

    private String account;
    private String nickname;
    private String password;
    private Birthday birthday = new Birthday();
    //private int level;
    //private int integral;
    //private String status;
    boolean sex;  // true for man, false for woman
    int state;
    int room;
    GameData data = new GameData();

    public User() {
        state = Type.State.OTHER;
        room = 0;
    }
    public User(String nickname,int level,int win,int lose,int state) {
    	this.nickname = nickname;
    	data.setLevel(level);
    	data.setWin(win);
    	data.setLose(lose);
    	this.state = state;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(int year, int month, int day) {
        this.birthday.year = year;
        this.birthday.month = month;
        this.birthday.day = day;
    }

    public String getAccount() {
        return this.account;
    }

    public String getNickname() {
        return this.nickname;
    }
    public StringProperty getNicknameProperty() {
        return new SimpleStringProperty(nickname);
    }
    public String getPassword() {
        return this.password;
    }

    public String getBirthday() {
        return String.format("%4d-%02d-%02d", this.birthday.year, this.birthday.month, this.birthday.day);
    }

    public boolean getSex() {
        return this.sex;
    }

    public int getState() {
        return this.state;
    }

    public int getRoom() {
        return this.room;
    }

    public int getLevel() {
        return this.data.level;
    }
    public StringProperty getLevelProperty(){
        String[] level = {"十八级","十七级","十六级","十五级","十四级","十三级","十二级","十一级","十级"
                ,"九级","八级","七级","六级","五级","四级","三级","二级","一级",
        "一段","二段","三段","四段","五段","六段","七段","八段","九段"};
        return new SimpleStringProperty(level[this.data.level]);
    }
    public StringProperty getStateProperty(){
        if(this.state == Type.State.IDLE){
            return new SimpleStringProperty("闲逛中");
        }
        if(this.state == Type.State.READY){
            return new SimpleStringProperty("在房间"+Integer.toString(this.room)+"准备中");
        }
        return new SimpleStringProperty("在房间"+Integer.toString(this.room)+"游戏中");
    }
    public StringProperty getWinProperty() {
    	return new SimpleStringProperty(Integer.toString(this.data.win));
    }
    public StringProperty getLoseProperty() {
    	return new SimpleStringProperty(Integer.toString(this.data.lose));
    }
    
}

