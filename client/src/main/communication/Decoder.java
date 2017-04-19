package src.main.communication;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import src.main.UserInfo;

public class Decoder {
	
		public UserInfo jsonToJavaBean(String json) {
			String s = "{'name':'john','age':'19','gender':'ÄÐ'}";
			UserInfo userInfo = JSON.parseObject(s,UserInfo.class);
			System.out.println(userInfo);
			return userInfo;
		}

		public static ArrayList parseJsontoArray(String  json) {
			ArrayList<UserInfo> list = (ArrayList<UserInfo>) JSON.parseArray(json,UserInfo.class);
			System.out.println(list);
			return list;
		}
}
