package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class GetLinkedPlainAccount implements RequestHandler<GetLinkedPlainAccountArgs, Credentials> {

    public GetLinkedPlainAccount() {
        Database.init();
    }

    @Override
    public Credentials handleRequest(GetLinkedPlainAccountArgs aA, Context context) {

        Credentials creds = null;

        try {
            context.getLogger().log("Getting plain credentials for user: " + aA.username + " and service: " + aA.service);
            creds = Database.getPlainCredentials(aA.username, aA.passphrase, aA.service);
            if (creds == null) {
                context.getLogger().log("User '" + aA.username + "' not found in the database");
            }
        } catch (SQLException e) {
            context.getLogger().log("Error accessing database: " + e.getMessage());
        } catch(GeneralSecurityException e) {
            context.getLogger().log("Error decrypting password: " + e.getMessage());
        }

        return creds;
    }

}
