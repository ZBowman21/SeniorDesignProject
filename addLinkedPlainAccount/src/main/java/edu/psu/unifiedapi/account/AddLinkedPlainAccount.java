package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.security.GeneralSecurityException;
import java.sql.SQLException;


public class AddLinkedPlainAccount implements IAddLinkedPlainAccount {

	public AddLinkedPlainAccount() {
		Database.init();
	}

	public void addLinkedPlainAccount(AddLinkedPlainAccountArgs args) throws RuntimeException {
		String encryptionKey = CognitoUtils.getEncryptionKey(args.userId);
		try {
			if (!Database.insertPlainCredentials(args.userId, encryptionKey, args.service, args.username, args.password)) {
				throw new RuntimeException("Internal server error");
			}
		} catch (SQLException | GeneralSecurityException e) {
			throw new RuntimeException("Internal server error");
		}
	}

}