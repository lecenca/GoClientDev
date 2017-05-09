package src.main;

public class Type {

    public static class Request {
        public static int
                CHECK_ACCOUNT = 0,
                FETCH_PLAYER_INFO = 1,
                FETCH_PLAYERS_INFO = 2,
                REGIST = 3,
                LOGIN = 4,
                LOGOUT = 5,
                FETCH_LOBBY_INFO = 6,
                FETCH_ROOMS_INFO = 7,
                UPDATE_ROOM = 8,  // SITDOWN
                UPDATE_PLAYER = 9,
                LEAVE = 9,
                READY = 10,
                ACTION = 11,
                GAMERESULT = 12,
                SEND_MSG = 13,
                GROUP_CHAT = 14,
                JUDGE = 15;
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

    public static class UserState {
        public static final int
                OUTLINE = 0,    // 离线
                OTHER = 1,      // 除其它情况以外的状态，如在注册，登陆界面等
                IDLE = 2,       // 已经登陆但还没有进入游戏房间，此时玩家列表状态显示"闲逛中"
                READY = 3,      // 已经进入游戏房间但是游戏还没有开始，此时玩家列表状态显示"准备中"
                GAMING = 4;     // 已经开始游戏，此时玩家列表状态显示"游戏中"
    }

    public static class RoomState {
        public static final int
                WATING = 0,
                READY = 1,
                GAMING = 2;
    }

    public static class UpdatePlayer {
        public static final int
                IN = 0,
                CHANGE = 1,
                OUT = 2;
    }

    public static class UpdateRoom {
        public static final int
                PLAYER1IN = 0,
                PLAYER2IN = 1,
                PLAYER1OUT = 2,
                PLAYER2OUT = 3,
                DESTROY = 4;
    }

    public static class KOMI {
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

    public static class GameResult{
        public static final int
            WIN = 0,
            LOSE = 1,
            DRAW = 2;
    }
}
