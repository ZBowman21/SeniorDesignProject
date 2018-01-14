package edu.psu.unifiedapi.account;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.authentication.Encryption;
import edu.psu.unifiedapi.authentication.Hashing;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddLinkedAccount implements RequestHandler<AddLinkedAccountArgs, Boolean> {

	private Connection connection;

	public AddLinkedAccount() {
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
	public Boolean handleRequest(AddLinkedAccountArgs args, Context context) {
		try {
			byte[] encryptedPassword = Encryption.encrypt(args.password, args.passphrase);
			byte[] hashedPassword = Hashing.hash(args.password);
			return insertDatabase(args.userId, args.service, args.username, encryptedPassword, hashedPassword);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	private boolean insertDatabase(String userId, String service, String username, byte[] encryptedPassword, byte[] hashedPassword) throws SQLException {
		String queryString = "INSERT INTO plain_credentials VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		statement.setString(3, username);
		statement.setBytes(4, encryptedPassword);
		statement.setBytes(5, hashedPassword);
		return statement.executeUpdate() > 0;
	}

}