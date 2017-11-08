package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * @author mthwate
 */
public class ReceiveEmailHandler implements RequestHandler<ReceiveEmailRequest, String> {

	@Override
	public String handleRequest(ReceiveEmailRequest input, Context context) {

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

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
