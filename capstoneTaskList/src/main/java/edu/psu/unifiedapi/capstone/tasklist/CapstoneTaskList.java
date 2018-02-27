package edu.psu.unifiedapi.capstone.tasklist;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.utils.CapstoneCurSprintIdArgs;
import edu.psu.unifiedapi.capstone.utils.ICapstoneCurSprintId;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneTaskList implements RequestHandler<CapstoneTaskListArgs, String[]> {
    private final String path = "/AgileTask/EGetTaskList";
    private final String param = "csid=4";

    @Override
    public String[] handleRequest(CapstoneTaskListArgs input, Context context) {
        CapstoneCurSprintIdArgs sa =new CapstoneCurSprintIdArgs();
        sa.username = input.username;
        ICapstoneCurSprintId cs = LambdaInvokerFactory.builder().build(ICapstoneCurSprintId.class);
        String sprintId = cs.getSprintId(sa);

        String params = param + "&teamid=" + input.teamId + "&sprintid=" + sprintId;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        ResponseTask response;
        try {
            response = (ResponseTask) cap.CapCall(ResponseTask.class, context);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Handle the response (Work with Brandon here)
        String[] temp = new String[response.response.length];
        for (int i = 0; i < response.response.length; i++) {
            temp[i] = response.response[i].taskid + " : " + response.response[i].task_desp;
        }
        return temp;
    }
}
