package edu.psu.unifiedapi.authentication;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import edu.psu.unifiedapi.sending_emails.EmailArgs;
import edu.psu.unifiedapi.sending_emails.EmailSendingTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mthwate
 */
public class AuthenticateTest {

	private interface Auth {
		@LambdaFunction(functionName = "getAuth")
		String auth(AuthArgs args);
	}

	@Test
	public void invalidCredentials() {
		Auth service = LambdaInvokerFactory.builder().build(Auth.class);

		AuthArgs args = new AuthArgs();

		args.username = "user";
		args.passphrase = "pass";
		args.service = "webmail";

		String result = service.auth(args);

		assertNull(result);
	}

}