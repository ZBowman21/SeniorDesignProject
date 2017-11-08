package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;
import edu.pennstate.api.PennStateUnified;

public abstract class RequestHandler
{
    protected PennStateUnified client;

    public RequestHandler()
    {
        client = PennStateUnified.builder().connectionConfiguration(new ConnectionConfiguration()
                .maxConnections(100)
                .connectionMaxIdleMillis(30000))
                .timeoutConfiguration(new TimeoutConfiguration()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .socketTimeout(30000)).build();
    }

    public abstract BaseResult sendRequest(Intent requestIntent);
    public abstract SpeechletResponse parseResponse(BaseResult response);
}
