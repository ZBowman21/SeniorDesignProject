package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.opensdk.BaseResult;

import java.util.*;

public class PennStateSpeechlet implements SpeechletV2 {

    private RequestHandler requestHandler;
    private EmailCorrector emailCorrector = new EmailCorrector();

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {

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
                    return GenerateMultiDialogStartedResponse(intentName, intent);
                }
                else if(dialogState.equals(DialogState.COMPLETED))
                {
                    requestHandler = new SendEmailRequestSender();
                    BaseResult result = requestHandler.sendRequest(intent);
                    return requestHandler.parseResponse(result);
                }
                else
                {
                    return GenerateMultiDialogInProgressResponse();
                }
        }
        return getDefaultResponse();
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    }

    private SpeechletResponse getDefaultResponse() {
        String speechText = "Welcome to the Penn State Unified System. I did not understand your request.";
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse GenerateMultiDialogStartedResponse(String intentName, Intent intent)
    {
        //Build the DialogIntent to build a speechlet from, to generate a reprompt and to keep intent state
        DialogIntent dialogIntent = new DialogIntent();
        dialogIntent.setName(intentName);
        Map<String, DialogSlot> dialogSlots = new HashMap<>();

        Iterator iter = intent.getSlots().entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry pair = (Map.Entry)iter.next();
            Slot slot = (Slot) pair.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            if(intentName.equals("SendMail"))
            {
                dialogSlot = CreateSendEmailDialogSlot(slot);
            }
            else
            {
                dialogSlot.setName(slot.getName());
                dialogSlot.setValue(slot.getValue());
            }
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

    private SpeechletResponse GenerateMultiDialogInProgressResponse()
    {
        DelegateDirective dd = new DelegateDirective();
        List<Directive> directiveList = new ArrayList<>();
        directiveList.add(dd);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setDirectives(directiveList);
        speechletResponse.setNullableShouldEndSession(false);
        return speechletResponse;
    }

    private DialogSlot CreateSendEmailDialogSlot(Slot slot)
    {
        DialogSlot dialogSlot = new DialogSlot();

        String slotName = slot.getName();
        dialogSlot.setName(slotName);

        if(slotName.equals("destination") && slot.getValue() != null && !emailCorrector.AddressCorrected)
        {
            dialogSlot.setValue(emailCorrector.CorrectAddress(slot.getValue()));
        }
        else if(slotName.equals("subject") && slot.getValue() != null && !emailCorrector.SubjectCorrected)
        {
            dialogSlot.setValue(emailCorrector.CorrectSubject(slot.getValue()));
        }
        else if(slotName.equals("body") && slot.getValue() != null && !emailCorrector.BodyCorrected)
        {
            dialogSlot.setValue(emailCorrector.CorrectBody(slot.getValue()));
        }
        else
        {
            dialogSlot.setValue(slot.getValue());
        }

        return dialogSlot;
    }
}