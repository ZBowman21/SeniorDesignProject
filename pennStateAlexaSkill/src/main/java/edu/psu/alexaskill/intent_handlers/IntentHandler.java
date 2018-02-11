package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

public interface IntentHandler
{
    SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
    SpeechletResponse IntentInProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
    SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope);
}
