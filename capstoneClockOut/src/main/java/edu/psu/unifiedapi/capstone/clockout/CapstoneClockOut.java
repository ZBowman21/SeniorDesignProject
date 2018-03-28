package edu.psu.unifiedapi.capstone.clockout;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.account.GetCapstoneDataArgs;
import edu.psu.unifiedapi.account.IGetCapstoneData;
import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;
import edu.psu.unifiedapi.capstone.curclockin.CapstoneCurClockInArgs;
import edu.psu.unifiedapi.capstone.curclockin.ICapstoneCurClockIn;

public class CapstoneClockOut implements RequestHandler<CapstoneClockOutArgs,Boolean>{
    private final String path = "/AgileTask/EStudentTaskOut";

    @Override
    public Boolean handleRequest(CapstoneClockOutArgs input, Context context) {

        // Need grab teamid from database
        //String teamid = "CSSE-BD-Class2018-002";
        GetCapstoneDataArgs gcda = new GetCapstoneDataArgs();
        gcda.userId = input.username;

        IGetCapstoneData gcd = LambdaInvokerFactory.builder().build(IGetCapstoneData.class);
        String teamid = gcd.getCapstoneData(gcda);

        // Need to grab taskid from lambda....
        CapstoneCurClockInArgs ccia = new CapstoneCurClockInArgs();
        ccia.username = input.username;

        ICapstoneCurClockIn cci =  LambdaInvokerFactory.builder().build(ICapstoneCurClockIn.class);
        int taskid = cci.getCurClockIn(ccia);

        String params = "taskid=" + taskid + "&teamid=" + teamid;

        CapstoneWrapper cap = new CapstoneWrapper(input.username, path, params);
        ResponseTaskedOut response;
        try {
            response = cap.capCall(ResponseTaskedOut.class);
        } catch (CapstoneException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response.response.stopped;
    }
}
