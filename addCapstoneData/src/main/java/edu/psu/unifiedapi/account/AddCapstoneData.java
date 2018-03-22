package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import edu.psu.unifiedapi.database.Database;
import java.sql.SQLException;
import edu.psu.unifiedapi.capstone.teamid.CapstoneTeamIdArgs;
import edu.psu.unifiedapi.capstone.teamid.ICapstoneTeamId;

public class AddCapstoneData implements IAddCapstoneData {
    @Override
    public void addCapstoneData(AddCapstoneDataArgs args) {

        CapstoneTeamIdArgs ca = new CapstoneTeamIdArgs();
        ca.username = args.userId;

        ICapstoneTeamId icap = LambdaInvokerFactory.builder().build(ICapstoneTeamId.class);
        String teamId = icap.getTeamId(ca);

        try {
            if (!Database.insertCapstoneData(args.userId, teamId)) {
                throw new RuntimeException("Internal server error");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
