package src.main;



import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by touhoudoge on 2017/4/16.
 */
public class Room {

    private class Config{
        public int komi;         // 贴目规则
        public int mainTime;    // 主时间 [1,5,10,15,20,30,40,60,90](单位：分)
        public int period;      // 读秒时间 [15,20,30,40,50,60]（单位：秒）
        public int periodTime; // 读秒次数 [1,3,5,7,10]
        public Config(){
            this.komi = Type.KOMI.SIX_FIVE;
            this.mainTime = 20;
            this.period = 30;
            this.periodTime = 3;
        }
    }

    private int id;
    private String name;
    private String password;
    private String player1;
    private String player2;
    private int state;
    private boolean player1Black;
    private Config config = new Config();

    /************* test ********************/
    /*
    private SimpleStringProperty player01Property = new SimpleStringProperty();
    private SimpleStringProperty player02Property = new SimpleStringProperty();
    private SimpleIntegerProperty stateProperty = new SimpleIntegerProperty();
    */
    /************* test ********************/

    public Room(){

    }
    //test
    public Room(int id,String name,String player1,String player2,int state) {
    	this.id = id;
    	this.name = name;
    	this.player1 = player1;
    	this.player2 = player2;
    	this.state = state;
    }
    public String getPlayer1Form(Room room){
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    /************* test ********************/
    /*
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
    */
    /************* test ********************/
   public StringProperty getIdProperty() {
	   return new SimpleStringProperty(Integer.toString(this.id));
   }
   public StringProperty getNameProperty() {
	   return new SimpleStringProperty(this.name);
   }
   public StringProperty getPlayer1Property() {
	   return new SimpleStringProperty(this.player1);
   }
   
   public StringProperty getPlayer2Property() {
	   return new SimpleStringProperty(this.player2);
   }
   public StringProperty getStatesProperty() {
	   return new SimpleStringProperty(Integer.toString(this.state));
   }
}
