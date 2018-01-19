package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.database.Database;


public class DelLinkedTokenAccount implements RequestHandler<DelLinkedTokenAccountArgs, Boolean> {

	public DelLinkedTokenAccount() {
		Database.init();
	}

	@Override
	public Boolean handleRequest(DelLinkedTokenAccountArgs args, Context context) {
		try {
			return Database.deleteTokenCredentials(args.userId, args.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}