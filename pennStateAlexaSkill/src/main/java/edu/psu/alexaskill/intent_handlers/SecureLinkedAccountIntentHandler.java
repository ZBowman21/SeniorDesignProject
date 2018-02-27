package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.psu.alexaskill.request_handlers.check_accounts.LinkAccountsRequestSender;

public abstract class SecureLinkedAccountIntentHandler extends SecureIntentHandler {

    public final SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope, String service) {
        LinkAccountsRequestSender linkAccountsRequestSender = new LinkAccountsRequestSender(requestEnvelope.getSession().getUser().getAccessToken());
        logger.info("Check if account is linked to {}", service);
        if(!linkAccountsRequestSender.sendRequest(service))
        {
            SpeechletResponse response = new SpeechletResponse();
            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText("You have not linked your " + service + " account. Please visit pennhub.bd.psu.edu to do so.");
            response.setOutputSpeech(speech);
            return response;
        }
        else
        {
            return super.IntentStarted(requestEnvelope);
        }
    }
}
