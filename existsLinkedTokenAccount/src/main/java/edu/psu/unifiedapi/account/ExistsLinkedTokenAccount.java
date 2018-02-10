package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class ExistsLinkedTokenAccount {

    public ExistsLinkedTokenAccount() {
        Database.init();
    }

    public void handleRequest(ExistsLinkedTokenAccountArgs aA) throws RuntimeException {
    	try {
			if (!Database.existsTokenCredentials(aA.userId, aA.service)) {
				throw new RuntimeException("Account not found");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Internal server error");
		}
    }

}
