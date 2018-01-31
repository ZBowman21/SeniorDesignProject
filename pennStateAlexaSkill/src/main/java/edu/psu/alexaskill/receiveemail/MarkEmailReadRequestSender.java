package edu.psu.alexaskill.receiveemail;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.MarkEmailReadRequest;
import edu.pennstate.api.model.MarkEmailReadResult;
import edu.psu.alexaskill.RequestHandler;

public class MarkEmailReadRequestSender extends RequestHandler
{
    @Override
    public BaseResult sendRequest(Intent requestIntent, String token) {
        throw new UnsupportedOperationException();    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        throw new UnsupportedOperationException();    }

    public BaseResult sendRequest(String passphrase, String token, int emailIndex)
    {
        String username = "bra130";
        String emailIndexString = String.valueOf(emailIndex);

        MarkEmailReadRequest request = new MarkEmailReadRequest();
        request.setPassword(passphrase);
        request.setUsername(username);
        request.setStart(emailIndexString);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .customHeader("Authentication", token)
                        .build()
        );

        ///GenerateClient(token);
        MarkEmailReadResult result = client.markEmailRead(request);
        return result;
    }
}
