package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * @author mthwate and Corey!
 */
public class ReceiveEmailHandler implements RequestHandler<ReceiveEmailRequest, String> {

	@Override
	public String handleRequest(ReceiveEmailRequest input, Context context) {

		String retStr = "No messages to display at this time.";

		Properties props = new Properties();

		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.port", "993");

		// Flags for unread messages
		Flags flags = new Flags();
		flags.add(Flags.Flag.SEEN);
		FlagTerm flagTerm = new FlagTerm(flags, false);

		Session session = Session.getDefaultInstance(props);

		try {

			Store store = session.getStore("imap");
			store.connect("email.psu.edu", input.getUsername(), input.getPassword());

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			Message[] messages = inbox.search(flagTerm);

			for(int i = 0; i < 10 && i < messages.length; i++){
				retStr = "From " + messages[i].getFrom()[0].toString() + " at "
						+  messages[i].getReceivedDate() + " with a subject of "
						+ messages[i].getSubject() + ".\n"
						+ getMessage(messages[i]);
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return retStr;
	}

	private <T extends Part> String getMessage(T m){
		String s = "Could not read the message's content.";
		try {
			if (m.getContentType().equals("text/plain")) {
				s = (String) m.getContent();
			} else if (m.getContentType().equals("multipart")) {
				s = "";
				Multipart mp = (Multipart)m.getContent();
				for(int i = 0; i < mp.getCount(); i++){
					s += getMessage(mp.getBodyPart(i)) + "\n";
				}
			}
		}catch(Exception e){
		}
		return s;
	}

}
