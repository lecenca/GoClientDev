package src.test;

import org.junit.Test;
import src.main.communication.Connect;

public class ConnectTest {

    @Test
    public void byteToIntHHTest(){
        long begin = System.currentTimeMillis();
        for(int i = 0; i < 1000000; ++i){
            int res = Connect.byteToIntHH(Connect.intToByteHH(i));
            res = res + 1;
        }
        long end = System.currentTimeMillis();
        System.out.println(end-begin+"ms");
    }
}
