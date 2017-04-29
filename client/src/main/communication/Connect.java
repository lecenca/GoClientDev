package src.main.communication;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import src.main.Room;
import src.main.Type;
import src.main.User;

import src.main.view.ChatBox;
import src.main.view.LobbyController;
import src.main.view.LoginController;
import src.main.view.SignupController;
import src.util.MessageQueue;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connect {
	/*
	 * private final static String LINE_SEPARATOR =
	 * System.getProperty("line.separator"); private static String IP; private
	 * static int PORT; private static Socket socket; private static
	 * OutputStream os; private static InputStream is; private static
	 * PrintWriter pw; private static BufferedReader br; private Thread
	 * receiveThread; private Thread chatThread; private String loginMessage;
	 * private String registerMessage; private String chatMessage = "hello";
	 * private ChatBox chatBox;
	 */
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	// private final static String IP = "172.16.90.242";
	private final static String IP = "123.64.10.15";
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
		try {
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
		} catch (ConnectException e) {
			e.printStackTrace();
			System.out.println("服务器连接失败");
			/*
			 * try { Thread.sleep(5000);
			 * 
			 * } catch (InterruptedException e1) { e1.printStackTrace(); }
			 */
		}catch(IOException e) {
			System.out.println("服务器连接失败");
		}
	}

    public static void send(String msg) {
        // get the outputStream of socket
        // OutputStream outputStream = socket.getOutputStream();
        // create the printwriter
        // pw = new PrintWriter(os,true);
        String sendMsg = new String(toHH(msg.length()), 0, 4);
        System.out.println("len: " + sendMsg.length() + msg.length() + ", msg: " + sendMsg + msg);
        pw.write(sendMsg + msg);
        pw.flush();
        recv = false;
        System.out.println("发送成功！");
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
            // System.out.println("response_type：" + response_type);
            switch (response_type) {
                case Type.Response.ACCOUNT_CHECK_SUCCESS:
                    handleAccountCheck(true);
                    break;
                case Type.Response.ACCOUNT_CHECK_FAILED:
                    handleAccountCheck(false);
                    break;
                case Type.Response.REGIST_SUCCESS:
                    handleRegist(true);
                    break;
                case Type.Response.REGIST_FAILED:
                    handleRegist(false);
                    break;
                case Type.Response.LOGIN_SUCCESS:
                    handleLogin(true);
                    break;
                case Type.Response.LOGIN_FAILED:
                    handleLogin(false);
                    break;
                case Type.Response.FETCH_ROOM_INFO_SUCCESS:
                    handleFetchRoom(jsonObject);
                    break;
                case Type.Response.FETCH_PLAYERS_INFO_SUCCESS:
                    handleFetchPlayer(jsonObject);
                    break;
                default:
                    break;
            }
            recv = true;
            // loginMessage = msg;
            registerMessage = msg;
            chatMessage = msg;
            // System.out.println("chatMessage:" + chatMessage);
            // msg = br.readLine();
        }
        catch (ConnectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("与服务器断开连接！");
        }catch (IOException e) {
            System.out.println("与服务器连接异常！");
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
		System.out.println("corect:" + LoginController.correct);
	}

	private void handleFetchRoom(JSONObject jsonObject) {
		ArrayList<Room> roomList = Decoder.parseRoomList(jsonObject);
        if(roomList.size() != 0){
            for (Room room : roomList) {
                MessageQueue<Room> rooms = LobbyController.getRooms();
                rooms.add(room);
            }
            System.out.println("handle list:" + roomList);
        }
	}

	private void handleFetchPlayer(JSONObject jsonObject) {
		ArrayList<User> playerList = Decoder.parsePlayerList(jsonObject);
        if(playerList.size() != 0){
            for(User user : playerList){
                MessageQueue<User> players = LobbyController.getPlayers();
                players.add(user);
            }
            System.out.println("handle list:" + playerList);
        }
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
