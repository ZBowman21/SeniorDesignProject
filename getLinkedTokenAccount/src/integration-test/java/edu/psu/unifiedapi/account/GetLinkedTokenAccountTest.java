package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaFunctionException;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import edu.psu.unifiedapi.auth.Credentials;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author mthwate
 */
public class GetLinkedTokenAccountTest {

	@Test
	public void invalidCredentials() {
		IGetLinkedTokenAccount service = LambdaInvokerFactory.builder().build(IGetLinkedTokenAccount.class);

		GetLinkedTokenAccountArgs args = new GetLinkedTokenAccountArgs();

		args.userId = "user";
		args.service = "webmail";

		String message = null;

		try {
			service.getLinkedTokenAccount(args);
		} catch (LambdaFunctionException e) {
			message = e.getMessage();
		}

		assertEquals(message, "Account not found");
	}

}