package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.SendEmailRequest;
import edu.pennstate.api.model.SendEmailResult;

public class SendEmailRequestSender extends RequestHandler {

    @Override
    public BaseResult sendRequest(Intent requestIntent)
    {
        String destination = requestIntent.getSlot("destination").getValue();
        String subject = requestIntent.getSlot("subject").getValue();
        String body = requestIntent.getSlot("body").getValue();

        SendEmailRequest request = new SendEmailRequest();
        request.setDestination(destination);
        request.setSubject(subject);
        request.setBody(body);
        request.setUsername("bra130");
        request.setPassword("pokemon175426093");

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        SendEmailResult response = client.sendEmail(request);
        return response;

    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response)
    {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        String outputResponse;

        if(response.sdkResponseMetadata().httpStatusCode() == 200) //Email Sent
        {
            outputResponse = "Your email has been sent successfully.";
        }
        else
        {
            outputResponse = "Your email was not sent successfully. Something went wrong.";
        }

        speech.setText(outputResponse);
        return SpeechletResponse.newTellResponse(speech);
    }
}
