package edu.psu.alexaskill.request_handlers.canvas;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.CanvasGetGradesRequest;
import edu.pennstate.api.model.CanvasGetGradesResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

import java.util.List;

public class CanvasGetGradesRequestSender extends RequestHandler{
    public CanvasGetGradesRequestSender(String token){
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        CanvasGetGradesRequest request = new CanvasGetGradesRequest();
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        return client.canvasGetGrades(request);
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        CanvasGetGradesResult result = (CanvasGetGradesResult)response;
        List<String> grades = result.getStringArray();

        SpeechletResponse speechletResponse = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String text = "Here are your current grades from Canvas: ";
        for(int i = 0; i < grades.size(); i++)
        {
            text += grades.get(i) + ". ";
        }

        card.setContent(text);
        speech.setText(text);

        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);
        return speechletResponse;
    }
}
