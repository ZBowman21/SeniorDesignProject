package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;

import java.util.ArrayList;
import java.util.List;

public interface IntentHandler
{
    SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
    SpeechletResponse IntentInProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
    SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);

    static SpeechletResponse getDefaultresponse() {
        DelegateDirective dd = new DelegateDirective();
        List<Directive> directiveList = new ArrayList<>();
        directiveList.add(dd);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setDirectives(directiveList);
        speechletResponse.setNullableShouldEndSession(false);
        return speechletResponse;
    }

}
