package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.opensdk.SdkRequestConfig;
import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;
import edu.pennstate.api.*;
import edu.pennstate.api.model.PennStateUnifiedException;
import edu.pennstate.api.model.SendEmailRequest;
import edu.pennstate.api.model.SendEmailResult;
import com.amazonaws.services.lambda.runtime.LambdaLogger;


import java.util.*;

public class PennStateSpeechlet implements SpeechletV2 {

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
       /* // any initialization logic goes here
        PennStateUnified client = PennStateUnified.builder().build();
        SendEmailRequest newRequest = new SendEmailRequest();

        newRequest.setDestination("bra130@psu.edu");
        newRequest.setBody("If this works, holy fucking shit.");
        newRequest.setSubject("Oh hot damn");
        client.sendEmail(newRequest);*/

    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {

        return getDefaultResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {

        IntentRequest request = requestEnvelope.getRequest();
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        DialogState dialogState = request.getDialogState();

        switch(intentName)
        {
            case "SendMail":
                if(dialogState.equals(DialogState.STARTED))
                {
                    //Build the DialogIntent to build a speechlet from, to generate a reprompt and to keep intent state
                    DialogIntent dialogIntent = new DialogIntent();
                    dialogIntent.setName(intentName);
                    Map<String, DialogSlot> dialogSlots = new HashMap<>();

                    Iterator iter = intent.getSlots().entrySet().iterator();
                    while(iter.hasNext())
                    {
                        Map.Entry pair = (Map.Entry)iter.next();
                        DialogSlot dialogSlot = new DialogSlot();
                        Slot slot = (Slot) pair.getValue();
                        dialogSlot.setName(slot.getName());
                        dialogSlot.setValue(slot.getValue());
                        dialogSlots.put((String)pair.getKey(), dialogSlot);
                    }

                    dialogIntent.setSlots(dialogSlots);

                    DelegateDirective dd = new DelegateDirective();
                    dd.setUpdatedIntent(dialogIntent);

                    List<Directive> directiveList = new ArrayList<>();
                    directiveList.add(dd);

                    SpeechletResponse speechletResponse = new SpeechletResponse();
                    speechletResponse.setDirectives(directiveList);
                    speechletResponse.setNullableShouldEndSession(false);
                    return speechletResponse;
                }
                else if(dialogState.equals(DialogState.COMPLETED))
                {
                    String subject = intent.getSlot("subject").getValue();
                    String body = intent.getSlot("body").getValue();
                    String speechText = "I will send your message";
                    PlainTextOutputSpeech speech =  new PlainTextOutputSpeech();
                    speech.setText(speechText);

                    PennStateUnified client = PennStateUnified.builder().connectionConfiguration(new ConnectionConfiguration()
                            .maxConnections(100)
                            .connectionMaxIdleMillis(10000))
                            .timeoutConfiguration(new TimeoutConfiguration()
                                    .httpRequestTimeout(12000)
                                    .totalExecutionTimeout(12000)
                                    .socketTimeout(12000)).build();

                    SendEmailRequest newRequest = new SendEmailRequest();
                    newRequest.setDestination("bra130");
                    newRequest.setBody(body);
                    newRequest.setSubject(subject);
                    newRequest.setUsername("bra130");

                    newRequest.sdkRequestConfig(
                        SdkRequestConfig.builder()
                                .httpRequestTimeout(5000)
                                .totalExecutionTimeout(10000)
                                .build()
                    );

                    String error;

                    try
                    {
                        client.sendEmail(newRequest);
                    }
                    catch(PennStateUnifiedException e)
                    {
                        error = e.getMessage();

                    }
                    return SpeechletResponse.newTellResponse(speech);
                }
                else
                {
                    DelegateDirective dd = new DelegateDirective();
                    List<Directive> directiveList = new ArrayList<>();
                    directiveList.add(dd);

                    SpeechletResponse speechletResponse = new SpeechletResponse();
                    speechletResponse.setDirectives(directiveList);
                    speechletResponse.setNullableShouldEndSession(false);
                    return speechletResponse;
                }
        }
        return getDefaultResponse();
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    }

    private SpeechletResponse getDefaultResponse() {
        //need to change this to story intro
        String speechText = "Welcome to the Penn State Unified System. I did not understand your request.";

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

    private SpeechletResponse getTestResponse(String value)
    {
        String response = "I found this as the destination: " + value;
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(response);
        return SpeechletResponse.newTellResponse(speech);

    }
}