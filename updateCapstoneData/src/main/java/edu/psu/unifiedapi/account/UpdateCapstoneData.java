package edu.psu.unifiedapi.updatecapstonedata;

import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class UpdateCapstoneData implements IUpdateCapstoneData {
    public UpdateCapstoneData()  {
        Database.init();
    }
    @Override
    public boolean updateCapstoneData(UpdateCapstoneDataArgs args) {
        try {
            return Database.updateCapstoneData(args.userId, args.teamId);
        } catch (SQLException e) {

        }

        return false;
    }
}
