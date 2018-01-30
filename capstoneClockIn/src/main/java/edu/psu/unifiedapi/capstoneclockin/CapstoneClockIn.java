package edu.psu.unifiedapi.capstoneclockin;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;
import edu.psu.unifiedapi.capstoneutils.ICapstoneWrapper;

public class CapstoneClockIn implements RequestHandler<CapstoneClockInArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskOut";

    @Override
    public Boolean handleRequest(CapstoneClockInArgs input, Context context) {

        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.username = input.username;
        cwa.url = path;
        cwa.params = "taskid=" + input.taskId + "&teamid=" + input.teamId;
        ICapstoneWrapper cap = LambdaInvokerFactory.builder().build(ICapstoneWrapper.class);
        ResponseTaskedIn response = (ResponseTaskedIn) cap.send(cwa);

        return response.taskedIn;
    }
}
