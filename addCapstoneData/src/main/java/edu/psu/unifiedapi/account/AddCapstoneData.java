package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.database.Database;
import java.sql.SQLException;

public class AddCapstoneData implements IAddCapstoneData {
    @Override
    public void addCapstoneData(AddCapstoneDataArgs args) {
        try {
            if (!Database.insertCapstoneData(args.userId, args.teamId)) {
                throw new RuntimeException("Internal server error");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
