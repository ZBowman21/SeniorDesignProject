package edu.psu.unifiedapi.capstonecursprintid;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneCurSprintIdArgs;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;
import edu.psu.unifiedapi.capstoneutils.ICapstoneWrapper;

public class CapstoneCurSprintId implements RequestHandler<CapstoneCurSprintIdArgs,String> {
    private final String path = "/AgileTask/EGetCurrentSprint";
    private final String param = "csid=4";

    @Override
    public String handleRequest(CapstoneCurSprintIdArgs input, Context context) {
        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.url = path;
        cwa.params = param;
        cwa.username = input.username;
        cwa.typeClass = ResponseSprint.class;

        ICapstoneWrapper cap = LambdaInvokerFactory.builder().build(ICapstoneWrapper.class);

        ResponseSprint response = (ResponseSprint) cap.send(cwa);

        return response.response.sprint_id;
    }
}
