package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.restclientutil.RestClient;


public class CapstoneWrapper implements RequestHandler<CapstoneWrapperArgs,String>{

    private final String baseUrl = "http://capstone.bd.psu.edu:8090";
    @Override
    public String handleRequest(CapstoneWrapperArgs input, Context context) {
        CapstoneAuth capAuth = new CapstoneAuth("2c1611e48121a3f865b3e6248ffdff507b9c30500a87b52a558cc21d39420c77");

        RestClient client = new RestClient(baseUrl + input.url, input.params + capAuth.BuildAuthString());
        return client.GetRequest();
    }
}
