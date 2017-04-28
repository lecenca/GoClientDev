package src.main;

import javax.print.attribute.standard.RequestingUserName;

/**
 * Created by 刘俊延 on 2017/4/28.
 */
public class Type {

    public static class Request {
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

    public static class Response {
        public static final int
                FETCH_PLAYER_INFO_SUCCESS = 0,
                FETCH_PLAYER_INFO_FAILED = 1,
                FETCH_PLAYERS_INFO_SUCCESS = 2,
                FETCH_PLAYERS_INFO_FAILED = 3,
                REGIST_SUCCESS = 4,
                REGIST_FAILED = 5,
                LOGIN_SUCCESS = 6,
                LOGIN_FAILED = 7,
                LOGOUT_SUCCESS = 8,
                LOGOUT_FAILED = 9,
                FETCH_LOBBY_INFO_SUCCESS = 10,
                FETCH_LOBBY_INFO_FAILED = 11,
                FETCH_ROOM_INFO_SUCCESS = 12,
                FETCH_ROOM_INFO_FAILED = 13,
                BROADCAST_SITDOWN = 14,
                SITDOWN_SUCCESS = 15,
                SITDOWN_FAILED = 16,
                BROADCAST_LEAVE = 17,
                LEAVE_SUCCESS = 18,
                LEAVE_FAILED = 19,
                SINGLECAST_READYGO = 20,
                BROADCAST_READYGO = 21,
                READYGO_SUCCESS = 22,
                READYGO_FAILED = 23,
                PLACECHESS_SUCCESS = 24,
                PLACECHESS_FAILED = 25,
                SINGLECAST_PLACECHESS = 26,
                UPDATE_GAMERESULT_SUCCESS = 27,
                UPDATE_GAMERESULT_FAILED = 28,
                SEND_MSG_SUCCESS = 29,
                SEND_MSG_FAILED = 30,
                BROADCAST_SOMEONE_UP = 31,
                BROADCAST_SOMEONE_DOWN = 32,
                GROUP_CHAT_MSG = 33,
                GROUP_CHAT_SUCCESS = 34,
                GROUP_CHAT_FAILED = 35,
                SINGLECAST_CHAT = 36,
                BROADCAST_GAMERESULT_UPDATE = 37,
                ACCOUNT_CHECK_SUCCESS = 38,
                ACCOUNT_CHECK_FAILED = 39;
    }

    public static class State {
        public static final int
                OUTLINE = 0,    // 离线
                OTHER = 1,      // 除其它情况以外的状态，如在注册，登陆界面等
                IDLE = 2,       // 已经登陆但还没有进入游戏房间，此时玩家列表状态显示"闲逛中"
                READY = 3,      // 已经进入游戏房间但是游戏还没有开始，此时玩家列表状态显示"准备中"
                GAMING = 4;     // 已经开始游戏，此时玩家列表状态显示"游戏中"
    }

    public static class KOMI{
        public static final int
                CONCESSION = 0,  // 让先/不贴目
                THREE_FIVE = 1,  // 黑贴3.5目
                SIX_FIVE = 2;    // 黑贴6.5目
    }

    public static class Action {
        public static final int
                INVALID = 0,    // 无效
                PLACE = 1,      // 落子
                KILL = 2,       // 提子
                SURRENDER = 3, // 认输
                JUDGE = 4;      // 请求判子
    }
}
