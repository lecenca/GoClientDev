package src.main.communication;

import com.google.gson.Gson;

import src.main.Action;
import src.main.Board;
import src.main.Stone;
import src.main.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private static class RequestType {

        public static int
                CHECK_ACCOUNT = 0,
                REGIST = 3,
                LOGIN = 4,
                LOGOUT = 5,
                FETCH_PLAYER_INFO = 2,
                FETCH_LOBBY_INFO,
                FETCH_ROOM_INFO = 7,
                ACTION = 8,
                SITDOWN,
                LEAVE,
                READGO,
                GIVEUP,
                PLACECHESS,
                GAMEOVER_WINNER,
                GAMEOVER_LOSER,
                SEND_MSG;
    }

    private static Gson gson = new Gson();

    public static String chechAccountRequest(String account) {
        Map map = new HashMap();
        map.put("account", account);
        return requestJson(gson.toJson(map), RequestType.CHECK_ACCOUNT);
    }

    public static String signupRequest(User u) {
        return requestJson(gson.toJson(u), RequestType.REGIST);
    }

    public static String loginRequest(String account, String password) {
        Map map = new HashMap();
        map.put("account", account);
        map.put("password", password);
        return requestJson(gson.toJson(map), RequestType.LOGIN);
    }

    public static String updateRoomRequest(){
        return "{\"request_type\":" + String.valueOf(RequestType.FETCH_ROOM_INFO) + "}";
    }

    public static String updatePlayersRequest(){
        return "{\"request_type\":" + String.valueOf(RequestType.FETCH_PLAYER_INFO) + "}";
    }

    public static String actionRequest(int action, int color, int x, int y) {
        Map map = new HashMap();
        if (action == Action.PLACE) {
            map.put("action", "place");
            Map placeMap = new HashMap();
            placeMap.put("x", x);
            placeMap.put("y", y);
            placeMap.put("color", color);
            map.put("place", placeMap);
        } else if (action == Action.KILL) {
            map.put("action", "kill");
            Map placeMap = new HashMap();
            placeMap.put("x", x);
            placeMap.put("y", y);
            placeMap.put("color", color);
            map.put("place", placeMap);
            ArrayList killList = new ArrayList();
            for (int chain : Board.killed) {
                for (Stone stone : Board.stoneMap.get(chain)) {
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
        return requestJson(gson.toJson(map), RequestType.ACTION);
    }

    public static String getPlayerListRequest() {
        return "{\"request_type\":" + String.valueOf(RequestType.FETCH_PLAYER_INFO) + "}";
    }

    private static String requestJson(String json, int type) {
        return "{\"request_type\":" + String.valueOf(type) + "," + json.substring(1);
    }

}