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

package src.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private class Birthday {
        public int year;
        public int month;
        public int day;
    }

    private class GameData {
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

        public int getWin() {
            return win;
        }

        public void setWin(int win) {
            this.win = win;
        }

        public int getLose() {
            return lose;
        }

        public void setLose(int lose) {
            this.lose = lose;
        }

        public int getDraw() {
            return draw;
        }

        public void setDraw(int draw) {
            this.draw = draw;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

    }

    private int priority;
    private String account = new String();
    private String nickname = new String();
    private String password = new String();
    private Birthday birthday = new Birthday();
    private boolean sex;  // true for man, false for woman
    private int state;
    private int room;
    private GameData data = new GameData();

    private static String[] level = {"十八级", "十七级", "十六级", "十五级", "十四级", "十三级", "十二级", "十一级", "十级"
            , "九级", "八级", "七级", "六级", "五级", "四级", "三级", "二级", "一级",
            "一段", "二段", "三段", "四段", "五段", "六段", "七段", "八段", "九段"};

    /********** test **********/
    public User() {
        account = "1";
        nickname = "小明";
        password = "123456";
        state = Type.UserState.IDLE;
        room = 0;
    }
    /********** test **********/

    public User(String account, String nickname) {
        this.account = account;
        this.nickname = nickname;
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

    public String getPassword() {
        return this.password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public StringProperty getNicknameProperty() {
        return new SimpleStringProperty(nickname);
    }

    public boolean getSex() {
        return this.sex;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRoom() {
        return this.room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public void updateGameDate(double point) {
        if (Math.abs(point) < 0.001) {
            ++data.draw;
        } else if (point > 0) {
            ++data.win;
        } else {
            ++data.lose;
        }
        data.rank += (int) (point / 3.0);
        adjustLevel();
    }

    private void adjustLevel() {
        int level = (this.data.rank - 1000) / 50;
        if (level < 0) {
            level = 0;
        } else if (level > 26) {
            level = 26;
        }
        this.data.level = level;
    }

    public int getLevel() {
        return this.data.level;
    }

    public int getRank() {
        return this.data.rank;
    }

    public int getWin() {
        return this.data.win;
    }

    public int getLose() {
        return this.data.lose;
    }

    public int getDraw() {
        return this.data.draw;
    }

    public String getLevelString() {
        return level[this.data.level];
    }

    public StringProperty getLevelProperty() {
        return new SimpleStringProperty(level[this.data.level]);
    }

    public GameData getData() {
        return data;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    public StringProperty getStateProperty() {
        if (this.state == Type.UserState.IDLE) {
            return new SimpleStringProperty("闲逛中");
        }
        if (this.state == Type.UserState.READY) {
            return new SimpleStringProperty("在房间" + Integer.toString(this.room) + "准备中");
        }
        return new SimpleStringProperty("在房间" + Integer.toString(this.room) + "游戏中");
    }

    public StringProperty getWinProperty() {
        return new SimpleStringProperty(Integer.toString(this.data.win));
    }

    public StringProperty getLoseProperty() {
        return new SimpleStringProperty(Integer.toString(this.data.lose));
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return account.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            throw new ClassCastException();
        User user = (User) obj;
        //System.out.println("this.accout:" + this.account + " " + "user.account: " + user.account + " " + this.account.equals(user.account));
        return this.account.equals(user.account);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}

