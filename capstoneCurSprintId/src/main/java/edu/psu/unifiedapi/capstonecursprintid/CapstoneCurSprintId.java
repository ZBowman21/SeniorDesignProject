package edu.psu.unifiedapi.capstonecursprintid;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.capstoneutils.CapstoneCurSprintIdArgs;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;

public class CapstoneCurSprintId implements RequestHandler<CapstoneCurSprintIdArgs,String> {
    private final String path = "/AgileTask/EGetCurrentSprint";
    private final String param = "csid=4";

    @Override
    public String handleRequest(CapstoneCurSprintIdArgs input, Context context){

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, param);

        ResponseSprint response;
        try {
            response = (ResponseSprint) cap.CapCall(ResponseSprint.class, context);
            context.getLogger().log(response.response.sprint_id);

            return response.response.sprint_id;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
