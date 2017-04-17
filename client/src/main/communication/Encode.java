package src.main.communication;

import com.google.gson.Gson;

import src.main.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘俊延 on 2017/4/9.
 */

public class Encode {

    class RequestType {

        public int
                CHECK_ACCOUNT = 0,
                REGIST = 1,
                LOGIN = 2,
                LOGOUT,
                FETCH_PLAYER_INFO,
                FETCH_LOBBY_INFO,
                FETCH_ROOM_INFO,
                SITDOWN,
                LEAVE,
                READGO,
                GIVEUP,
                PLACECHESS,
                GAMEOVER_WINNER,
                GAMEOVER_LOSER,
                SEND_MSG;
    }

    private RequestType rt = new RequestType();
    private Gson gson = new Gson();

    public String chechAccountRequest(String account){
        Map map = new HashMap();
        map.put("account",account);
        return requestJson(gson.toJson(map),rt.CHECK_ACCOUNT);
    }

    public String signUpRequest(UserInfo u){
        return requestJson(gson.toJson(u),rt.REGIST);
    }

    public String loginRequest(String account, String password){
        Map map = new HashMap();
        map.put("account",account);
        map.put("password",password);
        return requestJson(gson.toJson(map),rt.LOGIN);
    }

    private String requestJson(String json, int type){
        return "{\"request_type\":" + String.valueOf(type) + "," + json.substring(1);
    }

}
