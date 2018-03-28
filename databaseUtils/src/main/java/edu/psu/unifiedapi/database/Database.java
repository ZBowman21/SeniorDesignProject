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

	public static boolean insertPlainCredentials(String userId, String encryptionKey, String service, String username, String password) throws SQLException, GeneralSecurityException {
		byte[] encryptedPassword = Encryption.encrypt(password, encryptionKey);
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

	public static boolean existsPlainCredentials(String userId, String service) throws SQLException {
		String queryString = "select * from plain_credentials where id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public static boolean existsTokenCredentials(String userId, String service) throws SQLException {
		String queryString = "select * from token_credentials where id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();
		return result.next();
	}

	public static Credentials getPlainCredentials(String userId, String encryptionKey, String service) throws SQLException, GeneralSecurityException {

		Credentials creds = null;

		String queryString = "select username, password, hash from plain_credentials where id = ? AND service = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		statement.setString(2, service);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			String username = result.getString(1);
			byte[] encryptedPass = result.getBytes(2);
			byte[] pwHash = result.getBytes(3);


			String decryptedPass = Encryption.decrypt(encryptedPass, encryptionKey);
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

		//statement.setString(1, table);
		statement.setString(1, token);
		statement.setString(2, userId);
		statement.setString(3, service);

		return statement.executeUpdate() > 0;
	}

	public static boolean updateTokenCredentials(String userId, String service, String token) throws SQLException {
		return update("token_credentials", token, userId, service);
	}

	private static boolean updateCapstone(String teamid, String userId) throws SQLException{
		StringBuilder sb = new StringBuilder();

		sb.append("UPDATE user_capstone SET team_id = ? WHERE id = ?");

		PreparedStatement statement = getConnection().prepareStatement(sb.toString());

		statement.setString(1, teamid);
		statement.setString(2, userId);

		return statement.executeUpdate() > 0;
	}

	public static boolean updateCapstoneData(String teamid, String userId) throws SQLException{
		return updateCapstone(teamid, userId);
	}

	public static String getCapstoneData(String userId) throws SQLException{
		String token = null;

		String queryString = "select team_id from user_capstone where id = ?";
		PreparedStatement statement = getConnection().prepareStatement(queryString);
		statement.setString(1, userId);
		ResultSet result = statement.executeQuery();

		if(result.next()){
			token = result.getString(1);
		}

		return token;
	}

	public static boolean insertCapstoneData(String teamid, String userId) throws SQLException{
		PreparedStatement statement = getConnection().prepareStatement("INSERT INTO user_capstone VALUES ( ?, ?)");

		statement.setString(1, userId);
		statement.setString(2, teamid);

		return statement.executeUpdate() > 0;
	}

	public static boolean existsCapstoneData(String userid) throws SQLException{
		PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM user_capstone WHERE id = ?");
		statement.setString(1, userid);
		ResultSet result = statement.executeQuery();

		return result.next();
	}
}
