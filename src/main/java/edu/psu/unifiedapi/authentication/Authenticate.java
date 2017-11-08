package edu.psu.unifiedapi.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String pw = null;
        String pwHash = null;
        try {
            PreparedStatement statement = connection.prepareStatement("select something from something where username = ?");
            statement.setString(1, aA.username);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                pw = resultSet.getString(1);
                pwHash = resultSet.getString(2);
                context.getLogger().log(resultSet.getString(0) + " | " + resultSet.getString(1) + " | " + resultSet.getString(2));
            }
        } catch (SQLException e) {
            context.getLogger().log(e.toString());
        }

        //decrypt
        //kelp me matt
        pw.toString();
        pwHash.toString();
        aA.passphrase.toString();

        //hash
        //n kelp me cheese

        if (pw == pwHash)
            return pw;
        else
            return null;
    }
}
