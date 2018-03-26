package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import edu.psu.unifiedapi.auth.Credentials;

/**
 * @author mthwate
 */
public interface IGetLinkedTokenAccount {

	@LambdaFunction(functionName = "getLinkedTokenAccount")
	String getLinkedTokenAccount(GetLinkedTokenAccountArgs args);

}
