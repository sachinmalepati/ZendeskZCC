import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class ZendeskZCC {

    //todo: move to common place
    static String username = "sachin.malepati@gmail.com";
    static String token = "r8E9wvEXLdlaMQIJ1arMjo0oijjByUpG2qnBSkNh";

    static void getTickets() throws Exception {
        URL url = new URL("https://zccsachin.zendesk.com/api/v2/tickets.json");

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        String encoded = Base64.getEncoder().encodeToString((username+"/token:"+token).getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoded);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");

        try {
            //Getting the response code
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;

            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            //Close the scanner
            in.close();

            JSONObject json = new JSONObject(response.toString());

            json.get("next_page");
            json.get("tickets");
            json.get("count");
            json.get("previous_page");

            System.out.println(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        getTickets();
    }
}
