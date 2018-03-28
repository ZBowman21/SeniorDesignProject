package edu.psu.unifiedapi.account;

import edu.psu.unifiedapi.capstone.utils.CapstoneDbUtils;
import edu.psu.unifiedapi.database.Database;

public class GetCapstoneData implements IGetCapstoneData {
    public GetCapstoneData()  {
        Database.init();
    }
    @Override
    public String getCapstoneData(GetCapstoneDataArgs args) {
        String token = null;

        token = CapstoneDbUtils.getTeamId(args.userId);

        if (token == null) {
            throw new RuntimeException("Account not found");
        }

        return token;
    }
}
