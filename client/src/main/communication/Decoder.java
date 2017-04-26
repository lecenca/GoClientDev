package src.main.communication;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import src.main.User;

public class Decoder {

		public static ArrayList parseJsontoArray(String  json) {
			ArrayList<User> list = (ArrayList<User>) JSON.parseArray(json,User.class);
			System.out.println(list);
			return list;
		}

		public static JSONObject parseObject(String json) {
			//String s = "{'name':'王五','age':'19'}";
			JSONObject jsonObject = JSON.parseObject(json);
			return jsonObject;
		}

		public static int registResponse(String json){
		    return (int)JSON.parseObject(json).get("id");
        }
}
