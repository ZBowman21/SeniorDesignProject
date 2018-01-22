package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IUpdateLinkedTokenAccount {
    @LambdaFunction(functionName = "updateLinkedTokenAccount")
    String updateLinkedTokenAccount(UpdateLinkedTokenAccountArgs args);
}
