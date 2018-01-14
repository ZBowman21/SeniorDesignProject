package edu.psu.unifiedapi;

import edu.psu.unifiedapi.ReceiveEmailHandler;
import edu.psu.unifiedapi.ReceiveEmailRequest;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.ArrayList;

/**
 * Created by corey on 11/8/2017.
 */
public class TestReceiveEmail {
    public static void main(String[] args) {
        ReceiveEmailHandler hand = new ReceiveEmailHandler();
        ArrayList<EmailObject> s = hand.handleRequest(new ReceiveEmailRequest("mkb5413", "cheese", 4), null);

        for(int i = 0; i < s.size(); i++){
            System.out.println(s.get(i).from + " " + s.get(i).date);
        }
    }
}
