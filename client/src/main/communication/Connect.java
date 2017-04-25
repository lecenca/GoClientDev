package src.main.communication;

import com.alibaba.fastjson.JSONObject;
import src.main.ThreadLock;
import src.main.view.ChatBox;
import src.main.view.SignUpController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connect {
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static String IP = "172.16.90.242";
	private final static int PORT = 10005;
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
			socket = new Socket(IP,PORT);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(os);
			br = new BufferedReader(new InputStreamReader(is));
			receiveThread = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("接收线程启动");
					while(true) {
                        receive();
                        /*try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
				}
			});
			chatThread = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("鑱婂ぉ绾跨▼鍚姩");
					// TODO Auto-generated method stub
					while(true) {
						String msg = null;
						while(msg == null)
							msg = chatMessage;
						System.out.println(msg);
						if(chatBox != null)
							chatBox.sentSentence(msg);
                        /*try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("服务器连接失败");
		}
	}
    public String sendAndReceive(String args) throws Exception {
    	String msg=null;
    	//BufferedReader br = null;
    	//PrintWriter pw = null;
        try {
            //2.瀵版鍩宻ocket鐠囪鍟撳ù锟�?
            //OutputStream os=socket.getOutputStream();
           // pw=new PrintWriter(os);
            //鏉堟挸鍙嗗ù锟�?
           // InputStream is=socket.getInputStream();
          //  br=new BufferedReader(new InputStreamReader(is));
            //3.閸掆晝鏁ゅù浣瑰瘻閻撗傜鐎规氨娈戦幙宥勭稊閿涘苯顕畇ocket鏉╂稖顢戠拠璇插晸閹垮秳锟�?
            String info = new String(toHH(args.length())) + args;
            System.out.println("info:" + info);
            os.write(info.getBytes());
            //pw.flush();
            //socket.shutdownOutput();
            //閹恒儲鏁归張宥呭閸ｃ劎娈戦惄绋跨安
            /*String reply=null;
            while(!((reply=br.readLine())==null)){
                System.out.println("閹恒儲鏁归張宥呭閸ｃ劎娈戞穱鈩冧紖閿涳拷"+reply);
            }*/
            //create the buff 
            byte[] buff = new byte[1024];
            int len= is.read(buff);
            msg = new String(buff,0,len);
            //msg = br.readLine();
           // socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	return msg;
        }
    }
    
    public static void sendMessage(String msg) {
    	//get the outputStream of socket
		//OutputStream outputStream = socket.getOutputStream();
		//create the printwriter
		//pw = new PrintWriter(os,true);
		pw.write(msg + LINE_SEPARATOR);
		pw.flush();
		recv = false;
    }

    public void receive() {
    	String msg = null;
    	byte[] buff = new byte[1024];
    	try {
			int len = is.read(buff);
			msg = new String(buff,0,len);
            System.out.println("从服务器收到: " + msg);
            JSONObject jsonObject = Decoder.parseObject(msg);
            int response_type = jsonObject.getIntValue("response_type");
            System.out.println("response_type：" + response_type);
            switch(response_type) {
                case ResponseType.ACCOUNT_CHECK_SUCCESS:
                    handleAccountCheck(true);break;
                case ResponseType.ACCOUNT_CHECK_FAILED:
                    handleAccountCheck(false);break;
                case ResponseType.REGIST_SUCCESS:
                    handleRegist(true);break;
                case ResponseType.REGIST_FAILED:
                    handleRegist(false);break;
                case ResponseType.LOGIN_SUCCESS:
                    ThreadLock.lock.lock();
                    loginMessage = "false";
                    ThreadLock.client.signalAll();
                    ThreadLock.lock.unlock();
                    break;
                case ResponseType.LOGIN_FAILED:
                    loginMessage = "false";
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

    private void handleAccountCheck(boolean state){
        SignUpController.accountExist = state;
    }

    private void handleRegist(boolean state){
        SignUpController.registSuccess = state;
    }

    private byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    //
    private byte[] toHH(int n) {
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
