package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import edu.psu.unifiedapi.auth.Credentials;

/**
 * @author mthwate
 */
public interface IGetLinkedPlainAccount {

	@LambdaFunction(functionName = "getLinkedPlainAccount")
	Credentials getLinkedPlainAccount(GetLinkedPlainAccountArgs args);

}
