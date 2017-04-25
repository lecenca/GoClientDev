package src.main.communication;

import com.google.gson.Gson;

import src.main.Action;
import src.main.Board;
import src.main.Stone;
import src.main.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private static class RequestType {

        public static int
                CHECK_ACCOUNT = 0,
                CHECK_NAME = 1,
                REGIST = 2,
                LOGIN = 3,
                LOGOUT = 4,
                FETCH_PLAYER_INFO = 5,
                FETCH_LOBBY_INFO,
                FETCH_ROOM_INFO,
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

    private Gson gson = new Gson();

    public String chechAccountRequest(String account) {
        Map map = new HashMap();
        map.put("account", account);
        return requestJson(gson.toJson(map), RequestType.CHECK_ACCOUNT);
    }

    public String checkNameRequest(String name) {
        Map map = new HashMap();
        map.put("nickname", name);
        return requestJson(gson.toJson(map), RequestType.CHECK_NAME);
    }

    public String signUpRequest(UserInfo u) {
        return requestJson(gson.toJson(u), RequestType.REGIST);
    }

    public String loginRequest(String account, String password) {
        Map map = new HashMap();
        map.put("account", account);
        map.put("password", password);
        return requestJson(gson.toJson(map), RequestType.LOGIN);
    }

    public String actionRequest(int action, int color, int x, int y) {
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

    public String getPlayerListRequest() {
        return "{\"request_type\":" + String.valueOf(RequestType.FETCH_PLAYER_INFO) + "}";
    }

    private String requestJson(String json, int type) {
        return "{\"request_type\":" + String.valueOf(type) + "," + json.substring(1);
    }

}
