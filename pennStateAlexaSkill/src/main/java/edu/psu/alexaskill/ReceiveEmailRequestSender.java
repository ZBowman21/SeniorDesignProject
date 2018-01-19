package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.*;

public class ReceiveEmailRequestSender extends RequestHandler {

    @Override
    public BaseResult sendRequest(Intent requestIntent, String token)
    {
        String passphrase = requestIntent.getSlot("passphrase").getValue();
        String username = "bra130";

        ReceiveEmailsRequest request = new ReceiveEmailsRequest();
        request.setPassphrase(passphrase);
        request.setUsername(username);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .customHeader("Authorization", token)
                        .build()
        );

        ReceiveEmailsResult result = client.receiveEmails(request);
        return result;
    }

    public BaseResult sendRequest(Intent requestIntent, String token, int emailIndex)
    {
        String passphrase = requestIntent.getSlot("passphrase").getValue();
        String username = "bra130";
        String emailIndexString = String.valueOf(emailIndex);

        ReceiveEmailsRequest request = new ReceiveEmailsRequest();
        request.setPassphrase(passphrase);
        request.setUsername(username);
        request.setStartingemailindex(emailIndexString);

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .customHeader("Authorization", token)
                        .build()
        );

        ReceiveEmailsResult result = client.receiveEmails(request);
        return result;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response)
    {




    }
}