package edu.psu.unifiedapi.sending_emails;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.account.GetLinkedPlainAccountArgs;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class  EmailSending implements RequestHandler<EmailArgs, Boolean> {

    private interface Auth{
        @LambdaFunction(functionName = "getLinkedPlainAccount")
        String auth(GetLinkedPlainAccountArgs aA);
    }

    @Override
    public Boolean handleRequest(EmailArgs eA, Context context) {
        boolean output = false;

        GetLinkedPlainAccountArgs aA = new GetLinkedPlainAccountArgs();
        aA.passphrase = eA.password;
        aA.service = "webmail";
        aA.username = eA.username;

        //Call authenticate with AuthArgs
        Auth authService = LambdaInvokerFactory.builder().build(Auth.class);
        eA.password = authService.auth(aA);

        if(eA.password != null) {
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

                //copy to outbox
                Properties prop = new Properties();
                prop.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                prop.setProperty("mail.imap.socketFactory.port", "993");

                Session session = Session.getDefaultInstance(prop);

                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(eA.username + "@psu.edu");
                msg.setRecipients(Message.RecipientType.TO, eA.destination + "@psu.edu");
                msg.setSubject(eA.subject);
                msg.setSentDate(new Date());
                msg.setText(eA.body);

                Store store = session.getStore("imap");
                store.connect("email.psu.edu", eA.username, eA.password);

                //check for "Sent" folder if doesn't exist, make it.
                if(!store.getFolder("Sent").exists()) {
                    boolean passFail = createFolder(store.getDefaultFolder(), "Sent");
                    if(!passFail){
                        context.getLogger().log("Failed to create folder \"Sent\"");
                    }
                }

                Folder folder = store.getFolder("Sent");
                folder.open(Folder.READ_WRITE);
                folder.appendMessages(new Message[]{msg});

                output = true;
                context.getLogger().log("Message sent on " + testEmail.getSentDate().toString() + ".");
            } catch (Exception e) {
                context.getLogger().log("Message failed to send. " + e.getCause());
            }
        } else {
            context.getLogger().log("Authentication failed");
        }
        return output;
    }

    private static boolean createFolder(Folder parent, String folderName)
    {
        boolean isCreated;

        try
        {
            Folder newFolder = parent.getFolder(folderName);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
            //System.out.println("created: " + isCreated);

        } catch (Exception e)
        {
            System.out.println("Error creating folder: " + e.getMessage());
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }
}