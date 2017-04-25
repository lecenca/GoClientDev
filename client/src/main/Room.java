package src.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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

    /************* test ********************/
    private SimpleStringProperty player01Property = new SimpleStringProperty();
    private SimpleStringProperty player02Property = new SimpleStringProperty();
    private SimpleIntegerProperty stateProperty = new SimpleIntegerProperty();
    /************* test ********************/

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

    /************* test ********************/
    public String getPlayer01Property() {return player01Property.get();}

    public SimpleStringProperty player01PropertyProperty()
    {
        return player01Property;
    }

    public void setPlayer01Property(String player01Property)
    {
        this.player01Property.set(player01Property);
    }

    public String getPlayer02Property()
    {
        return player02Property.get();
    }

    public SimpleStringProperty player02PropertyProperty()
    {
        return player02Property;
    }

    public void setPlayer02Property(String player02Property)
    {
        this.player02Property.set(player02Property);
    }

    public int getStateProperty()
    {
        return stateProperty.get();
    }

    public SimpleIntegerProperty statePropertyProperty()
    {
        return stateProperty;
    }

    public void setStateProperty(int stateProperty)
    {
        this.stateProperty.set(stateProperty);
    }

    /************* test ********************/
}
