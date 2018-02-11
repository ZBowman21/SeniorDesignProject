package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import edu.psu.unifiedapi.auth.Credentials;

public interface IAddLinkedPlainAccount {

	@LambdaFunction
	void addLinkedPlainAccount(AddLinkedPlainAccountArgs args);

}
