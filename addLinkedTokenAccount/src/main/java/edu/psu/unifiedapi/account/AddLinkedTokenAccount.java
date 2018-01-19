package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;


public class AddLinkedTokenAccount implements RequestHandler<AddLinkedTokenAccountArgs, Boolean> {

	public AddLinkedTokenAccount() {
		Database.init();
	}

	@Override
	public Boolean handleRequest(AddLinkedTokenAccountArgs args, Context context) {
		try {
			return Database.insertTokenCredentials(args.userId, args.passphrase, args.token);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}