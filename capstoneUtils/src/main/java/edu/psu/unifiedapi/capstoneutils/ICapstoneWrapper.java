package edu.psu.unifiedapi.capstoneutils;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneWrapper {
    @LambdaFunction(functionName = "capstoneWrapper")
    Object send(CapstoneWrapperArgs args);
}
