package edu.psu.unifiedapi.capstoneClockOut;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneClockOut implements RequestHandler<CapstoneClockOutArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskIn";

    @Override
    public Boolean handleRequest(CapstoneClockOutArgs input, Context context) {

        String params = "taskid=" + input.taskId + "&teamid=" + input.teamId;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        ResponseTaskedOut response = (ResponseTaskedOut) cap.CapCall(ResponseTaskedOut.class, context);

        return response.response.stopped;
    }
}
