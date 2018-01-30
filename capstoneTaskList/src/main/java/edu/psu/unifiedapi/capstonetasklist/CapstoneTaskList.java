package edu.psu.unifiedapi.capstonetasklist;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;
import edu.psu.unifiedapi.capstoneutils.CapstoneCurSprintIdArgs;
import edu.psu.unifiedapi.capstoneutils.ICapstoneCurSprintId;
import edu.psu.unifiedapi.capstoneutils.ICapstoneWrapper;

public class CapstoneTaskList implements RequestHandler<CapstoneTaskListArgs, String[]> {
    private final String path = "/AgileTask/EGetTaskList";
    private final String param = "?csid=4";

    @Override
    public String[] handleRequest(CapstoneTaskListArgs input, Context context) {
        CapstoneCurSprintIdArgs sa =new CapstoneCurSprintIdArgs();
        sa.username = input.unsername;
        ICapstoneCurSprintId cs = LambdaInvokerFactory.builder().build(ICapstoneCurSprintId.class);
        String sprintId = cs.getSprintId(sa);

        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.username = input.unsername;
        cwa.url = path;
        cwa.params = param + "&teamid=" + input.teamId + "&sprintid=" + sprintId;
        ICapstoneWrapper cap = LambdaInvokerFactory.builder().build(ICapstoneWrapper.class);
        ResponseTask[] response = (ResponseTask[]) cap.send(cwa);

        // Handle the response (Work with Brandon here)

        return new String[0];
    }
}
