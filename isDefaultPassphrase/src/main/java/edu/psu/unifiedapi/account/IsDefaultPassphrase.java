package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class IsDefaultPassphrase implements RequestHandler<String, Boolean> {

    @Override
    public Boolean handleRequest(String userId, Context context) {
        return CognitoUtils.isDefaultPassphrase(userId);
    }

}
