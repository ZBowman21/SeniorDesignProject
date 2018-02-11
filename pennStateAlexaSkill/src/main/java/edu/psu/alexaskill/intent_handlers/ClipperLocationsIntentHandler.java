package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import edu.psu.alexaskill.request_handlers.dining.GetClipperLocationRequestSender;

@Handler("GetClipperLocation")
public class ClipperLocationsIntentHandler extends BasicIntentHandler
{
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        RequestHandler requestHandler = new GetClipperLocationRequestSender();
        BaseResult result = requestHandler.sendRequest(requestEnvelope.getRequest().getIntent(), requestEnvelope.getSession().getUser().getAccessToken());
        return requestHandler.parseResponse(result);
    }
}
