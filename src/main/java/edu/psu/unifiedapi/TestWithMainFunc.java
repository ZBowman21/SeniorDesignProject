package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class TestWithMainFunc {

    public static void main(String[] args){
        EmailArgs input = new EmailArgs();
        input.body = "TEST";
        input.destination = "zob5056";
        input.subject = "MAIN TEST";
        input.username = "testing";

        System.out.println(handleRequest(input));
    }

    static public String handleRequest(EmailArgs input) {
        String output;
        try {
            Email testEmail = new SimpleEmail();
            testEmail.setHostName("authsmtp.psu.edu");
            //testEmail.setSmtpPort(2525);
            //testEmail.setAuthenticator(new DefaultAuthenticator(input.username, input.password));
            testEmail.setSSLOnConnect(true);
            testEmail.setFrom(input.username + "@psu.edu");
            testEmail.addTo(input.destination + "@psu.edu");
            testEmail.setSubject(input.subject);
            testEmail.setMsg(input.body);
            testEmail.setSocketTimeout(2000);
            testEmail.setSocketConnectionTimeout(2000);
            testEmail.send();
            output = "Message sent on " + testEmail.getSentDate().toString() + ". " + testEmail.getSmtpPort();


        } catch (EmailException e) {
            output = "Message failed to send.";
        }
        return output;
    }
}
