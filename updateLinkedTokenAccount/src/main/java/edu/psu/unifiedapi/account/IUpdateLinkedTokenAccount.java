package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IUpdateLinkedTokenAccount {
    @LambdaFunction(functionName = "updateLinkedTokenAccount")
    boolean updateLinkedTokenAccount(UpdateLinkedTokenAccountArgs args);
}
