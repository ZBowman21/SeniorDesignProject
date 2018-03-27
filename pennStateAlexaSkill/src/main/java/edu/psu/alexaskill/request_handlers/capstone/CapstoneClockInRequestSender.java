package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import com.fasterxml.jackson.databind.ser.Serializers;
import edu.pennstate.api.model.*;
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

    public boolean sendRequest(String taskID)
    {
        int statusCode = 200;

        CapstoneClockInRequest request = new CapstoneClockInRequest();
        request.setTaskId(taskID);
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        try
        {
            client.capstoneClockIn(request);
        }
        catch(PennStateUnifiedException e)
        {
            statusCode = e.sdkHttpMetadata().httpStatusCode();
        }

        if(statusCode == 200)
            return true;
        else
            return false;
    }
}
