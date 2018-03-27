package edu.psu.unifiedapi.capstone.curclockin;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneCurClockIn {
    @LambdaFunction(functionName = "capstoneCurClockIn")
    int getCurClockIn(CapstoneCurClockInArgs args);
}
