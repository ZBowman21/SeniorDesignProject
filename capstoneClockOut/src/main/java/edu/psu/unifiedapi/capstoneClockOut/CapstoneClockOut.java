package edu.psu.unifiedapi.capstoneClockOut;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;
import edu.psu.unifiedapi.capstoneutils.ICapstoneWrapper;

public class CapstoneClockOut implements RequestHandler<CapstoneClockOutArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskIn";

    @Override
    public Boolean handleRequest(CapstoneClockOutArgs input, Context context) {

        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.username = input.username;
        cwa.url = path;
        cwa.params = "taskid=" + input.taskId + "&teamid=" + input.teamId;
        cwa.typeClass = ResponseTaskedOut.class;
        ICapstoneWrapper cap = LambdaInvokerFactory.builder().build(ICapstoneWrapper.class);
        ResponseTaskedOut response = (ResponseTaskedOut) cap.send(cwa);

        return response.response.stopped;
    }
}
