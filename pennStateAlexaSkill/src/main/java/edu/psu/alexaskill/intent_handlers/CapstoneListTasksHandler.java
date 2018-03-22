package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import edu.psu.alexaskill.request_handlers.capstone.CapstoneTaskManager;

@Handler("CapstoneListTasks")
public class CapstoneListTasksHandler extends SecureLinkedAccountIntentHandler {
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        CapstoneTaskManager capstoneTaskManager = new CapstoneTaskManager(requestEnvelope.getSession());
        return capstoneTaskManager.getFormattedTaskListResponse();
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
