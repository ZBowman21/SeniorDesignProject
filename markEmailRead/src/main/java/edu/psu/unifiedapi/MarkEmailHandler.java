package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.account.GetLinkedPlainAccountArgs;
import edu.psu.unifiedapi.account.IGetLinkedPlainAccount;
import edu.psu.unifiedapi.auth.Credentials;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

/**
 * @author mthwate and Corey!
 */
public class MarkEmailHandler implements RequestHandler<MarkEmailRequest, Boolean> {

	@Override
	public Boolean handleRequest(MarkEmailRequest input, Context context) {
		Properties props = new Properties();

		//Authentication
		GetLinkedPlainAccountArgs aA = new GetLinkedPlainAccountArgs();
		aA.service = "webmail";
		aA.userId = input.userId;

		//Call authenticate with AuthArgs
		IGetLinkedPlainAccount authService = LambdaInvokerFactory.builder().build(IGetLinkedPlainAccount.class);
		Credentials creds = authService.getLinkedPlainAccount(aA);
		String username = creds.getUsername();
		String password = creds.getPassword();

		if(password != null) {
			props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.imap.socketFactory.port", "993");

			// Flags for unread messages
			Flags flags = new Flags();
			flags.add(Flags.Flag.SEEN);
			FlagTerm flagTerm = new FlagTerm(flags, false);

			Session session = Session.getDefaultInstance(props);

			try {
				Store store = session.getStore("imap");
				store.connect("email.psu.edu", username, password);

				Folder inbox = store.getFolder("INBOX"); //INBOX or Sent
				inbox.open(Folder.READ_WRITE);

				Message[] messages = inbox.search(flagTerm);

				//Implicitly marked as read when viewed.
				getMessage(messages[messages.length - input.start - 1]);
			} catch (Exception e) {
				e.printStackTrace();
				context.getLogger().log("Problem marking email as read" + e.toString());
				return false;
			}
			context.getLogger().log("Email marked as read.");
		}
		else{
			context.getLogger().log("Authentication failed.");
			return false;
		}
		return true;
	}

	private String getMessage(Part p){
		String s = "";
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