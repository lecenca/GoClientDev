package src.main.communication;

import com.google.gson.Gson;

import src.main.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private static Gson gson = new Gson();

    public static String chechAccountRequest(String account) {
        Map map = new HashMap();
        map.put("account", account);
        return requestJson(gson.toJson(map), Type.Request.CHECK_ACCOUNT);
    }

    public static String signupRequest(User u) {
        return requestJson(gson.toJson(u), Type.Request.REGIST);
    }

    public static String loginRequest(String account, String password) {
        Map map = new HashMap();
        map.put("account", account);
        map.put("password", password);
        return requestJson(gson.toJson(map), Type.Request.LOGIN);
    }

    public static String updateRoomRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_ROOM_INFO) + "}";
    }

    public static String updatePlayersRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_PLAYER_INFO) + "}";
    }

    public static String actionRequest(int action, int color, int x, int y) {
        Map map = new HashMap();
        if (action == Type.Action.PLACE) {
            map.put("action", "place");
            Map placeMap = new HashMap();
            placeMap.put("x", x);
            placeMap.put("y", y);
            placeMap.put("color", color);
            map.put("place", placeMap);
        } else if (action == Type.Action.KILL) {
            map.put("action", "kill");
            Map placeMap = new HashMap();
            placeMap.put("x", x);
            placeMap.put("y", y);
            placeMap.put("color", color);
            map.put("place", placeMap);
            ArrayList killList = new ArrayList();
            for (int chain : Board.dead) {
                for (Stone stone : Board.stonesMap.get(chain)) {
                    Map killStone = new HashMap();
                    killStone.put("x", stone.x);
                    killStone.put("y", stone.y);
                    killStone.put("color", stone.color);
                    killList.add(killStone);
                    break;
                }
            }
            map.put("kill", killList);
        }
        return requestJson(gson.toJson(map), Type.Request.ACTION);
    }

    public static String getPlayerListRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_PLAYER_INFO) + "}";
    }

    private static String requestJson(String json, int type) {
        return "{\"request_type\":" + String.valueOf(type) + "," + json.substring(1);
    }

    public static String getRoomListRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_ROOM_INFO) + "}";
    }
}
