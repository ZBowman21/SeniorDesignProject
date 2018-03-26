package edu.psu.unifiedapi.capstone.teamid;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface ICapstoneTeamId {
    @LambdaFunction(functionName = "capstoneTeamId")
    String getTeamId(CapstoneTeamIdArgs args);
}
