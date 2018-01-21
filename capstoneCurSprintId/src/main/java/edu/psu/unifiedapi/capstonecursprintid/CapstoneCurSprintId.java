package edu.psu.unifiedapi.capstonecursprintid;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;

public class CapstoneCurSprintId implements RequestHandler<CapstoneCurSprintIdArgs,String> {
    private final String path = "/AgileTask/EGetCurrentSprint";
    private final String param = "csid=4";

    private interface Capstone {
        @LambdaFunction(functionName = "capstoneWrapper")
        String send(CapstoneWrapperArgs cwa);
    }

    @Override
    public String handleRequest(CapstoneCurSprintIdArgs input, Context context) {
        CapstoneWrapperArgs cwa = new CapstoneWrapperArgs();
        cwa.url = path;
        cwa.params = param;
        cwa.username = input.username;

        Capstone cap = LambdaInvokerFactory.builder().build(Capstone.class);

        String response = cap.send(cwa);
        // Parse response here

        return response;
    }
}
