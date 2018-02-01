package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.account.GetLinkedTokenAccountArgs;
import edu.psu.unifiedapi.account.IGetLinkedTokenAccount;
import edu.psu.unifiedapi.account.IUpdateLinkedTokenAccount;
import edu.psu.unifiedapi.account.UpdateLinkedTokenAccountArgs;
import edu.psu.unifiedapi.capstoneutils.CapstoneResponse;
import edu.psu.unifiedapi.restclientutil.RestClient;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;

import java.io.IOException;
import java.io.StringReader;


public class CapstoneWrapper implements RequestHandler<CapstoneWrapperArgs,CapstoneResponse>{

    private final String baseUrl = "http://capstone.bd.psu.edu:8090";
    @Override
    public CapstoneResponse handleRequest(CapstoneWrapperArgs input, Context context) {

        GetLinkedTokenAccountArgs ta = new GetLinkedTokenAccountArgs();
        ta.username = input.username;
        ta.service = "capstone";    // Don't know if it will be uppercase or not

        IGetLinkedTokenAccount tokenAccount = LambdaInvokerFactory.builder().build(IGetLinkedTokenAccount.class);
        String nounce = tokenAccount.getLinkedTokenAccount(ta);

        context.getLogger().log(nounce);

        CapstoneAuth capAuth = new CapstoneAuth(nounce);

        RestClient client = new RestClient(baseUrl + input.url, input.params + capAuth.BuildAuthString());
        String response = client.GetRequest();

        JacksonFactory jack = new JacksonFactory();
        JsonObjectParser parser = new JsonObjectParser(jack);
        CapstoneResponse capRes = new CapstoneResponse();
        //capRes.response = ""; // to avoid null pointer exception
        try {
            capRes = parser.parseAndClose(new StringReader(response), CapstoneResponse.class);
        } catch (IOException e) {
            context.getLogger().log(e.getMessage());
        }

        // manipulate the capRes.Auth here....
        UpdateLinkedTokenAccountArgs ua = new UpdateLinkedTokenAccountArgs();
        ua.username = input.username;
        ua.service = ta.service;
        context.getLogger().log(response);
        ua.value = capRes.AuthenticateObject.NounceCode;

        IUpdateLinkedTokenAccount updateToken = LambdaInvokerFactory.builder().build(IUpdateLinkedTokenAccount.class);
        updateToken.updateLinkedTokenAccount(ua);

        return capRes;
    }
}
