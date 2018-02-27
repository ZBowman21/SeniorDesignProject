package edu.psu.unifiedapi.capstone.clockin;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneClockIn implements RequestHandler<CapstoneClockInArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskIn";

    @Override
    public Boolean handleRequest(CapstoneClockInArgs input, Context context) {

        String params = "taskid=" + input.taskId + "&teamid=" + input.teamId;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        TaskedInResponse response;
        try {
            response = (TaskedInResponse) cap.CapCall(TaskedInResponse.class, context);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response.response.taskedIn;
    }
}
