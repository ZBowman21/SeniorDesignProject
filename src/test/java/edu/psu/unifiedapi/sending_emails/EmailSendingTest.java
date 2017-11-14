package edu.psu.unifiedapi.sending_emails;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import edu.psu.unifiedapi.authentication.AuthArgs;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mthwate
 */
public class EmailSendingTest {

	private interface SendEmail {
		@LambdaFunction(functionName = "sendEmail")
		Boolean sendEmail(EmailArgs args);
	}

	@Test
	public void invalidCredentials() {
		SendEmail service = LambdaInvokerFactory.builder().build(SendEmail.class);

		EmailArgs args = new EmailArgs();

		args.username = "user";
		args.password = "pass";
		args.destination = "dest";
		args.subject = "Test";
		args.body = "This is a test";

		Boolean result = service.sendEmail(args);

		assertFalse(result);
	}

}