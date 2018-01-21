package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.restclientutil.RestClient;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.StringReader;


public class CapstoneWrapper implements RequestHandler<CapstoneWrapperArgs,Object>{

    private final String baseUrl = "http://capstone.bd.psu.edu:8090";
    @Override
    public Object handleRequest(CapstoneWrapperArgs input, Context context) {
        CapstoneAuth capAuth = new CapstoneAuth("94e61ea81c7c74937ddbd6c5636d9cfaea081c3a8903a160cae779cec36281c1");

        RestClient client = new RestClient(baseUrl + input.url, input.params + capAuth.BuildAuthString());
        String response = client.GetRequest();

        JacksonFactory jack = new JacksonFactory();
        JsonObjectParser parser = new JsonObjectParser(jack);
        CapstoneResponse capRes = new CapstoneResponse();
        //capRes.response = ""; // to avoid null pointer exception
        try {
            capRes = parser.parseAndClose(new StringReader(response), CapstoneResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // manipulate the capRes.Auth here....

        return capRes.response;
    }
}
