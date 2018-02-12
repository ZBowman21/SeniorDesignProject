package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import edu.psu.alexaskill.PassphraseManager;

@Handler("SetupPassphrase")
public class SetPassphraseIntentHandler extends SecureIntentHandler {

    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return PassphraseManager.setupPassphrase(requestEnvelope.getRequest().getIntent(), requestEnvelope.getSession().getUser().getAccessToken());
    }
}
