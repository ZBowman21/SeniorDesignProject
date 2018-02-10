package edu.psu.unifiedapi;

/**
 * Created by corey on 11/8/2017.
 */
public class TestReceiveEmail {
    public static void main(String[] args) {
        ReceiveEmailHandler hand = new ReceiveEmailHandler();
        EmailObject s = hand.handleRequest(new ReceiveEmailRequest("mkb5413", 0), null);

        System.out.println(s.from + " " + s.date + " " + s.subject + " " + s.body + "\n" + s.unread);
    }
}
