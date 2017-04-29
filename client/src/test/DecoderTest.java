package src.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.Assert;
import src.main.Room;

import java.util.ArrayList;

/**
 * Created by 刘俊延 on 2017/4/28.
 */
public class DecoderTest {

    @Test
    public void parseRoomListTest(){
        String json = "{\"rooms_list\":[]}";
        JSONObject object = JSONObject.parseObject(json);
        JSONArray array = object.getJSONArray("rooms_list");
        Assert.assertNotNull(array);
        Assert.assertEquals(0,array.size());
        ArrayList<Room> rooms = new ArrayList<Room>(JSON.parseArray(array.toJSONString(),Room.class));
        Assert.assertNotNull(rooms);
        Assert.assertEquals(0,rooms.size());
    }
    private byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}
    @Test
    public void getInt() {
    	long begin = System.currentTimeMillis();
    	String str = new String(toHH(1000));
    	String s="";
    	byte[] bytes = str.getBytes();
    	for(byte b : bytes) {
    		b = (byte) (b  & 0x000000FF);
    		System.out.println(Integer.toBinaryString(b));
    		s+=Integer.toBinaryString(b);
    	}
    	System.out.println(s);
    	System.out.println(Integer.parseInt(s, 2));
    	long end = System.currentTimeMillis();
    	System.out.println(end - begin + "毫秒");
    }
    @Test
    public void getInt2() {
    	long begin = System.currentTimeMillis();
    	String str = new String(toHH(1000));
    	StringBuilder sb = new StringBuilder();
    	byte[] bytes = str.getBytes();
    	for(byte b : bytes) {
    		sb.append(Integer.toBinaryString(b));
    	}
    	System.out.println(Integer.parseInt(sb.toString(), 2));
    	long end = System.currentTimeMillis();
    	System.out.println(end - begin + "毫秒");
    }
}
