package src.main.communication;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import src.main.UserInfo;

public class Decoder {

		public static ArrayList parseJsontoArray(String  json) {
			ArrayList<UserInfo> list = (ArrayList<UserInfo>) JSON.parseArray(json,UserInfo.class);
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
