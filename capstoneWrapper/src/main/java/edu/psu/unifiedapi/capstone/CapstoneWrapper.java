package edu.psu.unifiedapi.capstone;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

public class CapstoneWrapper implements RequestHandler<CapstoneWrapperArgs,String>{

    @Override
    public String handleRequest(CapstoneWrapperArgs input, Context context) {
        CapstoneAuth capAuth = new CapstoneAuth("2c1611e48121a3f865b3e6248ffdff507b9c30500a87b52a558cc21d39420c77");




        return null;
    }
}
