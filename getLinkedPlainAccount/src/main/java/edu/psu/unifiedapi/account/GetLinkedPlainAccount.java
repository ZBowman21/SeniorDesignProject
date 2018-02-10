package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class GetLinkedPlainAccount {

    public GetLinkedPlainAccount() {
        Database.init();
    }

    public Credentials handleRequest(GetLinkedPlainAccountArgs aA, Context context) throws GeneralSecurityException, SQLException, AccountNotFoundException {

        Credentials creds = null;

        context.getLogger().log("Getting plain credentials for user: " + aA.userId + " and service: " + aA.service);
        creds = Database.getPlainCredentials(aA.userId, aA.passphrase, aA.service);
        if (creds == null) {
            context.getLogger().log("User '" + aA.userId + "' not found in the database");
            throw new AccountNotFoundException();
        }

        return creds;
    }

}
