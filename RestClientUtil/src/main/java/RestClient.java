import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

/* Inspiration from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

public class RestClient {
    private String url;
    private String param;
    private final String USER_AGENT = "Mozilla/5.0";
    private URL obj;
    private HttpsURLConnection con;
    private int responseCode;

    public RestClient(String baseurl, String param) {
        this.url = baseurl + "&" + param;

        try {
            obj = new URL(url);
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        } catch(Exception e) {
        }
    }

    public String GetRequest() {
        try {
            con.setRequestMethod("GET");
            return ReadResponse();
        } catch(Exception e) {
        }
        return "";
    }
    public String PostRequest(String param) {
        try {
            //add reuqest header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(param);
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            return ReadResponse();
        } catch (Exception e) {

        }
        return "";
    }

    private String ReadResponse() {
        String inputLine = "";

        try {
            responseCode = con.getResponseCode();
            if(responseCode/100 == 2) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                inputLine = "Received Response Code " + responseCode;
            }
        } catch(Exception e) {
        }
        return inputLine;
    }
}
