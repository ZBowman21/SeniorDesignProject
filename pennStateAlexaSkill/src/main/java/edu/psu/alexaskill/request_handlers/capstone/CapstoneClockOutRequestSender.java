package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.CapstoneClockOutRequest;
import edu.pennstate.api.model.CapstoneClockOutResult;
import edu.pennstate.api.model.GetCapstoneTaskListResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

import java.util.List;

public class CapstoneClockOutRequestSender extends RequestHandler {

    public CapstoneClockOutRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        CapstoneClockOutRequest request = new CapstoneClockOutRequest();
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        CapstoneClockOutResult response = client.capstoneClockOut(request);
        return response;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();
        String outputResponse = "You have been clocked out of your task.";


        card.setContent(outputResponse);
        speech.setText(outputResponse);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);

        return speechletResponse;
    }
}
