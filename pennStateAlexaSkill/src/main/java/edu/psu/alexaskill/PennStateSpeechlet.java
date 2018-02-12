package edu.psu.alexaskill;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.dialog.directives.ConfirmSlotDirective;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.intent_handlers.Handler;
import edu.psu.alexaskill.intent_handlers.Handlers;
import edu.psu.alexaskill.intent_handlers.IntentHandler;
import edu.psu.alexaskill.passphrase.PassphraseManager;
import edu.psu.alexaskill.request_handlers.check_accounts.LinkAccountsRequestSender;
import edu.psu.alexaskill.request_handlers.receive_email.ReceiveEmailDialogManager;
import edu.psu.alexaskill.request_handlers.dining.GetClipperLocationRequestSender;
import edu.psu.alexaskill.request_handlers.dining.GetHoursRequestSender;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import edu.psu.alexaskill.request_handlers.send_email.SendEmailRequestSender;
import edu.psu.unifiedapi.account.CognitoUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PennStateSpeechlet implements SpeechletV2 {

    private RequestHandler requestHandler;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, IntentHandler> intentHandlers = new HashMap<>();

    public PennStateSpeechlet()
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage("edu.psu.alexaskill.intent_handlers"))
        .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

		Set<Class<?>> intentHandlerConstructors = reflections.getTypesAnnotatedWith(Handler.class);

		intentHandlerConstructors.addAll(reflections.getTypesAnnotatedWith(Handlers.class));

        for(Class<?> c: intentHandlerConstructors)
        {
            try
            {
                IntentHandler instance = (IntentHandler)c.newInstance();
                for (Handler annotation : c.getAnnotationsByType(Handler.class)) {
                    intentHandlers.put(annotation.value(), instance);
                }
            }
            catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        logger.info("Session Started: Checking for passphrase");
        logger.info("Current passphrase: {}" , CognitoUtils.getPassphrase(requestEnvelope.getSession().getUser().getAccessToken()));

        requestEnvelope.getSession().setAttribute("hasPassphrase",
                CognitoUtils.hasPassphrase(requestEnvelope.getSession().getUser().getAccessToken()));
        requestEnvelope.getSession().setAttribute("passphraseChecked", false);
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

        IntentHandler intentHandler = intentHandlers.get(intentName);
        switch(dialogState)
        {
            case STARTED:
                return intentHandler.IntentStarted(requestEnvelope);
            case IN_PROGRESS:
                return intentHandler.IntentInProgress(requestEnvelope);
            case COMPLETED:
                return intentHandler.IntentCompleted(requestEnvelope);
        }


        logger.info("Intent: {} State: {}", intentName, dialogState);
        switch(intentName)
        {
            case "GetMail" :
                if(dialogState.equals(DialogState.STARTED))
                {
                    return GenerateLinkedAccountCheckSpeechletResponse(intent, "webmail", requestEnvelope.getSession().getUser().getAccessToken(),
                            requestEnvelope.getSession());
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

    private SpeechletResponse GenerateMultiDialogStartedResponse(Intent intent, Session session)
    {
        //Build the DialogIntent to build a speechlet from, to generate a reprompt and to keep intent state
        DialogIntent dialogIntent = new DialogIntent();
        SpeechletResponse speechletResponse = new SpeechletResponse();

        boolean invalidPassphrase = false;

        dialogIntent.setName(intent.getName());
        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        Iterator iter = intent.getSlots().entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry pair = (Map.Entry)iter.next();
            Slot slot = (Slot) pair.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());

            logger.info("{}bb{}bb", slot.getName(), slot.getValue());

            //Check if passphrase was correct
            if(slot.getName().equalsIgnoreCase("passphrase") && !(boolean)session.getAttribute("hasPassphrase"))
            {
                logger.info("Has Passphrase? {}", session.getAttribute("hasPassphrase"));
                dialogSlot.setValue("fdsafewaopjfp");
                dialogSlot.setConfirmationStatus(ConfirmationStatus.CONFIRMED);

            }
            else if(slot.getName().equalsIgnoreCase("passphrase")  && slot.getValue() != null && (boolean)session.getAttribute("hasPassphrase") &&
                    !(boolean)session.getAttribute("passphraseChecked"))
            {
                logger.info("Checking for invalid passphrase");
                //Check if passphrase is correct
                PassphraseManager passphraseManager = new PassphraseManager();
                boolean passphraseMatch = passphraseManager.checkPassphrase(slot.getValue(), session.getUser().getAccessToken());
                if(!passphraseMatch)
                {
                    logger.info("invalid passphrase");
                    dialogSlot.setConfirmationStatus(ConfirmationStatus.DENIED);
                    invalidPassphrase = true;
                }
                session.setAttribute("passphraseChecked", true);
            }
            else
            {
                dialogSlot.setValue(slot.getValue());
            }

            dialogSlots.put((String)pair.getKey(), dialogSlot);
        }

        dialogIntent.setSlots(dialogSlots);
        List<Directive> directiveList = new ArrayList<>();

        if(invalidPassphrase)
        {
            ConfirmSlotDirective csd = new ConfirmSlotDirective();
            csd.setUpdatedIntent(dialogIntent);
            csd.setSlotToConfirm("passphrase");

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            String output = "Incorrect passphrase. Please try again.";
            speech.setText(output);
            speechletResponse.setOutputSpeech(speech);

            directiveList.add(csd);
        }
        else
        {
            DelegateDirective dd = new DelegateDirective();
            dd.setUpdatedIntent(dialogIntent);
            directiveList.add(dd);
        }

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

    private SpeechletResponse GenerateLinkedAccountCheckSpeechletResponse(Intent intent, String service, String token, Session session)
    {
        LinkAccountsRequestSender linkAccountsRequestSender = new LinkAccountsRequestSender();
        boolean accountLinked = linkAccountsRequestSender.sendRequest(token, service);

        if(!accountLinked)
        {
            SpeechletResponse response = new SpeechletResponse();
            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText("You have not linked your " + service + " account. Please visit dokistone.bd.psu.edu to do so.");
            response.setOutputSpeech(speech);
            return response;
        }
        else
        {
            return GenerateMultiDialogStartedResponse(intent, session);
        }
    }
}