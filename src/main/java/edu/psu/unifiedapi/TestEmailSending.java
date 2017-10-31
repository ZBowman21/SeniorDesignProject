package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class TestEmailSending implements RequestHandler<EmailArgs, String> {

    @Override
    public String handleRequest(EmailArgs input, Context context) {
        String output;
        try {
            Email testEmail = new SimpleEmail();
            testEmail.setHostName("authsmtp.psu.edu");
            //testEmail.setSmtpPort(2525);
            testEmail.setAuthenticator(new DefaultAuthenticator(input.username, input.password));
            testEmail.setSSLOnConnect(true);
            testEmail.setFrom(input.username + "@psu.edu");
            testEmail.addTo(input.destination + "@psu.edu");
            testEmail.setSubject(input.subject);
            testEmail.setMsg(input.body);
            testEmail.setSocketTimeout(5000);
            testEmail.setSocketConnectionTimeout(5000);
            testEmail.send();
            output = "Message sent on " + testEmail.getSentDate().toString() + ".";
            context.getLogger().log(output);
        } catch (EmailException e) {
            output = "Message failed to send. " + e.getCause();
            context.getLogger().log(output);
        }
        return output;
    }
}