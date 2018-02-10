package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class GetLinkedPlainAccount {

    public GetLinkedPlainAccount() {
        Database.init();
    }

    public Credentials handleRequest(GetLinkedPlainAccountArgs aA, Context context) throws RuntimeException {

        Credentials creds;

        context.getLogger().log("Getting plain credentials for user: " + aA.userId + " and service: " + aA.service);
        try {
			String encryptionKey = CognitoUtils.getEncryptionKey(aA.userId);
			creds = Database.getPlainCredentials(aA.userId, encryptionKey, aA.service);
		} catch (SQLException | GeneralSecurityException e) {
			throw new RuntimeException("Internal server error");
		}
        if (creds == null) {
            context.getLogger().log("User '" + aA.userId + "' not found in the database");
			throw new RuntimeException("Account not found");
        }

        return creds;
    }

}
