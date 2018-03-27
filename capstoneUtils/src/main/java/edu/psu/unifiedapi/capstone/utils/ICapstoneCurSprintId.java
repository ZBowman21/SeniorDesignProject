package edu.psu.unifiedapi.capstone.utils;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneCurSprintId {
    @LambdaFunction(functionName = "capstoneCurSprintId")
    String getSprintId(CapstoneCurSprintIdArgs args);
}
