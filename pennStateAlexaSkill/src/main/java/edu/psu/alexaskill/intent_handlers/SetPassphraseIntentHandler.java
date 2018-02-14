package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import edu.psu.alexaskill.PassphraseManager;

import java.util.ArrayList;
import java.util.List;

@Handler("SetupPassphrase")
public class SetPassphraseIntentHandler extends SecureIntentHandler {

    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return PassphraseManager.setupPassphrase(requestEnvelope.getRequest().getIntent(), requestEnvelope.getSession().getUser().getAccessToken());
    }

    @Override
    protected SpeechletResponse inProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return IntentHandler.getDefaultresponse();
    }
}
