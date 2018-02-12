package edu.psu.alexaskill.request_handlers.receive_email;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.MarkEmailReadRequest;
import edu.pennstate.api.model.MarkEmailReadResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

public class MarkEmailReadRequestSender extends RequestHandler
{

    public MarkEmailReadRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        throw new UnsupportedOperationException();    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        throw new UnsupportedOperationException();    }

    public BaseResult sendRequest(int emailIndex)
    {
        String emailIndexString = String.valueOf(emailIndex);

        MarkEmailReadRequest request = new MarkEmailReadRequest();
        request.setStart(emailIndexString);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        MarkEmailReadResult result = client.markEmailRead(request);
        return result;
    }
}
