package edu.psu.unifiedapi.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sun.mail.iap.ByteArray;

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

    @Override
    public String handleRequest(AuthArgs aA, Context context) {

        //set up connection
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            context.getLogger().log(e.toString());
        }

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            context.getLogger().log(e.toString());
        }

        //lookup database
        ResultSet resultSet;
        byte[] pw = null;
        String dPW = null;
        byte[] pwHash = null;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select username, password, hash from plain_credentials where username = ? AND service = ?");
            statement.setString(1, aA.username);
            statement.setString(2, aA.service);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                byte[] un = resultSet.getBytes(0);
                pw = resultSet.getBytes(1);
                pwHash = resultSet.getBytes(2);
                context.getLogger().log(un.toString());
            }
        } catch (SQLException e) {
            context.getLogger().log(e.toString());
        }

        //decrypt
        //kelp me matt
        try {
            dPW = Encryption.decrypt(pw, aA.passphrase);
        }
        catch(GeneralSecurityException e) {
            context.getLogger().log(e.toString());
        }

        //hash
        //n kelp me cheese
        try{
            pw = Hashing.hash(dPW);
        }
        catch (NoSuchAlgorithmException e){
            context.getLogger().log(e.toString());
        }

        context.getLogger().log(dPW);

        if (Arrays.equals(pw, pwHash))
            return dPW;
        else
            return null;
    }
}
