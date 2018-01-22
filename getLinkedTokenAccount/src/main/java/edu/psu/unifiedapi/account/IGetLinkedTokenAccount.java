package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IGetLinkedTokenAccount {
    @LambdaFunction(functionName = "getLinkedTokenAccount")
    String getLinkedTokenAccount(GetLinkedTokenAccountArgs args);
}
