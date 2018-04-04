package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

@Handler("AMAZON.HelpIntent")
public class HelpIntentHandler extends BasicIntentHandler {
    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();

        SimpleCard card = new SimpleCard();
        card.setContent("Example commands:\nAsk Penn State to clock into Capstone\nAsk Penn State to send an email\nAsk Penn State to read my email\n Ask Penn State when Brunos is open\n Ask Penn State to set up a new passphrase");

        speech.setText("Welcome to the Penn State Alexa Skill. With this skill, you can check your email, setup a passphrase, get hours for campus services, and clock into and out of Capstone tasks. You can also get your grades and assignments for Canvas.");
        response.setOutputSpeech(speech);
        response.setCard(card);

        return response;
    }
}
