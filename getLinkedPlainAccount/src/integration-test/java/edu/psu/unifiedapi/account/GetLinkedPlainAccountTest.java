package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author mthwate
 */
public class GetLinkedPlainAccountTest {

	private interface Auth {
		@LambdaFunction(functionName = "getLinkedPlainAccount")
		String auth(GetLinkedPlainAccountArgs args);
	}

	@Test
	public void invalidCredentials() {
		Auth service = LambdaInvokerFactory.builder().build(Auth.class);

		GetLinkedPlainAccountArgs args = new GetLinkedPlainAccountArgs();

		args.username = "user";
		args.passphrase = "pass";
		args.service = "webmail";

		String result = service.auth(args);

		assertNull(result);
	}

}