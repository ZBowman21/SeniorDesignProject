package edu.psu.unifiedapi.capstone.utils;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneWrapper {
    @LambdaFunction(functionName = "capstoneWrapper")
    CapstoneResponse send(CapstoneWrapperArgs args);
}
