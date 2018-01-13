package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.ReceiveEmailRequest;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author mthwate and Corey!
 */
public class ReceiveEmailHandler implements RequestHandler<ReceiveEmailRequest, ArrayList> {

	@Override
	public ArrayList handleRequest(ReceiveEmailRequest input, Context context) {

		String retStr = "No messages to display at this time.";
		ArrayList<String> returnMessages = new ArrayList();
		Properties props = new Properties();

		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//props.setProperty("mail.imap.ssl.enable", "true");
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

			for(int i = input.getStart(); i <= input.getFinish() && i < messages.length; i++){
				returnMessages.add("From " + messages[i].getFrom()[0].toString() + " at "
						+  messages[i].getReceivedDate() + " with a subject of "
						+ messages[i].getSubject() + ".\n Message reads\n"
						+ getMessage(messages[i]) + "\n\n");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		if(returnMessages.isEmpty())
			returnMessages.add(retStr);

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
