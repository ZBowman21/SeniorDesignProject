package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.*;
import edu.psu.alexaskill.request_handlers.RequestHandler;

import java.util.List;

public class CapstoneTaskListRequestSender extends RequestHandler {

    public CapstoneTaskListRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        int statusCode = 200;

        GetCapstoneTaskListRequest request = new GetCapstoneTaskListRequest();
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );
        GetCapstoneTaskListResult response = new GetCapstoneTaskListResult();
        try
        {
            response = client.getCapstoneTaskList(request);
        }
        catch(PennStateUnifiedException e)
        {
            statusCode = e.sdkHttpMetadata().httpStatusCode();
        }

        if(statusCode == 200)
        {
            return response;
        }
        else
        {
            return null;
        }
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String outputResponse;

        GetCapstoneTaskListResult result = (GetCapstoneTaskListResult)response;

        List<String> tasks = result.getCapstoneTaskList();
        if(tasks.isEmpty())
        {
            outputResponse = "I could not find any tasks for your team in the Capstone system.";
        }
        else
        {
            outputResponse = "Your team has the following tasks in Capstone: ";
            for(int i = 0; i < tasks.size(); i++)
            {
                outputResponse += tasks.get(i) + ". ";
            }
        }

        card.setContent(outputResponse);
        speech.setText(outputResponse);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);

        return speechletResponse;
    }
}