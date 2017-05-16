package src.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.Assert;
import src.main.Room;

import java.util.ArrayList;

public class DecoderTest {

    @Test
    public void parseRoomListTest() {
        String json = "{\"rooms_list\":[]}";
        JSONObject object = JSONObject.parseObject(json);
        JSONArray array = object.getJSONArray("rooms_list");
        Assert.assertNotNull(array);
        Assert.assertEquals(0, array.size());
        ArrayList<Room> rooms = new ArrayList<Room>(JSON.parseArray(array.toJSONString(), Room.class));
        Assert.assertNotNull(rooms);
        Assert.assertEquals(0, rooms.size());
    }

}
