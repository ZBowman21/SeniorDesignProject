package edu.psu.unifiedapi.capstone.utils;

import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;
import edu.psu.unifiedapi.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class CapstoneDbUtils {

	private static Logger logger = LoggerFactory.getLogger(CapstoneDbUtils.class);

	public static String getTeamId(String username) {

		String teamId = null;

		try {
			teamId = Database.getCapstoneData(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (teamId == null) {
			logger.info("Team ID is null (not in db)");

			CapstoneWrapper cap = new CapstoneWrapper(username, "/AgileTask/EGetMyTeamID", "csid=4");
			ResponseTeamId response;
			try {
				response = cap.capCall(ResponseTeamId.class);
			} catch (CapstoneException e) {
				throw new RuntimeException(e.getMessage());
			}

			teamId = response.response.teamid;

			logger.info("Team ID retrieved from Capstone, got {}", teamId);

			try {
				Database.insertCapstoneData(teamId, username);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return teamId;
	}

}
