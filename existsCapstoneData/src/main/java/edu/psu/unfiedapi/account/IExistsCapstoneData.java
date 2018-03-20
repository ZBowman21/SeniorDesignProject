package edu.psu.unfiedapi.existCapstoneData;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IExistsCapstoneData {
    @LambdaFunction(functionName = "existsCapstoneData")
    void existsCapstoneData(ExistsCapstoneDataArgs args);
}
