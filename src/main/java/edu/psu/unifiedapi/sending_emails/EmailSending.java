package edu.psu.unifiedapi.sending_emails;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailSending implements RequestHandler<EmailArgs, String> {

    @Override
    public String handleRequest(EmailArgs eA, Context context) {
        String output;
        try {
            Email testEmail = new SimpleEmail();
            testEmail.setHostName("authsmtp.psu.edu");
            //testEmail.setSmtpPort(2525);
            //testEmail.setAuthenticator(new DefaultAuthenticator(eA.username, eA.password));
            testEmail.setSSLOnConnect(true);
            testEmail.setFrom(eA.username + "@psu.edu");
            testEmail.addTo(eA.destination + "@psu.edu");
            testEmail.setSubject(eA.subject);
            testEmail.setMsg(eA.body);
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