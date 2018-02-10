package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class ExistsLinkedPlainAccount {

    public ExistsLinkedPlainAccount() {
        Database.init();
    }

    public boolean handleRequest(ExistsLinkedPlainAccountArgs aA) throws SQLException {
        return Database.existsPlainCredentials(aA.userId, aA.service);
    }

}
