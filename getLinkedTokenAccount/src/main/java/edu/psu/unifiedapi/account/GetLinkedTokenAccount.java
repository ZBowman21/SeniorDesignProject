package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class GetLinkedTokenAccount implements RequestHandler<GetLinkedTokenAccountArgs, String> {

    public GetLinkedTokenAccount() {
        Database.init();
    }

    @Override
    public String handleRequest(GetLinkedTokenAccountArgs aA, Context context) {

        String token = null;

        try {
            token = Database.getTokenCredentials(aA.username, aA.service);
            if (token == null) {
                context.getLogger().log("User '" + aA.username + "' not found in the database");
            }
        } catch (SQLException e) {
            context.getLogger().log("Error accessing database: " + e.getMessage());
        }

        return token;
    }

}
