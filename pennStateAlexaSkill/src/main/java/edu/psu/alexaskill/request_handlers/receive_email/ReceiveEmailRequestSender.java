package edu.psu.alexaskill.request_handlers.receive_email;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.ReceiveEmailsRequest;
import edu.pennstate.api.model.ReceiveEmailsResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

public class ReceiveEmailRequestSender extends RequestHandler {

    public ReceiveEmailRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent)
    {
        throw new UnsupportedOperationException();
    }

    public BaseResult sendRequest(int emailIndex)
    {
        String emailIndexString = String.valueOf(emailIndex);

        ReceiveEmailsRequest request = new ReceiveEmailsRequest();
        request.setStart(emailIndexString);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        ReceiveEmailsResult result = client.receiveEmails(request);
        return result;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response)
    {
        throw new UnsupportedOperationException();
    }
}
