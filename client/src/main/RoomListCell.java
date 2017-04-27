package src.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by touhoudoge on 2017/4/27.
 */
public class RoomListCell {

    private Room room;
    private SimpleIntegerProperty roomId = new SimpleIntegerProperty();
    private SimpleStringProperty roomName = new SimpleStringProperty();
    private SimpleStringProperty player01 = new SimpleStringProperty();
    private SimpleStringProperty player02 = new SimpleStringProperty();
    private SimpleIntegerProperty state = new SimpleIntegerProperty();

    public RoomListCell(){}

    public RoomListCell(Room room){
        this.room = room;
    }

    public Room getRoom(){
        return room;
    }

    public int getRoomId()
    {
        return roomId.get();
    }

    public SimpleIntegerProperty roomIdProperty()
    {
        return roomId;
    }

    public void setRoomId(int roomId)
    {
        this.roomId.set(roomId);
    }

    public String getRoomName()
    {
        return roomName.get();
    }

    public SimpleStringProperty roomNameProperty()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName.set(roomName);
    }

    public String getPlayer01()
    {
        return player01.get();
    }

    public SimpleStringProperty player01Property()
    {
        return player01;
    }

    public void setPlayer01(String player01)
    {
        this.player01.set(player01);
    }

    public String getPlayer02()
    {
        return player02.get();
    }

    public SimpleStringProperty player02Property()
    {
        return player02;
    }

    public void setPlayer02(String player02)
    {
        this.player02.set(player02);
    }

    public int getState()
    {
        return state.get();
    }

    public SimpleIntegerProperty stateProperty()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state.set(state);
    }

	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}
}
