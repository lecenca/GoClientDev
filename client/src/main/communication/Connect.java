package src.main.communication;

import com.alibaba.fastjson.JSONObject;

import src.main.*;

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
import java.util.Properties;

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
	//private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private static String IP = "172.16.90.242";
	private static int PORT = 10005;
	private static Socket socket;
	private static OutputStream os;
	private static InputStream is;
	private static PrintWriter pw;
	private static BufferedReader br;
	private Thread receiveThread;
	//private Thread chatThread;
	//private String loginMessage;
	//private String registerMessage;
	private String chatMessage = "hello";
	private static ArrayList<Integer> responseValues = new ArrayList<>();
	public static boolean recv = false;

	public Connect() {
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("Connect.properties");
			Properties pro = new Properties();
			pro.load(inputStream);
			/*
			 * IP = pro.getProperty("IP"); PORT =
			 * Integer.parseInt(pro.getProperty("PORT"));
			 */

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
			/*chatThread = new Thread(new Runnable() {
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
							chatBox.setItems(msg);
					}
				}
			});*/
		} catch (ConnectException e) {
			e.printStackTrace();
			System.out.println("服务器连接失败");
		} catch (IOException e) {
			System.out.println("服务器连接失败");
		}
	}

	public static void send(String message) {
        try {
            /***** test *****/
            System.out.println("message len(utf-8 bytes): " + message.getBytes("utf-8").length);
            System.out.println("message: "+message);
            /***** test *****/
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
		outer: while (/* !Connect.recv */true) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (recv)
				for (Integer requestValue : requestValues) {
					for (Integer resonseValue : responseValues) {
						if (resonseValue == requestValue)
							break outer;
					}
				}
			i++;
			if (i == 100) {
				JOptionPane.showMessageDialog(null, "连接超时，请重试", "连接错误", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		}
	}
	public static void waitForRec2() {
		int i = 0;
		while (!Connect.recv) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/*if (recv)
				for (Integer requestValue : requestValues) {
					for (Integer resonseValue : responseValues) {
						if (resonseValue == requestValue)
							break outer;
					}
				}*/
			i++;
			if (i == 100) {
				JOptionPane.showMessageDialog(null, "连接超时，请重试", "连接错误", JOptionPane.INFORMATION_MESSAGE);
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
			System.out.println("服务器响应信息长度:" + len);
			byte[] buff = new byte[len];
			is.read(buff);
			msg = new String(buff,0,len);
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
			 case Type.Response.PLACECHESS_SUCCESS:
                handleGameAction(jsonObject);
                break;
			 case Type.Response.GROUP_CHAT_MSG:
				 handleChatMessage(jsonObject);
				 break;
			default:
				break;
			}
			responseValues.add(response_type);
			recv = true;
			//registerMessage = msg;
			//chatMessage = msg;
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("与服务器断开连接！");
		} catch (IOException e) {
			System.out.println("与服务器连接异常！");
		}
	}

	private void handleChatMessage(JSONObject jsonObject) {
		String string = jsonObject.getString("chatMessage");
		MessageQueue<String> messages = Client.getChatMessages();
		messages.add(string);
	}

	private void handleAccountCheck(boolean state) {
		SignupController.accountCheckSuccess = state;
		System.out.println("SingupController accountCheckSuccess:" + SignupController.accountCheckSuccess);
	}

	private void handleRegist(boolean state) {
		SignupController.registSuccess = state;
		System.out.println("SinupController registSuccess:" + SignupController.registSuccess);
	}

	private void handleLogin(boolean state) {
		LoginController.correct = state;
		System.out.println("LoginController correct:" + LoginController.correct);
	}

	private void handleFetchRoom(JSONObject jsonObject) {
		ArrayList<Room> roomList = Decoder.parseRoomList(jsonObject);
		if (roomList.size() != 0) {
			MessageQueue<Room> rooms = Client.getRooms();
			for (Room room : roomList) {
				rooms.add(room);
			}
			System.out.println("handle list:" + roomList);
		}
	}

	private void handleFetchPlayer(JSONObject jsonObject) {
		ArrayList<User> playerList = Decoder.parsePlayerList(jsonObject);
		if (playerList.size() != 0) {
			MessageQueue<User> players = Client.getPlayers();
			for (User user : playerList) {
				players.add(user);
			}
			System.out.println("handle list:" + playerList);
		}
	}
	private void handleGameAction(JSONObject jsonObject) {
        Client.getGameController().place(
                jsonObject.getIntValue("x"),
                jsonObject.getIntValue("y"),
                jsonObject.getIntValue("color"));
        if (jsonObject.getIntValue("action") == Type.Action.KILL) {
            ArrayList<Stone> deadList = Decoder.parseKillList(jsonObject);
            Client.getGameController().kill(deadList);
            Client.getGameController().checkKo(
                    jsonObject.getIntValue("x"),
                    jsonObject.getIntValue("y"),
                    deadList);
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

	// For C/C++ on Windows.
	public static byte[] intToByteLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return new byte[] { (byte) (n & 0xff), (byte) ((n >> 8) & 0xff), (byte) ((n >> 16) & 0xff),
				(byte) ((n >> 24) & 0xff) };
	}

	// For C/C++ on Linux/Unix.
	public static byte[] intToByteHH(int n) {
		return new byte[] { (byte) ((n >> 24) & 0xff), (byte) ((n >> 16) & 0xff), (byte) ((n >> 8) & 0xff),
				(byte) (n & 0xff) };
	}

	public static int byteToIntLH(byte[] bytes) {
		return ((bytes[3] & 0xFF) << 24) | ((bytes[2] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF);
	}

	public static int byteToIntHH(byte[] bytes) {
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
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

	/*public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}*/

	public Thread getReceiveThread() {
		return receiveThread;
	}
	/*
	public String getRegisterMessage() {
		return registerMessage;
	}

	public void setRegisterMessage(String registerMessage) {
		this.registerMessage = registerMessage;
	}*/

	public String getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	/*public void setChatBox(ChatBox chatBox) {
	}*/

	/*public Thread getChatThread() {
		return chatThread;
	}

	public void setChatThread(Thread chatThread) {
		this.chatThread = chatThread;
	}*/

}
