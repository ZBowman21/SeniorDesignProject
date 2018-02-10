package edu.psu.unifiedapi;

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
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

/**
 * @author mthwate and Corey!
 */
public class ReceiveEmailHandler implements RequestHandler<ReceiveEmailRequest, EmailObject> {

	@Override
	public EmailObject handleRequest(ReceiveEmailRequest input, Context context) {
		EmailObject returnEmail = new EmailObject();
		Properties props = new Properties();

		//Authentication
		GetLinkedPlainAccountArgs aA = new GetLinkedPlainAccountArgs();
		aA.passphrase = input.password;
		aA.service = "webmail";
		aA.userId = input.username;

		//Call authenticate with AuthArgs
		IGetLinkedPlainAccount authService = LambdaInvokerFactory.builder().build(IGetLinkedPlainAccount.class);
		Credentials creds = authService.getLinkedPlainAccount(aA);
		input.username = creds.getUsername();
		input.password = creds.getPassword();

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
					//format return strings
					String from = messages[messages.length - input.start - 1].getFrom()[0].toString();

					Date d = messages[messages.length - input.start - 1].getReceivedDate();

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(d.getTime() - 18000000);

					SimpleDateFormat estDF = new SimpleDateFormat("MMMM dd, yyyy, hh:mm a");
					String date = estDF.format(c.getTime());

					String subject =messages[messages.length - input.start - 1].getSubject();
					String message = getMessage(messages[messages.length - input.start - 1]);

					//also return # of unread (size of messages)
					returnEmail = new EmailObject(from, date, subject, message, messages.length);
				}
				else{
					returnEmail = new EmailObject("","","","",0);
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
		return returnEmail;
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
