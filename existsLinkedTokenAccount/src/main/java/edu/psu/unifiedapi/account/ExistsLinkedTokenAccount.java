package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class ExistsLinkedTokenAccount {

    public ExistsLinkedTokenAccount() {
        Database.init();
    }

    public boolean handleRequest(ExistsLinkedTokenAccountArgs aA) throws SQLException {
        return Database.existsTokenCredentials(aA.userId, aA.service);
    }

}
