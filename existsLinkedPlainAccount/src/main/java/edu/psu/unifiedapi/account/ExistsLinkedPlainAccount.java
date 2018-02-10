package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class ExistsLinkedPlainAccount {

    public ExistsLinkedPlainAccount() {
        Database.init();
    }

    public void handleRequest(ExistsLinkedPlainAccountArgs aA) throws RuntimeException {
        try {
            if (!Database.existsPlainCredentials(aA.userId, aA.service)) {
                throw new RuntimeException("Account not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Internal server error");
        }
    }

}
