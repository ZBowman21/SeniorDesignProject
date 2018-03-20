package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import com.fasterxml.jackson.databind.ser.Serializers;
import edu.pennstate.api.model.CapstoneClockInRequest;
import edu.pennstate.api.model.CapstoneClockInResult;
import edu.pennstate.api.model.GetCapstoneTaskListRequest;
import edu.pennstate.api.model.GetCapstoneTaskListResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

public class CapstoneClockInRequestSender extends RequestHandler{
    public CapstoneClockInRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        return null;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        return null;
    }

    public BaseResult sendRequest(String taskID)
    {
        CapstoneClockInRequest request = new CapstoneClockInRequest();
        request.setTaskId(taskID);
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        CapstoneClockInResult response = client.capstoneClockIn(request);
        return response;
    }
}
