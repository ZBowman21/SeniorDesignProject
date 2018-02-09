package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPassphrase implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String userId, Context context) {
        return CognitoUtils.getPassphrase(userId);
    }

}
