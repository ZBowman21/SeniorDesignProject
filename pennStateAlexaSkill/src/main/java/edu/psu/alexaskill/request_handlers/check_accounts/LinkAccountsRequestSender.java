package edu.psu.alexaskill.request_handlers.check_accounts;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import edu.pennstate.api.model.ExistsTokenAccountRequest;
import edu.pennstate.api.model.PennStateUnifiedException;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import edu.pennstate.api.model.ExistsPlainAccountRequest;

public class LinkAccountsRequestSender extends RequestHandler {

    public LinkAccountsRequestSender(String token) {
        super(token);
    }

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {
        throw new UnsupportedOperationException();
    }

    public boolean sendRequest(String service)
    {
        int statusCode = 200;

        if(service.equals("webmail"))
        {
            ExistsPlainAccountRequest request = new ExistsPlainAccountRequest();
            request.setService(service);
            try
            {
                client.existsPlainAccount(request);
            }
            catch(PennStateUnifiedException e)
            {
                statusCode = e.sdkHttpMetadata().httpStatusCode();
            }
        }
        else
        {
            ExistsTokenAccountRequest request = new ExistsTokenAccountRequest();
            request.setService(service);
            try
            {
                client.existsTokenAccount(request);
            }
            catch(PennStateUnifiedException e)
            {
                statusCode = e.sdkHttpMetadata().httpStatusCode();
            }
        }

        if(statusCode == 200)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
