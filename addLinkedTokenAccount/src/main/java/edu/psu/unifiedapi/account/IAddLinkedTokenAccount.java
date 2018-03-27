package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IAddLinkedTokenAccount {

	@LambdaFunction
	void addLinkedTokenAccount(AddLinkedTokenAccountArgs args);

}
