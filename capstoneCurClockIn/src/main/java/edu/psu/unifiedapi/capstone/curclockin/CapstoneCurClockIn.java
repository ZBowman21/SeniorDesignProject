package edu.psu.unifiedapi.capstone.curclockin;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneCurClockIn implements RequestHandler<CapstoneCurClockInArgs,Integer> {
    private final String path = "/AgileTask/EGetActiveTaskInstance";

    @Override
    public Integer handleRequest(CapstoneCurClockInArgs input, Context context) {
        // Get team id from db here... still need to build table for capstone... gotta ask Wizard Matt...

        String teamId = "CSSE-BD-Class2018-002";
        String params = "teamid=" + teamId;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        ResponseClockIn response;
        try {
            response = (ResponseClockIn) cap.CapCall(ResponseClockIn.class, context);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        // This is only needed if Brandon wants it.
//        if(response.response.activeTask) {
//            throw new RuntimeException("No Tasks to clock out")
//        }

        return response.response.activeTaskID;
    }
}
