package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.fasterxml.jackson.databind.ser.Serializers;
import edu.pennstate.api.model.EmailsItem;
import edu.pennstate.api.model.ReceiveEmailsResult;
import edu.pennstate.api.model.ReceivedEmails;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.List;

public class ReceiveEmailDialogManager
{
    private int emailIndex = 0;
    private ReceiveEmailRequestSender requestSender = new ReceiveEmailRequestSender();
    private enum State
    {
        NoEmail,
        PostEmail,
        RequestingMoreEmails,
        InitialRetrieveEmails,
        Initial
    }

    private State currentState = State.Initial;

    public SpeechletResponse GenerateNoEmailsSpeechlet()
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();
        String message = "I couldn't find any new messages.";

        card.setContent(message);
        outputSpeech.setText(message);

        response.setCard(card);
        response.setOutputSpeech(outputSpeech);
        return response;
    }

    public SpeechletResponse GeneratePostEmailSpeechlet()
    {

    }

    public SpeechletResponse GenerateRequestMoreEmailsSpeechlet()
    {

    }

    public SpeechletResponse GenerateInitialRetrieveEmailResponseSpeechlet()
    {

    }

    public SpeechletResponse GenerateResponse(Intent requestIntent, String accessToken)
    {
        SpeechletResponse response = new SpeechletResponse();

        switch(currentState)
        {
            case Initial:
                BaseResult result = requestSender.sendRequest(requestIntent, accessToken);
                ReceiveEmailsResult receiveEmailsResult = (ReceiveEmailsResult)result;
                ReceivedEmails emails = receiveEmailsResult.getReceivedEmails();

                List<EmailsItem> emailList = emails.getEmails();
                if(emailList.size() == 0)
                {
                    response = GenerateNoEmailsSpeechlet();
                }
                else
                {
                    String firstSender = emailList.get(0).getFrom();
                    String responseText = "Here are your first " + emailList.size() + " unread email messages. The first is from " +
                            firstSender + ". Would you like to hear it or skip it?";

                    SimpleCard card = new SimpleCard();
                    PlainTextOutputSpeech speechOutput = new PlainTextOutputSpeech();

                    card.setContent(responseText);
                    speechOutput.setText(responseText);

                    response.setOutputSpeech(speechOutput);
                    response.setCard(card);
                }
                break;
            case InitialRetrieveEmails:
                break;
            case RequestingMoreEmails:
                break;
            case PostEmail:
                break;
            case NoEmail:
                break;

        }
        return  response;
    }
}
