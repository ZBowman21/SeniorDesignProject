package edu.psu.unifiedapi.capstone.teamid;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.utils.CapstoneDbUtils;


public class CapstoneTeamId implements RequestHandler<CapstoneTeamIdArgs,String> {

    @Override
    public String handleRequest(CapstoneTeamIdArgs input, Context context) {

        String teamId = CapstoneDbUtils.getTeamId(input.username);

        if (teamId == null) {
            throw new RuntimeException("Cannot get team ID");
        }

        return teamId;
    }
}
