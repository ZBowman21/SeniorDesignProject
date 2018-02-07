package edu.psu.unifiedapi.capstoneclockin;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneClockIn implements RequestHandler<CapstoneClockInArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskOut";

    @Override
    public Boolean handleRequest(CapstoneClockInArgs input, Context context) {

        String params = "taskid=" + input.taskId + "&teamid=" + input.teamId;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        TaskedInResponse response = (TaskedInResponse) cap.CapCall(TaskedInResponse.class, context);

        return response.response.taskedIn;
    }
}
