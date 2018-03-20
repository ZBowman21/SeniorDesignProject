package edu.psu.unifiedapi.getcapstonedata;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class GetCapstoneData implements IGetCapstoneData {
    public GetCapstoneData()  {
        Database.init();
    }
    @Override
    public String getCapstoneData(GetCapstoneDataArgs args) {
        String token = null;

        try {
            token = Database.getCapstoneData(args.userId);
        } catch (SQLException e) {
        }

        if (token == null) {
            throw new RuntimeException("Account not found");
        }

        return token;
    }
}
