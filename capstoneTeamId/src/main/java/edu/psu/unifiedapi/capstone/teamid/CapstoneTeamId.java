package edu.psu.unifiedapi.capstone.teamid;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;


public class CapstoneTeamId implements RequestHandler<CapstoneTeamIdArgs,Void> {
    private final String path = "/AgileTask/EGetMyTeamID";

    @Override
    public Void handleRequest(CapstoneTeamIdArgs input, Context context) {

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, null);
        ResponseTeamId response;
        try {
            response = (ResponseTeamId) cap.CapCall(ResponseTeamId.class, context);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Add the teamId to the db


        return null;
    }
}
