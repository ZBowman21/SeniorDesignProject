package edu.psu.unifiedapi.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.http.auth.AUTH;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Authenticate implements RequestHandler<AuthArgs, String> {

    private Connection connection;

    public Authenticate() {
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
    public String handleRequest(AuthArgs aA, Context context) {

        String decryptedPass = null;

        try {
            ResultSet resultSet = queryDatabase(aA.username, aA.service);
            if (resultSet.next()) {
                String username = resultSet.getString(1);
                byte[] encryptedPass = resultSet.getBytes(2);
                byte[] pwHash = resultSet.getBytes(3);


                String decryptedPassTmp = Encryption.decrypt(encryptedPass, aA.passphrase);
                byte[] pwHashCheck = Hashing.hash(decryptedPassTmp);

                if (Arrays.equals(pwHash, pwHashCheck)) {
                    decryptedPass = decryptedPassTmp;
                }

            } else {
                context.getLogger().log("User '" + aA.username + "' not found in the database");
            }
        } catch (SQLException e) {
            context.getLogger().log("Error accessing database: " + e.getMessage());
        } catch(GeneralSecurityException e) {
            context.getLogger().log("Error decrypting password: " + e.getMessage());
        }

        return decryptedPass;
    }

    private ResultSet queryDatabase(String username, String service) throws SQLException {
        String queryString = "select username, password, hash from plain_credentials where username = ? AND service = ?";
        PreparedStatement statement = getConnection().prepareStatement(queryString);
        statement.setString(1, username);
        statement.setString(2, service);
        statement.execute();
        return statement.getResultSet();
    }

}
