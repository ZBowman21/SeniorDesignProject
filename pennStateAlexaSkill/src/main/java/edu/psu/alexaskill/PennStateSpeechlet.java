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
import edu.psu.alexaskill.request_handlers.receive_email.ReceiveEmailDialogManager;
import edu.psu.alexaskill.request_handlers.dining.GetClipperLocationRequestSender;
import edu.psu.alexaskill.request_handlers.dining.GetHoursRequestSender;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import edu.psu.alexaskill.request_handlers.send_email.SendEmailRequestSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PennStateSpeechlet implements SpeechletV2 {

    private RequestHandler requestHandler;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        logger.info("Session Started");
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
        //Check session attribute of state, if in receive email, go to or call separate method  or look through separate switch. If no state or default state, continue because we are in the initial state and could be sending emails, getting grades, etc.
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
                    BaseResult result = requestHandler.sendRequest(intent, requestEnvelope.getSession().getUser().getAccessToken());
                    return requestHandler.parseResponse(result);
                }
                else
                {
                    return GenerateMultiDialogInProgressResponse();
                }
            case "GetMail" :
                if(dialogState.equals(DialogState.STARTED))
                {
                    return GenerateMultiDialogStartedResponse(intentName, intent);
                }
                else if(dialogState.equals(DialogState.COMPLETED)) //passphrase received. will always go here for reading back emails
                {
                    ReceiveEmailDialogManager receiveEmailDialogManager = new ReceiveEmailDialogManager(requestEnvelope);
                    return receiveEmailDialogManager.generateResponse();
                }
                else
                {
                    return GenerateMultiDialogInProgressResponse();
                }
            case "GetHours":
                if(dialogState.equals(DialogState.STARTED))
                {
                    return GenerateMultiDialogStartedResponse(intentName, intent);
                }
                else if(dialogState.equals(DialogState.COMPLETED))
                {
                    requestHandler = new GetHoursRequestSender();
                    BaseResult result = requestHandler.sendRequest(intent, requestEnvelope.getSession().getUser().getAccessToken());
                    return requestHandler.parseResponse(result);
                }
                else
                {
                    return GenerateMultiDialogInProgressResponse();
                }
            case "GetClipperLocation":
                if(dialogState.equals(DialogState.STARTED))
                {
                    return GenerateMultiDialogStartedResponse(intentName, intent);
                }
                else if(dialogState.equals(DialogState.COMPLETED))
                {
                    requestHandler = new GetClipperLocationRequestSender();
                    BaseResult result = requestHandler.sendRequest(intent, requestEnvelope.getSession().getUser().getAccessToken());
                    return requestHandler.parseResponse(result);
                }
                else
                {
                    return GenerateMultiDialogInProgressResponse();
                }
        }

        //Used to detect in-progress dialog for receiving email. Primarily used to detect mid-dialog feature specific intents such as repeat and skip
        if(requestEnvelope.getSession().getAttribute("state") != null && (intentName.equals("Repeat") || intentName.equals("Skip")
        || intentName.equals("Read") || intentName.equals("Next")))
        {
            ReceiveEmailDialogManager receiveEmailDialogManager = new ReceiveEmailDialogManager(requestEnvelope);
            return receiveEmailDialogManager.generateResponse();
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

}