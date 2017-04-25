package src.main.communication;

public class ResponseType {
    public static final int
            FETCH_PLAYER_INFO_SUCCESS = 0,
            FETCH_PLAYER_INFO_FAILED = 1,
            REGIST_SUCCESS = 2,
            REGIST_FAILED = 3,
            LOGIN_SUCCESS = 4,
            LOGIN_FAILED = 5,
            LOGOUT_SUCCESS = 6,
            LOGOUT_FAILED = 7,
            FETCH_LOBBY_INFO_SUCCESS = 8,
            FETCH_LOBBY_INFO_FAILED = 9,
            FETCH_ROOM_INFO_SUCCESS = 10,
            FETCH_ROOM_INFO_FAILED = 11,
            BROADCAST_SITDOWN = 12,
            SITDOWN_FAILED = 13,
            BROADCAST_LEAVE = 14,
            LEAVE_FAILED = 15,
            SINGLECAST_READYGO = 16,
            READYGO_FAILED = 17,
            GIVEUP_SUCCESS = 18,
            GIVEUP_FAILED = 19,
            PLACECHESS_SUCCESS = 20,
            PLACECHESS_FAILED = 21,
            SINGLECAST_PLACECHESS = 22,
            GAMEOVER = 23,
            GAMEOVER_FAILED = 24,
            SEND_MSG_SUCCESS = 25,
            SEND_MSG_FAILED = 26,
            ACCOUNT_CHECK_SUCCESS = 27,
            ACCOUNT_CHECK_FAILED = 28;
}
