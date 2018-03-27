package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.psu.alexaskill.intent_handlers.Handler;
import edu.psu.alexaskill.intent_handlers.Handlers;
import edu.psu.alexaskill.intent_handlers.IntentHandler;
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
                    logger.info("Annoation Name: {}, Class name: {}", annotation.value(), instance.getClass().getName());
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

        logger.info(intentName);
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

        return getDefaultResponse();
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope)
    {

    }

    private SpeechletResponse getDefaultResponse()
    {
        String speechText = "Welcome to the Penn State Alexa Application. You can do things like send and receive email, clock into senior design, and more.";
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }
}