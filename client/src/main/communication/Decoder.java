package src.main.communication;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import src.main.UserInfo;

public class Decoder {
	
		public static ResponseType jsonToJavaBean(String json) {
			//String s = "{'name':'john','age':'19','gender':'��'}";
			ResponseType responseType = JSON.parseObject(json,ResponseType.class);
			System.out.println(responseType);
			return responseType;
		}

		public static ArrayList parseJsontoArray(String  json) {
			ArrayList<UserInfo> list = (ArrayList<UserInfo>) JSON.parseArray(json,UserInfo.class);
			System.out.println(list);
			return list;
		}
		public static JSONObject jsonToJsonObject(String json) {
			//String s = "{'name':'王五','age':'19'}";
			JSONObject jsonObject = JSON.parseObject(json);
			return jsonObject;
		}
}
