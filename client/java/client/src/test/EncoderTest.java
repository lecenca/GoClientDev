package src.test;

import org.junit.Test;
import org.junit.Assert;
import src.main.communication.Encoder;

public class EncoderTest {

    @Test
    public void checkAccountTest() {
        String account = "SimpleTest";
        String jsonExcept = "{\"request_type\":0,\"account\":\"SimpleTest\"}";
        Assert.assertEquals(jsonExcept, Encoder.chechAccountRequest(account));
    }

}
