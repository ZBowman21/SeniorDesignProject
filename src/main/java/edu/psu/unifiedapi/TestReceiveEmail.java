package edu.psu.unifiedapi;

import edu.psu.unifiedapi.ReceiveEmailHandler;
import edu.psu.unifiedapi.ReceiveEmailRequest;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * Created by corey on 11/8/2017.
 */
public class TestReceiveEmail {
    public static void main(String[] args) {
        ReceiveEmailHandler hand = new ReceiveEmailHandler();
        String s = hand.handleRequest(new ReceiveEmailRequest("", ""), null);
        System.out.println(s);
    }
}
