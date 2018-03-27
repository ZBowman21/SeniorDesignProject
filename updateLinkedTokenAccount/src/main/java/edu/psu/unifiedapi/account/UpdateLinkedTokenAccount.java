package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.sql.SQLException;
import edu.psu.unifiedapi.database.Database;

public class UpdateLinkedTokenAccount implements RequestHandler<UpdateLinkedTokenAccountArgs, Boolean> {

    public UpdateLinkedTokenAccount() {
        Database.init();
    }

    @Override
    public Boolean handleRequest(UpdateLinkedTokenAccountArgs aA, Context context) {

        try {
            return Database.updateTokenCredentials(aA.username, aA.service, aA.value);
        } catch (SQLException e) {
            context.getLogger().log("Error accessing database: " + e.getMessage());
        }

        return false;
    }

}
