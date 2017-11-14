package edu.psu.unifiedapi.testing;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sun.mail.iap.ByteArray;
import edu.psu.unifiedapi.authentication.AuthArgs;
import edu.psu.unifiedapi.authentication.Encryption;
import edu.psu.unifiedapi.authentication.Hashing;
import org.apache.http.auth.AUTH;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class TestAuth {//implements RequestHandler<AuthArgs> {
    private static Connection connection;

    public static void main(String [] args){
        AuthArgs aA = new AuthArgs();

        aA.service = "webmail";
        aA.passphrase = "what the fuck";
        aA.username = "zob5056";
        connection = null;
        String output = handleRequest(aA);
        System.out.println(output);
    }

    //@Override
    static public String handleRequest(AuthArgs aA) {

        //set up connection
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            //context.getLogger().log(e.toString());
        }

        String url = "jdbc:postgresql://unifiedapi.ckrjtyihoqf3.us-east-1.rds.amazonaws.com:5432/unifiedapi"; //System.getenv("DB_URL");
        String user = "master"; //System.getenv("DB_USER");
        String pass = "SkyIsTheLimit!"; //System.getenv("DB_PASS");

        try {
            //context.getLogger().log("Timeout " + DriverManager.getLoginTimeout());
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            //context.getLogger().log(e.toString());
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
                //context.getLogger().log(un.toString());
                int i = 0;
            }
        } catch (SQLException e) {
            //context.getLogger().log(e.toString());
            int i = 0;
        }

        //decrypt
        //kelp me matt
        try {
            System.out.println(pw + " " + aA.passphrase);
            dPW = Encryption.decrypt(pw, aA.passphrase);
            int i = 0;
        }
        catch(GeneralSecurityException e) {
            //context.getLogger().log(e.toString());
            int i = 0;
        }

        //hash
        //n kelp me cheese
        try{
            pw = Hashing.hash(dPW);
        }
        catch (NoSuchAlgorithmException e){
            //context.getLogger().log(e.toString());
        }

        //context.getLogger().log(dPW);

        if (Arrays.equals(pw, pwHash))
            return dPW;
        else
            return null;
    }
}
