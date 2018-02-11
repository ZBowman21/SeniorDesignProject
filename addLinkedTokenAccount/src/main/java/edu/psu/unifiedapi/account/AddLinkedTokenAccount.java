package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;


public class AddLinkedTokenAccount implements IAddLinkedTokenAccount {

	public AddLinkedTokenAccount() {
		Database.init();
	}

	public void addLinkedTokenAccount(AddLinkedTokenAccountArgs args) {
		try {
			if (!Database.insertTokenCredentials(args.userId, args.service, args.token)) {
				throw new RuntimeException("Internal server error");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Internal server error");
		}
	}

}