package src.main;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Room {
    private int roomId;
    private String roomName;
    private UserInfo player01;
    private UserInfo player02;
    private int state;
    private String password;

    public Room(){}

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public UserInfo getPlayer01() {
        return player01;
    }

    public void setPlayer01(UserInfo player01) {
        this.player01 = player01;
    }

    public UserInfo getPlayer02() {
        return player02;
    }

    public void setPlayer02(UserInfo player02) {
        this.player02 = player02;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
