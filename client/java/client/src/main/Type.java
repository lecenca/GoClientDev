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

package src.main;

public class Type {

    public static class Request {
        public static final int
                KEEP_ALIVE = 0,
                CHECK_ACCOUNT = 1,
                REGIST = 2,
                LOGIN = 3,
                LOGOUT = 4,
                FETCH_PLAYERS_INFO = 5,
                FETCH_ROOMS_INFO = 6,
                UPDATE_PLAYER = 7,
                UPDATE_ROOM = 8,
                SITDOWN = 9,
                READY = 10,
                JUDGE = 11,
                GAME_ACTION = 12,
                GAME_OVER = 13,
                ROOM_CHAT = 14,
                LOBBY_CHAT = 15;
    }

    public static class Response {
        public static final int
                KEEP_ALIVE = 0,
                CHECK_ACCOUNT_SUCCESS = 1,
                CHECK_ACCOUNT_FAILED = 2,
                REGIST_SUCCESS = 3,
                REGIST_FAILED = 4,
                LOGIN_SUCCESS = 5,
                LOGIN_FAILED = 6,
                LOGOUT = 7,
                FETCH_PLAYERS_INFO_SUCCESS = 8,
                FETCH_PLAYERS_INFO_FAILED = 9,
                FETCH_ROOMS_INFO_SUCCESS = 10,
                FETCH_ROOMS_INFO_FAILED = 11,
                UPDATE_ROOM = 12,
                UPDATE_PLAYER = 13,
                SITDOWN_SUCCESS = 14,
                SITDOWN_FAILED = 15,
                READY = 16,
                JUDGE = 17,
                GAME_ACTION = 18,
                GAME_RESULT = 19,
                ROOM_CHAT_MSG = 20,
                LOBBY_CHAT_MSG = 21;
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
                GAMING = 2,
                NONE = 3;
    }

    public static class UpdateRoom {
        public static final int
                PLAYER1IN = 0,
                PLAYER2IN = 1,
                PLAYER1OUT = 2,
                PLAYER2OUT = 3,
                DESTROY = 4,
                STATE_CHANGE = 5;
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
                KILL = 2;       // 提子
    }

    public static class GameResult {
        public static final int
                WIN = 0,
                LOSE = 1,
                DRAW = 2,
                PLAYER1_OVERTIME = 16,
                PLAYER2_OVERTIME = 17,
                PLAYER1_SURRENDER = 32,
                PLAYER2_SURRENDER = 33,
                PLAYER1_ESCAPE = 64,
                PLAYER2_ESCAPE = 65;
    }

}
