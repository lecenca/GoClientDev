package src.main.communication;

import com.alibaba.fastjson.JSONObject;
import src.main.view.ChatBox;
import src.main.view.LoginController;
import src.main.view.SignupController;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect {
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");
    //private final static String IP = "172.16.90.242";
    private final static String IP = "110.120.143.235";
    private final static int PORT = 60000;
    private static Socket socket;
    private static OutputStream os;
    private static InputStream is;
    private static PrintWriter pw;
    private static BufferedReader br;
    private Thread receiveThread;
    private Thread chatThread;
    private String loginMessage;
    private String registerMessage;
    private String chatMessage = "hello";
    private ChatBox chatBox;

    public static boolean recv = false;

    public Connect() {
        try{
            socket = new Socket(IP, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            pw = new PrintWriter(os);
            br = new BufferedReader(new InputStreamReader(is));
            receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("接收线程启动");
                    while (true) {
                        receive();
                    }
                }
            });
            chatThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("聊天线程启动");
                    // TODO Auto-generated method stub
                    while (true) {
                        String msg = null;
                        while (msg == null)
                            msg = chatMessage;
                        System.out.println(msg);
                        if (chatBox != null)
                            chatBox.sentSentence(msg);
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器连接失败");
            /*try {
                Thread.sleep(5000);

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }*/
        }
    }

    public static void send(String msg) {
        //get the outputStream of socket
        //OutputStream outputStream = socket.getOutputStream();
        //create the printwriter
        //pw = new PrintWriter(os,true);
        byte[] len = new byte[4];
        len = toHH(msg.length());
        String sendMsg = new String(len, 0, 4);
        System.out.println("len:" + msg.length() + "msg: " + sendMsg);
        pw.write(sendMsg + msg);
        pw.flush();
        recv = false;
    }

    public static void waitForRec() {
        int i = 0;
        while (!Connect.recv) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i == 100) {
                JOptionPane.showMessageDialog(null, "连接超时，请重试", "连接错误", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    public void receive() {
        String msg = null;
        byte[] buff = new byte[1024];
        try {
            int len = is.read(buff);
            msg = new String(buff, 4, len - 4);
            System.out.println("receive from server: " + msg);
            JSONObject jsonObject = Decoder.parseObject(msg);
            int response_type = jsonObject.getIntValue("response_type");
            //System.out.println("response_type：" + response_type);
            switch (response_type) {
                case ResponseType.ACCOUNT_CHECK_SUCCESS:
                    handleAccountCheck(true);
                    break;
                case ResponseType.ACCOUNT_CHECK_FAILED:
                    handleAccountCheck(false);
                    break;
                case ResponseType.REGIST_SUCCESS:
                    handleRegist(true);
                    break;
                case ResponseType.REGIST_FAILED:
                    handleRegist(false);
                    break;
                case ResponseType.LOGIN_SUCCESS:
                    handleLogin(true);
                    break;
                case ResponseType.LOGIN_FAILED:
                    handleLogin(false);
                    break;
                case ResponseType.FETCH_ROOM_INFO_SUCCESS:
                    handleFetchRoom(jsonObject);
                    break;
                case ResponseType.FETCH_PLAYER_INFO_SUCCESS:
                    handleFetchPlayer(jsonObject);
                    break;
                default:
                    break;
            }
            recv = true;
            //loginMessage = msg;
            registerMessage = msg;
            chatMessage = msg;
            //System.out.println("chatMessage:" + chatMessage);
            //msg = br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void handleAccountCheck(boolean state) {
        SignupController.accountCheckSuccess = state;
    }

    private void handleRegist(boolean state) {
        SignupController.registSuccess = state;
    }

    private void handleLogin(boolean state) {
        LoginController.correct = state;
    }

    private void handleFetchRoom(JSONObject object){

    }

    private void handleFetchPlayer(JSONObject object){

    }
    // For C/C++ on Windows.
    private static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    // For C/C++ on Linux/Unix.
    private static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public void closeInputstream() {
        // TODO Auto-generated method stub
        try {
            socket.shutdownInput();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public Thread getReceiveThread() {
        return receiveThread;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setChatBox(ChatBox chatBox) {
        this.chatBox = chatBox;
    }

    public Thread getChatThread() {
        return chatThread;
    }

    public void setChatThread(Thread chatThread) {
        this.chatThread = chatThread;
    }

}
