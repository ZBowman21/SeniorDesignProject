package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import edu.psu.alexaskill.PassphraseManager;

@Handler("RemovePassphrase")
public class DeletePassphraseIntentHandler extends SecureIntentHandler
{
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return PassphraseManager.deletePassphrase(requestEnvelope.getSession().getUser().getAccessToken());
    }

}
