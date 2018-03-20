package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IAddCapstoneData {
        @LambdaFunction(functionName = "addCapstoneData")
        void addCapstoneData(AddCapstoneDataArgs args);
}
