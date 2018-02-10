package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.database.Database;


public class AddLinkedPlainAccount implements RequestHandler<AddLinkedPlainAccountArgs, Boolean> {

	public AddLinkedPlainAccount() {
		Database.init();
	}

	@Override
	public Boolean handleRequest(AddLinkedPlainAccountArgs args, Context context) {
		try {
			String encryptionKey = CognitoUtils.getEncryptionKey(args.userId);
			return Database.insertPlainCredentials(args.userId, encryptionKey, args.service, args.username, args.password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}