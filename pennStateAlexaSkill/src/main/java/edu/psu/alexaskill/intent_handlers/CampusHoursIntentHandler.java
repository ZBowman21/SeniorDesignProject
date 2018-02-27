package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import edu.psu.alexaskill.request_handlers.dining.GetHoursRequestSender;

@Handler("GetHours")
public class CampusHoursIntentHandler extends BasicIntentHandler
{
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        RequestHandler requestHandler = new GetHoursRequestSender();
        BaseResult result = requestHandler.sendRequest(requestEnvelope.getRequest().getIntent());
        return requestHandler.parseResponse(result);
    }
}
