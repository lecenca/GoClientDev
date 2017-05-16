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
import javafx.scene.control.Alert;
import src.main.*;
import src.main.view.LoginController;
import src.main.view.SignupController;
import src.util.MessageQueue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Connect {

    private static String IP;
    private static int PORT;
    public static Socket socket;
    private static OutputStream os;
    private static InputStream is;
    private Thread receiveThread;
    private String chatMessage = "hello";
    private static ConcurrentLinkedDeque<Integer> responseValues = new ConcurrentLinkedDeque<>();
    public static ArrayList<Integer> requestValues = new ArrayList<>();
    public static boolean recv = false;
    private static boolean connect = false;
    public static boolean timeout = false;

    public static Thread waitThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int i = 0;
            outer:
            while (/* !Connect.recv */true) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (recv)
                    for (Integer requestValue : requestValues) {
                        for (Integer resonseValue : responseValues) {
                            if (resonseValue == requestValue) {
                                requestValues.clear();
                                break outer;
                            }
                        }
                    }
                i++;
                if (i == 100) {
                    //JOptionPane.showMessageDialog(null, "连接超时，请重试", "连接错误", JOptionPane.INFORMATION_MESSAGE);
                    Alert error = new Alert(Alert.AlertType.ERROR, "连接超时，请重试");
                    error.show();
                    break;
                }
            }
        }
    });

    public Connect() {
        InputStream inputStream = this.getClass().getResourceAsStream("Connect.properties");
        Properties pro = new Properties();
        try {
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IP = pro.getProperty("IP");
        PORT = Integer.parseInt(pro.getProperty("PORT"));
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(IP, PORT), 2000);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            connect = true;
            System.out.println("服务器连接成功");
        } catch (IOException e) {
            connect = false;
            System.out.println("服务器连接失败");
        }
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("接收线程启动");
                while (true) {
                    receive();
                }
            }
        });
    }

    public static void sendMsgToserver(String str) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.write(str.getBytes());
        dos.flush();
    }

    public static void send(String message) {
        try {
            os.write(intToByteHH(message.getBytes("utf-8").length));
            os.write(message.getBytes("utf-8"));
            os.flush();
            recv = false;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void waitForRec(Integer... requestValues) {
        int i = 0;
        outer:
        while (/* !Connect.recv */true) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (recv)
                for (Integer requestValue : requestValues) {
                    for (Integer resonseValue : responseValues) {
                        if (resonseValue == requestValue) {
                            responseValues.remove(resonseValue);
                            break outer;
                        }
                    }
                }
            i++;
            if (i == 100) {
                Alert error = new Alert(Alert.AlertType.ERROR, "连接超时，请重试");
                error.show();
                timeout = true;
                break;
            }
        }
    }

    public void receive() {
        String msg = null;
        byte[] first = new byte[4];
        try {
            is.read(first);
            int len = byteToIntHH(first);
            //System.out.println("服务器响应信息长度:" + len);
            if (len > 0) {
                byte[] buff = new byte[len];
                is.read(buff);
                msg = new String(buff, 0, len);
                System.out.println("receive from server: " + msg);
                JSONObject jsonObject = Decoder.parseObject(msg);
                int response_type = jsonObject.getIntValue("response_type");
                switch (response_type) {
                    case Type.Response.CHECK_ACCOUNT_SUCCESS:
                        handleAccountCheck(true);
                        break;
                    case Type.Response.CHECK_ACCOUNT_FAILED:
                        handleAccountCheck(false);
                        break;
                    case Type.Response.REGIST_SUCCESS:
                        handleRegist(true);
                        break;
                    case Type.Response.REGIST_FAILED:
                        handleRegist(false);
                        break;
                    case Type.Response.LOGIN_SUCCESS:
                        handleLogin(true, jsonObject);
                        break;
                    case Type.Response.LOGIN_FAILED:
                        handleLogin(false, jsonObject);
                        break;
                    case Type.Response.LOGOUT:
                        handleLogout(jsonObject);
                        break;
                    case Type.Response.FETCH_ROOMS_INFO_SUCCESS:
                        handleFetchRoom(jsonObject);
                        break;
                    case Type.Response.FETCH_PLAYERS_INFO_SUCCESS:
                        handleFetchPlayer(jsonObject);
                        break;
                    case Type.Response.UPDATE_PLAYER:
                        handleUpdatePlayer(jsonObject);
                        break;
                    case Type.Response.UPDATE_ROOM:
                        handleUpdateRoom(jsonObject);
                        break;
                    case Type.Response.LOBBY_CHAT_MSG:
                        handleChatMessage(jsonObject);
                        break;
                    case Type.Response.ROOM_CHAT_MSG:
                        handlePrivateChatMessage(jsonObject);
                        break;
                    case Type.Response.SITDOWN_SUCCESS:
                        handleSitDown(true);
                        break;
                    case Type.Response.SITDOWN_FAILED:
                        handleSitDown(false);
                        break;
                    case Type.Response.GAME_ACTION:
                        handleGameAction(jsonObject);
                        break;
                    case Type.Response.READY:
                        handleReady(jsonObject);
                        break;
                    case Type.Response.JUDGE:
                        handleJudge();
                        break;
                    case Type.Response.GAME_RESULT:
                        handleGameResult(jsonObject);
                        break;
                    default:
                        break;
                }
                responseValues.add(response_type);
                recv = true;
            }
        } catch (ConnectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("与服务器断开连接！");
        } catch (IOException e) {

        }
    }

    private void handlePrivateChatMessage(JSONObject jsonObject) {
        String account = jsonObject.getString("account");
        String nickname = Client.playersMap.get(account).getNickname();
        String msg = jsonObject.getString("message");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = String.format(" (" + format.format(date) + "):");
        MessageQueue<String> messages = Client.getPrivateChatMessages();
        messages.add(nickname + time + msg);
    }

    private void handleChatMessage(JSONObject jsonObject) {
        String account = jsonObject.getString("account");
        String nickname = Client.playersMap.get(account).getNickname();
        String message = jsonObject.getString("message");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = String.format(" (" + format.format(date) + "):");
        MessageQueue<String> messages = Client.getChatMessages();
        messages.add(nickname + time + message);
    }

    private void handleSitDown(boolean state) {
        Client.getLobbyController().setSitDown(state);
    }

    private void handleAccountCheck(boolean state) {
        SignupController.accountCheckSuccess = state;
    }

    private void handleRegist(boolean state) {
        SignupController.registSuccess = state;
    }

    private void handleLogin(boolean state, JSONObject jsonObject) {
        LoginController.correct = state;
        if (state) {
            User user = Decoder.parseUser(jsonObject.getJSONObject("user"));
            user.setPriority(1);
            Client.setUser(user);
            MessageQueue<User> players = Client.getPlayers();
            players.add(user);
            System.out.println("user priority:" + user.getPriority());
        }
    }

    private void handleLogout(JSONObject jsonObject) {
        String account = jsonObject.getString("account");
        Client.removePlayer(account);
    }

    private void handleFetchRoom(JSONObject jsonObject) {
        ArrayList<Room> roomList = Decoder.parseRoomList(jsonObject);
        if (roomList.size() != 0) {
            MessageQueue<Room> rooms = Client.getRooms();
            for (Room room : roomList) {
                rooms.add(room);
            }
        }
    }

    private void handleFetchPlayer(JSONObject jsonObject) {
        ArrayList<User> playerList = Decoder.parsePlayerList(jsonObject);
        if (playerList.size() != 0) {
            MessageQueue<User> players = Client.getPlayers();
            for (User user : playerList) {
                if (user.equals(Client.getUser()))
                    user.setPriority(1);
                else
                    user.setPriority(0);
                System.out.println("other user priority" + user.getPriority());
                players.add(user);
            }
        }
    }

    private void handleUpdatePlayer(JSONObject jsonObject) {
        MessageQueue<User> players = Client.getPlayers();
        User user = Decoder.parseUser(jsonObject.getJSONObject("user"));
        if (!user.equals(Client.getUser()))
            user.setPriority(0);
        else
            user.setPriority(1);
        players.add(user);
    }

    private void handleUpdateRoom(JSONObject jsonObject) {
        if (jsonObject.getIntValue("action") == Type.UpdateRoom.DESTROY) {
            Client.removeRoom(jsonObject.getJSONObject("room").getIntValue("id"));
            return;
        }
        MessageQueue<Room> rooms = Client.getRooms();
        rooms.add(Decoder.parseRoom(jsonObject.getJSONObject("room")));
    }

    private void handleReady(JSONObject jsonObject) {
        Client.getGameController().setReady(jsonObject.getBooleanValue("player1"),
                jsonObject.getBooleanValue("player2"));
    }

    private void handleJudge() {
        Client.getGameController().judgeFromOpponent();
    }

    private void handleGameAction(JSONObject jsonObject) {
        JSONObject place = jsonObject.getJSONObject("place");
        if (jsonObject.getIntValue("action") == Type.Action.PLACE) {
            Client.getGameController().playAction(
                    Type.Action.PLACE,
                    place.getIntValue("x"),
                    place.getIntValue("y"),
                    place.getIntValue("color"));
        } else {
            Board.addDead(Decoder.parseKillList(jsonObject));
            Core.SetPossibleKo(place.getIntValue("x"), place.getIntValue("y"));
            Client.getGameController().playAction(
                    Type.Action.KILL,
                    place.getIntValue("x"),
                    place.getIntValue("y"),
                    place.getIntValue("color"));
        }
    }

    private void handleGameResult(JSONObject jsonObject) {
        Client.getGameController().setGameResult(
                jsonObject.getIntValue("result")
        );
        Client.getGameController().setScore(
                jsonObject.getDoubleValue("score")
        );
        Client.getGameController().showGameResult();
    }

    // For C/C++ on Windows.
    public static byte[] intToByteLH(int n) {
        return new byte[]{
                (byte) (n & 0xff),
                (byte) ((n >> 8) & 0xff),
                (byte) ((n >> 16) & 0xff),
                (byte) ((n >> 24) & 0xff)
        };
    }

    // For C/C++ on Linux/Unix.
    public static byte[] intToByteHH(int n) {
        return new byte[]{
                (byte) ((n >> 24) & 0xff),
                (byte) ((n >> 16) & 0xff),
                (byte) ((n >> 8) & 0xff),
                (byte) (n & 0xff)
        };
    }

    public static int byteToIntLH(byte[] bytes) {
        return ((bytes[3] & 0xFF) << 24)
                | ((bytes[2] & 0xFF) << 16)
                | ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
    }

    public static int byteToIntHH(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24)
                | ((bytes[1] & 0xFF) << 16)
                | ((bytes[2] & 0xFF) << 8)
                | (bytes[3] & 0xFF);
    }

    public Thread getReceiveThread() {
        return receiveThread;
    }

    public static boolean interrupted() {
        if (!connect) {
            //JOptionPane.showMessageDialog(null, "请检查您的网络连接", "连接失败", JOptionPane.ERROR_MESSAGE);
            Alert error = new Alert(Alert.AlertType.ERROR, "请检查您的网络连接");
            error.show();
            return true;
        }
        return false;
    }

    public static boolean hasConnect() {
        return connect;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Connect.socket = socket;
    }

    public static String getIP() {
        return IP;
    }

    public static int getPORT() {
        return PORT;
    }

    public static OutputStream getOs() {
        return os;
    }

    public static void setOs(OutputStream os) {
        Connect.os = os;
    }

    public static InputStream getIs() {
        return is;
    }

    public static void setIs(InputStream is) {
        Connect.is = is;
    }

}
