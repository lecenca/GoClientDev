package src.main.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 鍒樹繆寤�? on 2017/4/10.
 */

public class Connect {
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static String IP = "172.16.90.242";
	private final static int PORT = 10005;
	private static Socket socket;
	private static OutputStream os;
	private static InputStream is;
	private static PrintWriter pw;
	private static BufferedReader br;
	public Connect() {
		try {
			socket = new Socket(IP,PORT);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(os);
			br = new BufferedReader(new InputStreamReader(is));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接服务器失�?");
		}
	}
    public String sendAndReceive(String args) throws Exception {
    	String msg=null;
    	//BufferedReader br = null;
    	//PrintWriter pw = null;
        try {
            //2.寰楀埌socket璇诲啓娴�?
            //OutputStream os=socket.getOutputStream();
           // pw=new PrintWriter(os);
            //杈撳叆娴�?
           // InputStream is=socket.getInputStream();
          //  br=new BufferedReader(new InputStreamReader(is));
            //3.鍒╃敤娴佹寜鐓т竴瀹氱殑鎿嶄綔锛屽socket杩涜璇诲啓鎿嶄�?
            String info = new String(toHH(args.length())) + args;
            System.out.println("info:" + info);
            os.write(info.getBytes());
            //pw.flush();
            //socket.shutdownOutput();
            //鎺ユ敹鏈嶅姟鍣ㄧ殑鐩稿簲
            /*String reply=null;
            while(!((reply=br.readLine())==null)){
                System.out.println("鎺ユ敹鏈嶅姟鍣ㄧ殑淇℃伅锛�"+reply);
            }*/
            //create the buff 
            byte[] buff = new byte[1024];
            int len= is.read(buff);
            msg = new String(buff,0,len);
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
    }
    //
    public static String receiveMessage() {
    	String msg = null;
    	//创建字节数组缓冲�?
    	byte[] buff = new byte[1024];
    	try {
			int len = is.read(buff);
			msg = new String(buff,0,len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			return msg;
		}
		
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
}
