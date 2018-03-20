package edu.psu.unfiedapi.existCapstoneData;

import edu.psu.unifiedapi.database.Database;
import java.sql.SQLException;

public class ExistsCapstoneData implements IExistsCapstoneData {
    public ExistsCapstoneData()  {
        Database.init();
    }
    @Override
    public void existsCapstoneData(ExistsCapstoneDataArgs args) {
        try {
            if (!Database.existsCapstoneData(args.userId)) {
                throw new RuntimeException("Account not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
