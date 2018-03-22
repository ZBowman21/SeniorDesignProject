package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

@Handler("AMAZON.StopIntent")
public class StopIntentHandler extends BasicIntentHandler {
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        return new SpeechletResponse();
    }
}
