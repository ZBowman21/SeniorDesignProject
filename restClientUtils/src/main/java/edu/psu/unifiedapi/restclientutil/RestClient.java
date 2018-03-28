package edu.psu.unifiedapi.restclientutil;

//import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/* Inspiration from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

public class RestClient {
    private String url;
    private String param;
    private final String USER_AGENT = "Mozilla/5.0";
    private URL obj;
    private HttpURLConnection con;
    private int responseCode;

    public RestClient(String baseurl, String param) {
        this.url = baseurl + "?" + param;

        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String GetRequest() {
        try {
            con.setRequestMethod("GET");
            return ReadResponse();
        } catch(Exception e) {
            return "Unable to connect to URL";
        }
    }
    public String PostRequest(String param) {
        try {
            //add reuqest header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(param);
            wr.close();

            responseCode = con.getResponseCode();
            return ReadResponse();
        } catch (Exception e) {
            return "Unable to connect to URL";
        }
    }

    private String ReadResponse() {
        String response = "";

        try {
            responseCode = con.getResponseCode();
            if(responseCode/100 == 2) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    buffer.append(inputLine);
                }
                in.close();
                response = buffer.toString();
            } else {
                response = "Received Response Code " + responseCode;
            }
        } catch(Exception e){
            response = "Error occurec in reading response" + e.getMessage();
        }
        return response;
    }
}
