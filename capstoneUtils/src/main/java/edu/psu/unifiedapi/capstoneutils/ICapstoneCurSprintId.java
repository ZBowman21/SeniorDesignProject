package edu.psu.unifiedapi.capstoneutils;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneCurSprintId {
    @LambdaFunction(functionName = "capstoneCurSprintId")
    String getSprintId(CapstoneCurSprintIdArgs args);
}
