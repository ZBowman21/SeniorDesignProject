package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.psu.alexaskill.request_handlers.capstone.CapstoneTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Handler("CapstoneClockIn")
public class CapstoneClockInIntentHandler extends SecureLinkedAccountIntentHandler
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public SpeechletResponse IntentCompleted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
    {
        logger.info("CompleteNormal: Slot Value=" + requestEnvelope.getRequest().getIntent().getSlot("Task").getValue());
        CapstoneTaskManager taskManager = new CapstoneTaskManager(requestEnvelope.getSession());
        Intent normalIntent = requestEnvelope.getRequest().getIntent();

        DialogIntent dialogIntent = new DialogIntent();
        dialogIntent.setName(normalIntent.getName());
        dialogIntent.setConfirmationStatus(normalIntent.getConfirmationStatus());

        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        for(Map.Entry<String, Slot> entry : normalIntent.getSlots().entrySet())
        {
            Slot slot = entry.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());
            dialogSlot.setValue(slot.getValue());
            dialogSlots.put(entry.getKey(), dialogSlot);
        }

        dialogIntent.setSlots(dialogSlots);
        logger.info("Complete: Slot Value=" + dialogIntent.getSlots().get("Task").getValue());

        return taskManager.getTaskSpokenTaskAnalysis(dialogIntent);
    }

    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        CapstoneTaskManager taskManager = new CapstoneTaskManager(requestEnvelope.getSession());
        SpeechletResponse response = IntentStarted(requestEnvelope, "capstone");
        PlainTextOutputSpeech speech = (PlainTextOutputSpeech)response.getOutputSpeech();
        if(speech != null)
        {
            return response;
        }
        else
        {
            DelegateDirective directive = (DelegateDirective)response.getDirectives().get(0);
            DialogIntent intent = directive.getUpdatedIntent();
            return taskManager.getTaskSpokenTaskAnalysis(intent);
        }
    }

    @Override
    protected SpeechletResponse inProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        CapstoneTaskManager taskManager = new CapstoneTaskManager(requestEnvelope.getSession());
        Intent normalIntent = requestEnvelope.getRequest().getIntent();

        DialogIntent dialogIntent = new DialogIntent();
        dialogIntent.setName(normalIntent.getName());
        dialogIntent.setConfirmationStatus(normalIntent.getConfirmationStatus());

        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        for(Map.Entry<String, Slot> entry : normalIntent.getSlots().entrySet())
        {
            Slot slot = entry.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());
            dialogSlots.put(entry.getKey(), dialogSlot);
        }
        dialogIntent.setSlots(dialogSlots);
        logger.info("InProgress: Slot Value=" + dialogIntent.getSlots().get("Task").getValue());

        return taskManager.getTaskSpokenTaskAnalysis(dialogIntent);
    }
}
