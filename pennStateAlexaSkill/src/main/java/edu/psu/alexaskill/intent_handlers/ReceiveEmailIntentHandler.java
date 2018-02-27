package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import edu.psu.alexaskill.request_handlers.receive_email.ReceiveEmailDialogManager;

import java.util.ArrayList;
import java.util.List;

@Handler("GetMail")
@Handler("Next")
@Handler("Skip")
@Handler("Repeat")
@Handler("Read")
public class ReceiveEmailIntentHandler extends SecureLinkedAccountIntentHandler
{
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        ReceiveEmailDialogManager receiveEmailDialogManager = new ReceiveEmailDialogManager(requestEnvelope);
        return receiveEmailDialogManager.generateResponse();
    }

    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentStarted(requestEnvelope, "webmail");
    }

    @Override
    protected SpeechletResponse inProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentHandler.getDefaultresponse();
    }
}
