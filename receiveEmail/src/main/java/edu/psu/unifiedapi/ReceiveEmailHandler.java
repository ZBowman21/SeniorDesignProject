package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.account.GetLinkedPlainAccountArgs;
import edu.psu.unifiedapi.account.IGetLinkedPlainAccount;
import edu.psu.unifiedapi.auth.Credentials;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author mthwate and Corey!
 */
public class ReceiveEmailHandler implements RequestHandler<ReceiveEmailRequest, ArrayList> {

	@Override
	public ArrayList<EmailObject> handleRequest(ReceiveEmailRequest input, Context context) {
		//String retStr = "No messages to display at this time.";
		ArrayList<EmailObject> returnMessages = new ArrayList<>();
		Properties props = new Properties();

		//Authentication
		GetLinkedPlainAccountArgs aA = new GetLinkedPlainAccountArgs();
		aA.passphrase = input.password;
		aA.service = "webmail";
		aA.username = input.username;

		//Call authenticate with AuthArgs
		IGetLinkedPlainAccount authService = LambdaInvokerFactory.builder().build(IGetLinkedPlainAccount.class);
		input.password = authService.getLinkedPlainAccount(aA).getPassword();

		if(input.password != null) {
			props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imap.socketFactory.port", "993");

			// Flags for unread messages
			Flags flags = new Flags();
			flags.add(Flags.Flag.SEEN);
			FlagTerm flagTerm = new FlagTerm(flags, false);

			Session session = Session.getDefaultInstance(props);

			try {
				Store store = session.getStore("imap");
				store.connect("email.psu.edu", input.username, input.password);

				Folder inbox = store.getFolder("INBOX"); //INBOX or Sent
				inbox.open(Folder.READ_ONLY);

				Message[] messages = inbox.search(flagTerm);

				if(messages.length > 0) {
					//also return # of unread (size of messages)
					returnMessages.add(new EmailObject(messages[input.start].getFrom()[0].toString(),
							messages[input.start].getReceivedDate().toString(), messages[input.start].getSubject(),
							getMessage(messages[input.start]), messages.length));
				}
				else{
					returnMessages.add(new EmailObject("","","","",0));
				}

			} catch (MessagingException e) {
				e.printStackTrace();
				//context.getLogger().log("Problem retrieving emails: " + e.toString());
			}
			//context.getLogger().log("Emails retrieved.");
		}
		else{
			//context.getLogger().log("Authentication failed.");
		}
		return returnMessages;
	}

	private String getMessage(Part p){
		String s = "Could not read the message's content.";
		try {
			String tmp = p.getContentType();
			if (tmp.contains("text/plain")) {
				s = (String) p.getContent();
			} else if (tmp.contains("multipart")) {
				s = "";
				Multipart mp = (Multipart)p.getContent();
				for(int i = 0; i < mp.getCount(); i++){
					s += getMessage(mp.getBodyPart(i)) + "\n";
				}
			}
		}catch(Exception e){
			s = e.getMessage();
		}
		return s;
	}
}
