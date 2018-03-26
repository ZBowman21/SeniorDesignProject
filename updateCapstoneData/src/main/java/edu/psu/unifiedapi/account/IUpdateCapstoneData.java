package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IUpdateCapstoneData {
    @LambdaFunction(functionName = "updateCapstoneData")
    boolean updateCapstoneData(UpdateCapstoneDataArgs args);
}
