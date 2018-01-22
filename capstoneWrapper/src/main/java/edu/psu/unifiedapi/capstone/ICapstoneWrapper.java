package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;

public interface ICapstoneWrapper {
    @LambdaFunction(functionName = "getLinkedPlainAccount")
    Object getLinkedPlainAccount(CapstoneWrapperArgs args);
}
