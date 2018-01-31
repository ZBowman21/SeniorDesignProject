package edu.psu.unifiedapi.database;

import edu.psu.unifiedapi.auth.Credentials;
import edu.psu.unifiedapi.auth.Encryption;
import edu.psu.unifiedapi.auth.Hashing;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	private static boolean insert(String table, Object ... values) throws SQLException {
		StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO ? VALUES (");
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("?");
		}
		sb.append(")");

		PreparedStatement statement = getConnection().prepareStatement(sb.toString());

		statement.setString(1, table);

		for (int i = 0; i < values.length; i++) {
			statement.setObject(i+2, values[i]);
		}

		return statement.executeUpdate() > 0;
	}

	public static boolean insertPlainCredentials(String userId, String passphrase, String service, String username, String password) throws SQLException, GeneralSecurityException {
		byte[] encryptedPassword = Encryption.encrypt(password, passphrase);
		byte[] hashedPassword = Hashing.hash(password);
		return insert("plain_credentials", userId, service, username, encryptedPassword, hashedPassword);
	}

	public static boolean insertTokenCredentials(String userId, String service, String token) throws SQLException {
		return insert("token_credentials", userId, service, token);
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

		String queryString = "select username, password, hash from plain_credentials where username = ? AND service = ?";
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

		String queryString = "select token from token_credentials where id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			token = result.getString(1);
		}

		return token;
	}

	private static boolean update(String table, String token, String userId, String service) throws SQLException {
		StringBuilder sb = new StringBuilder();

		sb.append("UPDATE token_credentials SET token = ? WHERE id = ? AND service = ?");

		PreparedStatement statement = getConnection().prepareStatement(sb.toString());

//		statement.setString(1, table);
		statement.setString(1, token);
		statement.setString(2, userId);
		statement.setString(3, service);

		return statement.executeUpdate() > 0;
	}

	public static boolean updateTokenCredentials(String userId, String service, String token) throws SQLException {
		return update("token_credentials", token, userId, service);
	}
}
