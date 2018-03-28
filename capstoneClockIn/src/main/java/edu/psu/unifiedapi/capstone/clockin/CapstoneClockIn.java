package edu.psu.unifiedapi.capstone.clockin;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.account.IGetCapstoneData;
import edu.psu.unifiedapi.account.GetCapstoneDataArgs;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;
import edu.psu.unifiedapi.capstone.utils.CapstoneDbUtils;

public class CapstoneClockIn implements RequestHandler<CapstoneClockInArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskIn";

    @Override
    public Boolean handleRequest(CapstoneClockInArgs input, Context context) {

        // Need to get teamid from db
        GetCapstoneDataArgs gcda = new GetCapstoneDataArgs();
        gcda.userId = input.username;

        IGetCapstoneData gcd = LambdaInvokerFactory.builder().build(IGetCapstoneData.class);
        String teamid = gcd.getCapstoneData(gcda);
        //String teamid = "CSSE-BD-Class2018-002";

        String params = "taskid=" + input.taskId + "&teamid=" + teamid;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        TaskedInResponse response;
        try {
            response = cap.capCall(TaskedInResponse.class);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response.response.taskedIn;
    }
}
