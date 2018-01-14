package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DelLinkedAccount implements RequestHandler<DelLinkedAccountArgs, Boolean> {

	private Connection connection;

	public DelLinkedAccount() {
		getConnection();
	}

	private Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				//TODO log this
			}

			String url = System.getenv("DB_URL");
			String user = System.getenv("DB_USER");
			String pass = System.getenv("DB_PASS");

			try {
				connection = DriverManager.getConnection(url, user, pass);
			} catch (SQLException e) {
				//TODO log this
			}
		}

		return connection;
	}

	@Override
	public Boolean handleRequest(DelLinkedAccountArgs args, Context context) {
		try {
			return insertDatabase(args.userId, args.service);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	private boolean insertDatabase(String userId, String service) throws SQLException {
		String queryString = "DELETE FROM plain_credentials WHERE id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		return statement.executeUpdate() > 0;
	}

}