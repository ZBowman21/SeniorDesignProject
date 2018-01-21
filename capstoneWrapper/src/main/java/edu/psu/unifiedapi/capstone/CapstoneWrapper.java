package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.restclientutil.RestClient;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.StringReader;


public class CapstoneWrapper implements RequestHandler<CapstoneWrapperArgs,String>{

    private final String baseUrl = "http://capstone.bd.psu.edu:8090";
    @Override
    public String handleRequest(CapstoneWrapperArgs input, Context context) {
        CapstoneAuth capAuth = new CapstoneAuth("fa63eb77e6d6c7dd31d087a6345ca4f3c2abc7a3969dba6c6aa02d1d42961316");

        RestClient client = new RestClient(baseUrl + input.url, input.params + capAuth.BuildAuthString());
        String response = client.GetRequest();

        JacksonFactory jack = new JacksonFactory();
        JsonObjectParser parser = new JsonObjectParser(jack);
        CapstoneResponse capRes = new CapstoneResponse();
        capRes.response = ""; // to avoid null pointer exception
        try {
            capRes = parser.parseAndClose(new StringReader(response), CapstoneResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return capRes.response;
    }
}
