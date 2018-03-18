package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.account.GetLinkedTokenAccountArgs;
import edu.psu.unifiedapi.account.IGetLinkedTokenAccount;
import edu.psu.unifiedapi.account.IUpdateLinkedTokenAccount;
import edu.psu.unifiedapi.account.UpdateLinkedTokenAccountArgs;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.capstone.utils.CapstoneResponse;
import edu.psu.unifiedapi.restclientutil.RestClient;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.StringReader;


public class CapstoneWrapper{

    private final String baseUrl = "http://capstone.bd.psu.edu:8090";
    private String username, path, params;

    public CapstoneWrapper(String username, String path, String params){
        this.username = username;
        this. path = path;
        this. params = params == null ? "" : params;
    }

    public CapstoneResponse CapCall(Class responseClass, Context context) throws CapstoneException{

        GetLinkedTokenAccountArgs ta = new GetLinkedTokenAccountArgs();
        ta.userId = username;
        ta.service = "capstone";

        IGetLinkedTokenAccount tokenAccount = LambdaInvokerFactory.builder().build(IGetLinkedTokenAccount.class);
        String token = tokenAccount.getLinkedTokenAccount(ta);      // Check with Matt on this one....

        // Regex...
        String[] authId = token.split(" ");

        context.getLogger().log("Using authid = " + authId[1] + " and nounce = " + authId[0]);

        CapstoneAuth capAuth = new CapstoneAuth(authId[1], authId[0]);

        RestClient client = new RestClient(baseUrl + path, params + capAuth.BuildAuthString());
        String response = client.GetRequest();

        JacksonFactory jack = new JacksonFactory();
        JsonObjectParser parser = new JsonObjectParser(jack);
        CapstoneResponse capRes = new CapstoneResponse();
        //capRes.response = ""; // to avoid null pointer exception
        try {
            capRes = (CapstoneResponse)parser.parseAndClose(new StringReader(response), responseClass);
        } catch (IOException e) {
            context.getLogger().log(e.getMessage());
        }

        if(!capRes.valid)
            throw new CapstoneException("Failed to authenticate with Capstone, must request new Authentication Object from Capstone.");

        // manipulate the capRes.Auth here....
        UpdateLinkedTokenAccountArgs ua = new UpdateLinkedTokenAccountArgs();
        ua.username = username;
        ua.service = ta.service;
        context.getLogger().log(response);
        ua.value = authId[0] + " " + capRes.AuthenticateObject.NounceCode;

        IUpdateLinkedTokenAccount updateToken = LambdaInvokerFactory.builder().build(IUpdateLinkedTokenAccount.class);
        updateToken.updateLinkedTokenAccount(ua);

        return capRes;
    }
}
