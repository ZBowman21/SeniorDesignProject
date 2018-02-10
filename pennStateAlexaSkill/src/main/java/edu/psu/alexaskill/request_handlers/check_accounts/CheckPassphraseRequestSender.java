package edu.psu.alexaskill.request_handlers.check_accounts;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.psu.alexaskill.request_handlers.RequestHandler;

public class CheckPassphraseRequestSender extends RequestHandler{

    @Override
    public BaseResult sendRequest(Intent requestIntent, String token)
    {
        return null;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response)
    {
        return null;
    }
}
