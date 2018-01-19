package edu.psu.unifiedapi.capstonetasklist;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstone.CapstoneWrapperArgs;
import edu.psu.unifiedapi.capstonecursprintid.CapstoneCurSprintIdArgs;

public class CapstoneTaskList implements RequestHandler<CapstoneTaskListArgs, String[]> {
    private final String baseUrl = "/AgileTask/EGetTaskList";
    private String param = "?csid=4";

    private interface Capstone {
        @LambdaFunction(functionName = "capstoneWrapper")
        String send(CapstoneWrapperArgs cwa);
    }
    private interface CurSprint {
        @LambdaFunction(functionName = "capstoneCurSprintId")
        String SprintId(CapstoneCurSprintIdArgs sa);
    }

    @Override
    public String[] handleRequest(CapstoneTaskListArgs input, Context context) {
        CapstoneCurSprintIdArgs sa =new CapstoneCurSprintIdArgs();
        sa.username = input.unsername;
        CurSprint cs = LambdaInvokerFactory.builder().build(CurSprint.class);
        String sprintId = cs.SprintId(sa);

        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.username = input.unsername;
        cwa.url = baseUrl;
        cwa.params = param + "&teamid=" + input.teamId + "&sprintid=" + sprintId;
        Capstone cap = LambdaInvokerFactory.builder().build(Capstone.class);
        String response = cap.send(cwa);

        // Handle the response

        return new String[0];
    }
}
