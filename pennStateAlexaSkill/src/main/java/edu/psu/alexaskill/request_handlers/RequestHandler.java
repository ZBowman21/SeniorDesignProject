package edu.psu.alexaskill.request_handlers;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;
import edu.pennstate.api.PennStateUnified;
import edu.pennstate.api.PennStateUnifiedClientBuilder;

public abstract class RequestHandler
{
    protected PennStateUnified client;

    public RequestHandler()
    {
        client = getBuilder().build();
    }

    public RequestHandler(String token)
    {
        client = getBuilder().signer(R -> token).build();
    }

    private PennStateUnifiedClientBuilder getBuilder() {
        return PennStateUnified.builder().connectionConfiguration(new ConnectionConfiguration()
                .maxConnections(100)
                .connectionMaxIdleMillis(30000))
                .timeoutConfiguration(new TimeoutConfiguration()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .socketTimeout(30000));
    }

    public abstract BaseResult sendRequest(Intent requestIntent);
    public abstract SpeechletResponse parseResponse(BaseResult response);
}
