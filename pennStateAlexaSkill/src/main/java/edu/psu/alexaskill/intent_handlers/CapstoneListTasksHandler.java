package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.request_handlers.capstone.CapstoneTaskListRequestSender;

@Handler("CapstoneListTasks")
public class CapstoneListTasksHandler extends SecureLinkedAccountIntentHandler {
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        CapstoneTaskListRequestSender requestSender = new CapstoneTaskListRequestSender(requestEnvelope.getSession().getUser().getAccessToken());
        BaseResult result = requestSender.sendRequest(null);
        return requestSender.parseResponse(result);
    }

    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentStarted(requestEnvelope, "capstone");
    }

    @Override
    protected SpeechletResponse inProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentHandler.getDefaultresponse();
    }
}
