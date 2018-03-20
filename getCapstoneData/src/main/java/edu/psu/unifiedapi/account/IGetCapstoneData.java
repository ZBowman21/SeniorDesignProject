package edu.psu.unifiedapi.getcapstonedata;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IGetCapstoneData {
    @LambdaFunction(functionName = "getCapstoneData")
    String getCapstoneData(GetCapstoneDataArgs args);
}
