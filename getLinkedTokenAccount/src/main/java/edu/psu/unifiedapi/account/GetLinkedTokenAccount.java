package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class GetLinkedTokenAccount {

	public GetLinkedTokenAccount() {
		Database.init();
	}

	public String handleRequest(GetLinkedTokenAccountArgs aA, Context context) {

		String token = null;

		context.getLogger().log("Getting token credentials for user: " + aA.userId + " and service: " + aA.service);

		try {
			token = Database.getTokenCredentials(aA.userId, aA.service);
		} catch (SQLException e) {
			context.getLogger().log("Error accessing database: " + e.getMessage());
		}

		if (token == null) {
			context.getLogger().log("User '" + aA.userId + "' not found in the database");
			throw new RuntimeException("Account not found");
		}

		return token;
	}

}
