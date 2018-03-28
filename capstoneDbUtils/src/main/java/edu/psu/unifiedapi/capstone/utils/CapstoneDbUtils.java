package edu.psu.unifiedapi.capstone.utils;

import edu.psu.unifiedapi.capstone.CapstoneException;
import edu.psu.unifiedapi.capstone.CapstoneWrapper;
import edu.psu.unifiedapi.database.Database;

import java.sql.SQLException;

public class CapstoneDbUtils {

	public static String getTeamId(String username) {

		String teamId = null;

		try {
			teamId = Database.getCapstoneData(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (teamId == null) {
			CapstoneWrapper cap = new CapstoneWrapper(username, "/AgileTask/EGetMyTeamID", null);
			ResponseTeamId response;
			try {
				response = cap.capCall(ResponseTeamId.class);
			} catch (CapstoneException e) {
				throw new RuntimeException(e.getMessage());
			}

			teamId = response.response.teamId;

			try {
				Database.insertCapstoneData(teamId, username);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return teamId;
	}

}
