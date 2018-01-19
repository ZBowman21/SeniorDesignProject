package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DelLinkedPlainAccount implements RequestHandler<DelLinkedPlainAccountArgs, Boolean> {

	public DelLinkedPlainAccount() {
		Database.init();
	}

	@Override
	public Boolean handleRequest(DelLinkedPlainAccountArgs args, Context context) {
		try {
			return Database.deletePlainCredentials(args.userId, args.service);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}