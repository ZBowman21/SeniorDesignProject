package edu.psu.unifiedapi.database;

import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.auth.Encryption;
import edu.psu.unifiedapi.auth.Hashing;

import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.Arrays;

/**
 * @author mthwate
 */
public class Database {

	private static Connection connection;

	public static void init() {
		getConnection();
	}

	private static Connection getConnection() {
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

	public static boolean insertPlainCredentials(String userId, String passphrase, String service, String username, String password) throws SQLException, GeneralSecurityException {
		byte[] encryptedPassword = Encryption.encrypt(password, passphrase);
		byte[] hashedPassword = Hashing.hash(password);

		PreparedStatement statement = getConnection().prepareStatement("INSERT INTO plain_credentials VALUES (?, ?, ?, ?, ?)");

		statement.setString(1, userId);
		statement.setString(2, service);
		statement.setString(3, username);
		statement.setBytes(4, encryptedPassword);
		statement.setBytes(5, hashedPassword);

		return statement.executeUpdate() > 0;
	}

	public static boolean insertTokenCredentials(String userId, String service, String token) throws SQLException {
		PreparedStatement statement = getConnection().prepareStatement("INSERT INTO token_credentials VALUES (?, ?, ?)");

		statement.setString(1, userId);
		statement.setString(2, service);
		statement.setString(3, token);

		return statement.executeUpdate() > 0;
	}

	public static boolean deletePlainCredentials(String userId, String service) throws SQLException {
		String queryString = "DELETE FROM plain_credentials WHERE id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		return statement.executeUpdate() > 0;
	}

	public static boolean deleteTokenCredentials(String userId, String service) throws SQLException {
		String queryString = "DELETE FROM token_credentials WHERE id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		return statement.executeUpdate() > 0;
	}

	public static Credentials getPlainCredentials(String userId, String passphrase, String service) throws SQLException, GeneralSecurityException {

		Credentials creds = null;

		String queryString = "select id, password, hash from plain_credentials where id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			String username = result.getString(1);
			byte[] encryptedPass = result.getBytes(2);
			byte[] pwHash = result.getBytes(3);


			String decryptedPass = Encryption.decrypt(encryptedPass, passphrase);
			byte[] pwHashCheck = Hashing.hash(decryptedPass);

			if (Arrays.equals(pwHash, pwHashCheck)) {
				creds = new Credentials(username, decryptedPass);
			}
		}

		return creds;
	}

	public static String getTokenCredentials(String userId, String service) throws SQLException {

		String token = null;

		String queryString = "select token from token_credentials where username = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			token = result.getString(1);
		}

		return token;
	}

}
