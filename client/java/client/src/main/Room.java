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

public class Room {

    private class Config {
        public int komi;          // 贴目规则
        public int mainTime;     // 主时间 [1,5,10,15,20,30,40,60,90](单位：分)
        public int period;       // 读秒时间 [15,20,30,40,50,60]（单位：秒）
        public int periodTimes; // 读秒次数 [1,3,5,7,10]

        public Config() {
            this.komi = 0;
            this.mainTime = 0;
            this.period = 0;
            this.periodTimes = 0;
        }

        public int getKomi() {
            return komi;
        }

        public void setKomi(int komi) {
            this.komi = komi;
        }

        public int getMainTime() {
            return mainTime;
        }

        public void setMainTime(int mainTime) {
            this.mainTime = mainTime;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getPeriodTimes() {
            return periodTimes;
        }

        public void setPeriodTimes(int periodTimes) {
            this.periodTimes = periodTimes;
        }

    }

    private int id = 0;
    private String name = new String();
    private String player1 = new String();
    private String player2 = new String();
    private Config config = new Config();
    private int state = Type.RoomState.NONE;

    private static String[] komi = {"让先", "黑贴3.5目", "黑贴6.5目"};
    private static String[] mainTime = {"1分", "5分", "10分", "15分", "20分", "30分", "40分", "60分", "90分"};
    private static String[] period = {"15秒", "20秒", "30秒", "40秒", "50秒", "60秒"};
    private static String[] periodTimes = {"1次", "3次", "5次", "7次", "10次"};
    private static String[] roomState = {"等待中", "准备中", "对战中"};
    private static int[] corMainTime = {1, 5, 10, 15, 20, 30, 40, 60, 90};
    private static int[] corPeriodTime = {15, 20, 30, 40, 50, 60};
    private static int[] corTimes = {1, 3, 5, 7, 10};

    public Room() {
    }

    public boolean hasSeat() {
        return player1.isEmpty() || player2.isEmpty();
    }

    public int playerNumber() {
        int num = 0;
        if (!player1.isEmpty()) {
            num++;
        }
        if (!player2.isEmpty()) {
            num++;
        }
        return num;
    }

    public String getPlayer1Name() {
        if (!player1.isEmpty()) {
            if (Client.playersMap.get(this.player1) != null) {
                return Client.playersMap.get(this.player1).getNickname();
            }
        }
        return "";
    }

    public String getPlayer2Name() {
        if (!player2.isEmpty()) {
            if (Client.playersMap.get(this.player2) != null) {
                return Client.playersMap.get(this.player2).getNickname();
            }
        }
        return "";
    }

    public String getKomiString() {
        return komi[this.config.komi];
    }

    public int getKomi() {
        return config.komi;
    }

    public int getMainTime() {
        return corMainTime[this.config.mainTime];
    }

    public int getPeriodTime() {
        return corPeriodTime[this.config.period];
    }

    public int getPeriodTimes() {
        return corTimes[this.config.periodTimes];
    }

    public String getPlayer1Form(Room room) {
        return room.player1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setConfig(int komi, int mainTime, int period, int periodTimes) {
        this.config.komi = komi;
        this.config.mainTime = mainTime;
        this.config.period = period;
        this.config.periodTimes = periodTimes;
    }

    public StringProperty getIdProperty() {
        return new SimpleStringProperty(Integer.toString(this.id));
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public StringProperty getPlayer1Property() {
        return new SimpleStringProperty(getPlayer1Name());
    }

    public StringProperty getPlayer2Property() {
        return new SimpleStringProperty(getPlayer2Name());
    }

    public StringProperty getStatesProperty() {
        return new SimpleStringProperty(roomState[this.state]);
    }

    public StringProperty getConfigProperty() {
        return new SimpleStringProperty(
                komi[this.config.komi] + " / " +
                        mainTime[this.config.mainTime] + " / " +
                        period[this.config.period] +
                        periodTimes[this.config.periodTimes]);
    }

    @Override
    public int hashCode() {
        final int number = 24;
        return name.hashCode() + id * number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Room))
            throw new ClassCastException();
        Room room = (Room) obj;
        return this.id == room.id;
    }


}
