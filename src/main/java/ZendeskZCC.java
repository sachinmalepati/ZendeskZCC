import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class ZendeskZCC {

    //todo: move to common place
    static String username = "sachin.malepati@gmail.com";
    static String token = "r8E9wvEXLdlaMQIJ1arMjo0oijjByUpG2qnBSkNh";

    static JSONObject getJson(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return json;
    }

    static TicketsWrapper getTickets() throws Exception {
        URL url = new URL("https://zccsachin.zendesk.com/api/v2/tickets.json?per_page=2");

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

            JSONObject json = getJson(response.toString());
            TicketsWrapper ticketsWrapper = parseJson(json);

            return ticketsWrapper;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static TicketsWrapper parseJson(JSONObject json) throws JSONException {
        ArrayList<Ticket> listdata = new ArrayList<>();
        JSONArray jArray = (JSONArray)json.get("tickets");
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(getTicketObj(jArray.getString(i)));
            }
        }
        TicketsWrapper ticketsWrapper = new TicketsWrapper(json.get("next_page"), json.get("previous_page"), json.get("count"), listdata);
        return ticketsWrapper;
    }

    private static Ticket getTicketObj(String jsonString) throws JSONException {
        JSONObject jsonObject = getJson(jsonString);

        return new Ticket(jsonObject.get("created_at"), jsonObject.get("description"), jsonObject.get("id"), jsonObject.get("priority"), jsonObject.get("status"), jsonObject.get("subject"), jsonObject.get("type"));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        try {
            TicketsWrapper ticketsWrapper = getTickets();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
