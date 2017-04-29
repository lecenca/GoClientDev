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
    private SimpleStringProperty player1 = new SimpleStringProperty();
    private SimpleStringProperty player2 = new SimpleStringProperty();
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

    public String getPlayer1()
    {
        return player1.get();
    }

    public SimpleStringProperty player1Property()
    {
        return player1;
    }

    public void setPlayer1(String player01)
    {
        this.player1.set(player01);
    }

    public String getPlayer2()
    {
        return player2.get();
    }

    public SimpleStringProperty player2Property()
    {
        return player2;
    }

    public void setPlayer2(String player02)
    {
        this.player2.set(player02);
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
