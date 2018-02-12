package edu.psu.alexaskill.intent_handlers;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.psu.alexaskill.PassphraseManager;
import edu.psu.unifiedapi.account.CognitoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class SecureIntentHandler implements IntentHandler
{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public SpeechletResponse IntentStarted(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
    {
        Intent intent = requestEnvelope.getRequest().getIntent();
        DialogIntent dialogIntent = new DialogIntent();
        SpeechletResponse speechletResponse = new SpeechletResponse();

        requestEnvelope.getSession().setAttribute("passphraseChecked", false);

        boolean hasPassphrase = CognitoUtils.hasPassphrase(requestEnvelope.getSession().getUser().getAccessToken());

        dialogIntent.setName(intent.getName());
        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        for(Map.Entry<String, Slot> entry : intent.getSlots().entrySet())
        {
            Slot slot = entry.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());

            //Check if user has a passphrase setup
            if(slot.getName().equalsIgnoreCase("passphrase") && !hasPassphrase)
            {
                dialogSlot.setValue("abcdefg");
                dialogSlot.setConfirmationStatus(ConfirmationStatus.CONFIRMED);
            }
            else
            {
                dialogSlot.setValue(slot.getValue());
            }

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
    public SpeechletResponse IntentInProgress(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
    {
        Intent intent = requestEnvelope.getRequest().getIntent();
        DialogIntent dialogIntent = new DialogIntent(intent);
        Map<String, DialogSlot> dialogSlots = new HashMap<>();
        SpeechletResponse speechletResponse = new SpeechletResponse();

        boolean invalidPassphrase = false;

        for(Map.Entry<String, Slot> entry : intent.getSlots().entrySet())
        {
            Slot slot = entry.getValue();
            DialogSlot dialogSlot = new DialogSlot();
            dialogSlot.setName(slot.getName());
            dialogSlot.setValue(slot.getValue());

            if(dialogSlot.getName().equalsIgnoreCase("passphrase"))
            {
                if(dialogSlot.getValue() != null && !dialogSlot.getValue().equals("abcdefg") && !(boolean)requestEnvelope.getSession().getAttribute("passphraseChecked"))
                {
                    //Check if passphrase is correct
                    logger.info("Checking Passphrase");
                    boolean passphraseMatch = PassphraseManager.checkPassphrase(dialogSlot.getValue(), requestEnvelope.getSession().getUser().getAccessToken());
                    if(!passphraseMatch)
                    {
                        logger.info("Passphrase mismatch");
                        invalidPassphrase = true;
                    }
                    else
                    {
                        requestEnvelope.getSession().setAttribute("passphraseChecked", true);
                    }
                }
            }
            dialogSlots.put(entry.getKey(), dialogSlot);

            logger.info("Key {}", entry.getKey());
            logger.info("InProgress - Slot Name: {} Slot Value: {}", dialogSlot.getName(), dialogSlot.getValue());
        }

        dialogIntent.setSlots(dialogSlots);
        List<Directive> directiveList = new ArrayList<>();

        if(invalidPassphrase)
        {

            logger.info("Passphrase mismatch, returning confirmslotdirective");
            ElicitSlotDirective esd = new ElicitSlotDirective();
            esd.setUpdatedIntent(dialogIntent);
            esd.setSlotToElicit("passphrase");
            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            String output = "Incorrect passphrase. Please try again.";
            speech.setText(output);
            speechletResponse.setOutputSpeech(speech);
            directiveList.add(esd);
        }
        else
        {
            logger.info("returning delegatedirective");
            DelegateDirective dd = new DelegateDirective();
            directiveList.add(dd);
        }

        speechletResponse.setDirectives(directiveList);
        speechletResponse.setNullableShouldEndSession(false);
        return speechletResponse;
    }
}
