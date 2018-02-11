package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class BasicIntentHandler implements IntentHandler {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Intent intent = requestEnvelope.getRequest().getIntent();
        DialogIntent dialogIntent = new DialogIntent();
        SpeechletResponse speechletResponse = new SpeechletResponse();

        dialogIntent.setName(intent.getName());
        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        for(Map.Entry<String, Slot> entry : intent.getSlots().entrySet())
        {
            Slot slot = entry.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());
            dialogSlot.setValue(slot.getValue());

            dialogSlots.put(entry.getKey(), dialogSlot);
            logger.info("Key {}", entry.getKey());
        }

        dialogIntent.setSlots(dialogSlots);
        List<Directive> directiveList = new ArrayList<>();

        DelegateDirective dd = new DelegateDirective();
        dd.setUpdatedIntent(dialogIntent);
        directiveList.add(dd);

        speechletResponse.setDirectives(directiveList);
        speechletResponse.setNullableShouldEndSession(false);
        return speechletResponse;
    }

    @Override
    public SpeechletResponse IntentInProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        DelegateDirective dd = new DelegateDirective();
        List<Directive> directiveList = new ArrayList<>();
        directiveList.add(dd);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setDirectives(directiveList);
        speechletResponse.setNullableShouldEndSession(false);
        return speechletResponse;
    }
}
