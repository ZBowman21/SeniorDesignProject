package edu.psu.alexaskill.receiveemail;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.ReceiveEmailsRequest;
import edu.pennstate.api.model.ReceiveEmailsResult;
import edu.psu.alexaskill.RequestHandler;

public class ReceiveEmailRequestSender extends RequestHandler {

    @Override
    public BaseResult sendRequest(Intent requestIntent, String token)
    {
        throw new UnsupportedOperationException();
    }

    public BaseResult sendRequest(String passphrase, String token, int emailIndex)
    {
        String emailIndexString = String.valueOf(emailIndex);

        ReceiveEmailsRequest request = new ReceiveEmailsRequest();
        request.setPassword(passphrase);
        request.setStart(emailIndexString);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        GenerateClient(token);
        ReceiveEmailsResult result = client.receiveEmails(request);
        return result;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response)
    {
        throw new UnsupportedOperationException();
    }
}
