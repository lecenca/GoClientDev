package src.test;

import org.junit.Assert;
import org.junit.Test;
import src.main.communication.Connect;

public class ConnectTest {

    @Test
    public void intToByteHHTest() {
        byte[] bytes1 = Connect.intToByteHH(0x00000000);
        Assert.assertEquals(0x00, bytes1[0] & 0xFF);
        Assert.assertEquals(0x00, bytes1[1] & 0xFF);
        Assert.assertEquals(0x00, bytes1[2] & 0xFF);
        Assert.assertEquals(0x00, bytes1[3] & 0xFF);
        byte[] bytes2 = Connect.intToByteHH(0x01020304);
        Assert.assertEquals(0x01, bytes2[0] & 0xFF);
        Assert.assertEquals(0x02, bytes2[1] & 0xFF);
        Assert.assertEquals(0x03, bytes2[2] & 0xFF);
        Assert.assertEquals(0x04, bytes2[3] & 0xFF);
        byte[] bytes3 = Connect.intToByteHH(0xAABBCCDD);
        Assert.assertEquals(0xAA, bytes3[0] & 0xFF);
        Assert.assertEquals(0xBB, bytes3[1] & 0xFF);
        Assert.assertEquals(0xCC, bytes3[2] & 0xFF);
        Assert.assertEquals(0xDD, bytes3[3] & 0xFF);
    }

    @Test
    public void byteToIntHHTest() {
        byte[] bytes1 = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        Assert.assertEquals(0, Connect.byteToIntHH(bytes1));
        byte[] bytes2 = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
        Assert.assertEquals(255, Connect.byteToIntHH(bytes2));
        byte[] bytes3 = new byte[]{(byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        Assert.assertEquals(2147483647, Connect.byteToIntHH(bytes3));
    }

}
