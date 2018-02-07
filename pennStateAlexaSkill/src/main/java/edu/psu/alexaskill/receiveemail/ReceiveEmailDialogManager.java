package edu.psu.alexaskill.receiveemail;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pennstate.api.model.ReceiveEmailsResult;
import edu.pennstate.api.model.ReceivedEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveEmailDialogManager
{
    private ReceiveEmailRequestSender requestSender = new ReceiveEmailRequestSender();
    private MarkEmailReadRequestSender markEmailRequestSender = new MarkEmailReadRequestSender();
    private ReceiveEmailState state;
    private Intent intent;
    private Session session;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ReceiveEmailDialogManager(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)
    {
        session = requestEnvelope.getSession();
        intent = requestEnvelope.getRequest().getIntent();
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String mappedJsonState = mapper.writeValueAsString(session.getAttribute("state"));
            state = mapper.readValue(mappedJsonState, ReceiveEmailState.class);
        }
        catch(Exception e)
        {}

        if(state == null) //Occurs on initial execution, as there is no mid-interaction state.
        {
            state = new ReceiveEmailState();
            session.setAttribute("passphrase", intent.getSlot("passphrase").getValue().toLowerCase());
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

        String from = email.getFrom().replaceAll("<.*>", "");

        String bodyToRead = email.getBody().replaceAll("www\\..*\\s", "");
        bodyToRead = bodyToRead.replaceAll("http.*\\s", "");
        bodyToRead = bodyToRead.replaceAll("https.*\\s", "");

        String message = "Your email from " + from + " has a subject of " +
                email.getSubject() + ". The email reads: " + bodyToRead + " Would you like to repeat that or hear your next email.";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.ReadingEmail);

        markEmailRequestSender.sendRequest((String)session.getAttribute("passphrase"), session.getUser().getAccessToken(), state.getCurrentEmailIndex());

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);
    }

    private SpeechletResponse generateNextEmailSpeechlet(ReceivedEmail email)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();
        String from = email.getFrom().replaceAll("<.*>", "");

        String message = "Your next email is from " + from + " sent " + email.getDate() + "." +
                "Would you like to hear it or skip it?";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.NextEmail);
        state.setEmail(email);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);    }

    private SpeechletResponse generateFirstUnreadSpeechlet(ReceivedEmail email)
    {
        SpeechletResponse response = new SpeechletResponse();
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        //PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();

        SimpleCard card = new SimpleCard();

        String from = email.getFrom().replaceAll("<.*>", "");

        String message = "You have " + email.getUnread().intValue() + " unread messages. Your first message is from " +
                from + " sent " + email.getDate() + ". Would you like to hear it or skip it?";

        String ssmlMessage = "<speak> <audio src=\"https://s3.amazonaws.com/psuunifiedwebsite/mail_time_new.mp3\" /> You have " + email.getUnread().intValue() + " unread messages. Your first message is from " +
                from + " sent " + email.getDate() + ". Would you like to hear it or skip it? </speak>";

        logger.info(ssmlMessage);
        card.setContent(message);

        outputSpeech.setSsml(ssmlMessage);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);

        state.setState(ReceiveEmailState.State.FirstUnread);
        state.setEmail(email);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);
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
                result = (ReceiveEmailsResult)requestSender.sendRequest((String)session.getAttribute("passphrase"), session.getUser().getAccessToken(), 0);
                email = result.getReceivedEmail();
                state.setEmail(email);
                state.setCurrentEmailIndex(0);
                state.setCurrentUnread(email.getUnread().intValue());

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
                    state.setCurrentEmailIndex(state.getCurrentEmailIndex());
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

        //Requests a new email if one is needed, such as when the user requests their next email
        if(!getNewEmail)
        {
            email = state.getEmail();
        }
        else
        {
            result = (ReceiveEmailsResult)requestSender.sendRequest((String)session.getAttribute("passphrase"), session.getUser().getAccessToken(), state.getCurrentEmailIndex());
            email = result.getReceivedEmail();
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
        return response;
    }
}
