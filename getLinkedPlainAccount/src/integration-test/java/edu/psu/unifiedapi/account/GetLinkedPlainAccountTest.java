package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import edu.psu.unifiedapi.auth.Credentials;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author mthwate
 */
public class GetLinkedPlainAccountTest {

	@Test
	public void invalidCredentials() {
		IGetLinkedPlainAccount service = LambdaInvokerFactory.builder().build(IGetLinkedPlainAccount.class);

		GetLinkedPlainAccountArgs args = new GetLinkedPlainAccountArgs();

		args.username = "user";
		args.passphrase = "pass";
		args.service = "webmail";

		Credentials result = service.getLinkedPlainAccount(args);

		assertNull(result);
	}

}