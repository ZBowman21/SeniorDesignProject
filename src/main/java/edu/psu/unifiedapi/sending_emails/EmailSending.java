package edu.psu.unifiedapi.sending_emails;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.authentication.AuthArgs;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailSending implements RequestHandler<EmailArgs, String> {

    private interface Auth{
        @LambdaFunction(functionName = "getAuth")
        String auth(AuthArgs aA);
    }

    @Override
    public String handleRequest(EmailArgs eA, Context context) {
        String output;

        AuthArgs aA = new AuthArgs();
        aA.passphrase = eA.password;
        aA.service = "webmail";
        aA.username = eA.username;

        //Call authenticate with AuthArgs
        Auth authService = LambdaInvokerFactory.builder()
                .build(Auth.class);
        eA.password = authService.auth(aA);

        if(eA.password == null){
            output = "Authentication failed.. Please try again later.";
        }
        else {
            try {
                Email testEmail = new SimpleEmail();
                testEmail.setHostName("authsmtp.psu.edu");
                testEmail.setAuthenticator(new DefaultAuthenticator(eA.username, eA.password));
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
        }
        return output;
    }
}