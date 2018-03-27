package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface IAddCapstoneData {
        @LambdaFunction(functionName = "insertCapstoneData")
        void addCapstoneData(AddCapstoneDataArgs args);
}
