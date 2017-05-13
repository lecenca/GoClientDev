package src.main.communication;

import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import src.main.Client;
import src.main.Room;
import src.main.Stone;
import src.main.User;

public class Decoder {

    public static JSONObject parseObject(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject;
    }

    public static User parseUser(JSONObject jsonObject){
        return JSON.parseObject(jsonObject.toJSONString(), User.class);
    }

    public static Room parseRoom(JSONObject jsonObject){
        return JSON.parseObject(jsonObject.toJSONString(), Room.class);
    }

    public static ArrayList<Room> parseRoomList(JSONObject object) {
        JSONArray rooms = object.getJSONArray("rooms_list");
        return new ArrayList<>(JSON.parseArray(rooms.toJSONString(), Room.class));
    }

    public static ArrayList<User> parsePlayerList(JSONObject object) {
        JSONArray players = object.getJSONArray("players_list");
        return new ArrayList<>(JSON.parseArray(players.toJSONString(), User.class));
    }

    public static ArrayList<Stone> parseKillList(JSONObject object) {
        JSONArray kill = object.getJSONArray("kill");
        return new ArrayList<>(JSON.parseArray(kill.toJSONString(), Stone.class));
    }

}
