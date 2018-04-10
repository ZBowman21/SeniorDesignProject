package edu.psu.alexaskill.request_handlers.canvas;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.CanvasGetAssignmentsRequest;
import edu.pennstate.api.model.CanvasGetAssignmentsResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

import java.util.List;

public class CanvasGetAssignmentsRequestSender extends RequestHandler {
    public CanvasGetAssignmentsRequestSender(String token){super(token);}

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        CanvasGetAssignmentsRequest request = new CanvasGetAssignmentsRequest();
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        return client.canvasGetAssignments(request);
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        CanvasGetAssignmentsResult result = (CanvasGetAssignmentsResult)response;
        List<String> assignments = result.getStringArray();

        SpeechletResponse speechletResponse = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String text = "Here are your current top assignments in Canvas: ";
        for(int i = 0; i < assignments.size(); i++)
        {
            text += assignments.get(i) + ". ";
        }

        card.setContent(text);
        speech.setText(text);

        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);
        return speechletResponse;
    }
}
