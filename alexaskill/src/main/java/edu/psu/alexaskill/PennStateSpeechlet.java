package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.OutputSpeech;
import edu.pennstate.api.*;
import edu.pennstate.api.model.SendEmailRequest;

public class PennStateSpeechlet implements SpeechletV2 {

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        // any initialization logic goes here
        PennStateUnified client = PennStateUnified.builder().build();
        SendEmailRequest newRequest = new SendEmailRequest();

        newRequest.setDestination("bra130@psu.edu");
        newRequest.setBody("If this works, holy fucking shit.");
        newRequest.setSubject("Oh hot damn");
        client.sendEmail(newRequest);

    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        Intent intent = requestEnvelope.getRequest().getIntent();
        String intentName = intent.getName();

        if(requestEnvelope.getRequest().getDialogState().equals(IntentRequest.DialogState.IN_PROGRESS))
        {

        }


        switch(intentName)
        {
            case "SendMail":
                //if( IntentRequest.DialogState == "STARTED")
                //{

                //}


        }
        return getWelcomeResponse();
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    }

    private SpeechletResponse getWelcomeResponse() {
        //need to change this to story intro
        String speechText = "Welcome to the Penn State Unified System.";

        // Create the Simple card content.
        //SimpleCard card = new SimpleCard();
        //card.setTitle("Hello Penn State student!");
        //card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        //Reprompt reprompt = new Reprompt();
        //reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newTellResponse(speech);
    }
}