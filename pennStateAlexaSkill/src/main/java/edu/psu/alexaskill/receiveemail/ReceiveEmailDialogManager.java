package edu.psu.alexaskill.receiveemail;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import edu.pennstate.api.model.ReceiveEmailsResult;
import edu.pennstate.api.model.ReceivedEmail;

public class ReceiveEmailDialogManager
{
    private ReceiveEmailRequestSender requestSender = new ReceiveEmailRequestSender();
    private ReceiveEmailState state;
    private Intent intent;
    private Session session;

    public ReceiveEmailDialogManager(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
    {
        session = requestEnvelope.getSession();
        intent = requestEnvelope.getRequest().getIntent();
        state = (ReceiveEmailState)session.getAttribute("state");

        if(state == null)
        {
            state = new ReceiveEmailState();
        }
    }

    private SpeechletResponse generateNoUnreadSpeechlet()
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();
        String message = "I couldn't find any new messages.";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.NoUnread);

        return response;
    }

    private SpeechletResponse generateReadEmailSpeechlet(ReceivedEmail email)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String message = "Your email from " + email.getFrom() + " sent " + email.getDate() + " with a subject of " +
                email.getSubject() + ". The email reads: " + email.getBody() + " Would you like to repeat that or hear your next email.";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.ReadingEmail);

        return response;
    }

    private SpeechletResponse generateNextEmailSpeechlet(ReceivedEmail email)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String message = "Your next email is from " + email.getFrom() + " sent " + email.getDate() + "." +
                "Would you like to hear it or skip it?";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.NextEmail);
        state.setEmail(email);

        return response;

    }

    private SpeechletResponse generateFirstUnreadSpeechlet(ReceivedEmail email)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String message = "You have " + email.getUnread() + " unread messages. Your first message is from " +
                email.getFrom() + " sent " + email.getDate() + ". Would you like to hear it or skip it?";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.FirstUnread);
        state.setEmail(email);

        return response;
    }

    public SpeechletResponse generateResponse()
    {
        SpeechletResponse response = new SpeechletResponse();
        ReceiveEmailsResult result;
        ReceivedEmail email;
        boolean getNewEmail = false;

        switch(state.getState())
        {
            case Initial:
                if(state.getCurrentUnread() == 0)
                {
                    state.setState(ReceiveEmailState.State.NoUnread);
                }
                else
                {
                    state.setState(ReceiveEmailState.State.FirstUnread);
                }
                break;
            case FirstUnread:
                if(intent.getName().equals("Skip") && state.getCurrentUnread() == 1)
                {
                    state.setState(ReceiveEmailState.State.NoUnread);
                    getNewEmail = true;
                }
                else if(intent.getName().equals("Skip") && state.getCurrentUnread() > 1)
                {
                    state.setState(ReceiveEmailState.State.NextEmail);
                    state.setCurrentEmailIndex(state.getCurrentEmailIndex() + 1);
                    getNewEmail = true;
                }
                else
                {
                    state.setState(ReceiveEmailState.State.ReadingEmail);
                }
                break;
            case ReadingEmail:
                if(intent.getName().equals("Repeat"))
                {
                    state.setState(ReceiveEmailState.State.ReadingEmail);
                }
                else if(intent.getName().equals("Next") && state.getCurrentUnread() == 0)
                {
                    state.setState(ReceiveEmailState.State.NoUnread);
                }
                else
                {
                    state.setState(ReceiveEmailState.State.NextEmail);
                    state.setCurrentEmailIndex(state.getCurrentEmailIndex() + 1);
                    getNewEmail = true;
                }
                break;
            case NextEmail:
                if(intent.getName().equals("Skip") && state.getCurrentUnread() == 0)
                {
                    state.setState(ReceiveEmailState.State.NoUnread);
                }
                else if(intent.getName().equals("Skip") && state.getCurrentUnread() > 0)
                {
                    state.setState(ReceiveEmailState.State.NextEmail);
                    state.setCurrentEmailIndex(state.getCurrentEmailIndex() + 1);
                    getNewEmail = true;
                }
                else
                {
                    state.setState(ReceiveEmailState.State.ReadingEmail);
                }
                break;
        }

        if(!getNewEmail)
        {
            email = state.getEmail();
        }
        else
        {
            if(state.getState() == ReceiveEmailState.State.Initial)
            {
                result = (ReceiveEmailsResult)requestSender.sendRequest(intent, session.getUser().getAccessToken());
            }
            else
            {
                result = (ReceiveEmailsResult)requestSender.sendRequest(intent, session.getUser().getAccessToken(), state.getCurrentEmailIndex());
            }

            email = result.getReceivedEmail();
        }

        if(email.getUnread() == 0)
        {
            state.setCurrentUnread(0);
        }
        else
        {
            state.setCurrentUnread(email.getUnread().intValue());
        }

        switch(state.getState())
        {
            case FirstUnread:
                response = generateFirstUnreadSpeechlet(email);
                break;
            case NoUnread:
                response = generateNoUnreadSpeechlet();
                break;
            case ReadingEmail:
                response = generateReadEmailSpeechlet(email);
                break;
            case NextEmail:
                response = generateNextEmailSpeechlet(email);
                break;
        }

        session.setAttribute("state", state);
        return  response;
    }
}
