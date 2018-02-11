package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import edu.psu.alexaskill.request_handlers.receive_email.ReceiveEmailDialogManager;

@Handler("GetMail")
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
}
