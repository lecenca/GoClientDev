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

package src.main.communication;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import src.main.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private static Gson gson = new Gson();

    public static String keepAliveRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.KEEP_ALIVE) + "}";
    }

    public static String chechAccountRequest(String account) {
        Map map = new HashMap();
        map.put("account", account);
        return requestJson(gson.toJson(map), Type.Request.CHECK_ACCOUNT);
    }

    public static String signupRequest(User user) {
        return requestJson(gson.toJson(user), Type.Request.REGIST);
    }

    public static String loginRequest(String account, String password) {
        Map map = new HashMap();
        map.put("account", account);
        map.put("password", password);
        return requestJson(gson.toJson(map), Type.Request.LOGIN);
    }

    public static String logoutRequest() {
        Map map = new HashMap();
        map.put("account", Client.getUser().getAccount());
        return requestJson(gson.toJson(map), Type.Request.LOGOUT);
    }

    public static String fetchRoomsRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_ROOMS_INFO) + "}";
    }

    public static String fetchPlayersRequest() {
        return "{\"request_type\":" + String.valueOf(Type.Request.FETCH_PLAYERS_INFO) + "}";
    }

    public static String updatePlayerRequest() {
        return requestJson(gson.toJson(Client.getUser()), Type.Request.UPDATE_PLAYER);
    }

    public static String updateRoomRequest(Room room, int type) {
        JSONObject jsonObject = JSONObject.parseObject(gson.toJson(room).toString());
        jsonObject.put("action", type);
        return requestJson(gson.toJson(jsonObject), Type.Request.UPDATE_ROOM);
    }

    public static String roomMessageRequest(int roomId, String message) {
        Map map = new HashMap();
        map.put("room_id", roomId);
        map.put("account", Client.getUser().getAccount());
        map.put("message", message);
        return requestJson(gson.toJson(map), Type.Request.ROOM_CHAT);
    }

    public static String lobbyMessageRequest(String message) {
        Map map = new HashMap();
        map.put("account", Client.getUser().getAccount());
        map.put("message", message);
        return requestJson(gson.toJson(map), Type.Request.LOBBY_CHAT);
    }

    public static String sitdownRequest(int roomId) {
        Map map = new HashMap();
        map.put("room_id", roomId);
        map.put("player", Client.getUser().getAccount());
        return requestJson(gson.toJson(map), Type.Request.SITDOWN);
    }

    public static String readyRequest(int roomId, Boolean player1IsReady, Boolean player2IsReady) {
        Map map = new HashMap();
        map.put("room_id", roomId);
        map.put("player1", player1IsReady);
        map.put("player2", player2IsReady);
        return requestJson(gson.toJson(map), Type.Request.READY);
    }

    public static String gameOverRequest(int roomId, double player1Point, double player2Point, int type) {
        Map map = new HashMap();
        map.put("room_id", roomId);
        map.put("point1", player1Point);
        map.put("point2", player2Point);
        map.put("result", type);
        return requestJson(gson.toJson(map), Type.Request.GAME_OVER);
    }

    public static String judgeRequest(int roomId, boolean player) {
        Map map = new HashMap();
        map.put("room_id", roomId);
        map.put("account", player);
        return requestJson(gson.toJson(map), Type.Request.JUDGE);
    }

    public static String gameActionRequest(int action, int x, int y) {
        Map map = new HashMap();
        map.put("room_id", Client.getUser().getRoom());
        map.put("action", action);
        Map placeMap = new HashMap();
        placeMap.put("x", x);
        placeMap.put("y", y);
        placeMap.put("color", Client.getGameController().getTurn());
        placeMap.put("step", Board.step);
        map.put("place", placeMap);
        if (action == Type.Action.KILL) {
            ArrayList killList = new ArrayList();
            for (int chain : Board.dead) {
                for (Stone stone : Board.stonesMap.get(chain)) {
                    Map killStone = new HashMap();
                    killStone.put("x", stone.x);
                    killStone.put("y", stone.y);
                    killStone.put("color", stone.color);
                    killStone.put("step", stone.step);
                    killList.add(killStone);
                    break;
                }
            }
            map.put("kill", killList);
        }
        return requestJson(gson.toJson(map), Type.Request.GAME_ACTION);
    }

    private static String requestJson(String json, int type) {
        return "{\"request_type\":" + type + "," + json.substring(1);
    }

}
