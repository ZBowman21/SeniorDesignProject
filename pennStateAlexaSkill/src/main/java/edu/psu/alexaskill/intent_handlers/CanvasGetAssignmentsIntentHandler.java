package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.request_handlers.canvas.CanvasGetAssignmentsRequestSender;

@Handler("GetAssignments")
public class CanvasGetAssignmentsIntentHandler extends SecureLinkedAccountIntentHandler {
    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentStarted(requestEnvelope, "canvas");
    }

    @Override
    protected SpeechletResponse inProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentHandler.getDefaultresponse();
    }

    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        CanvasGetAssignmentsRequestSender requestSender = new CanvasGetAssignmentsRequestSender(requestEnvelope.getSession().getUser().getAccessToken());
        BaseResult result = requestSender.sendRequest(requestEnvelope.getRequest().getIntent());
        return requestSender.parseResponse(result);
    }
}
