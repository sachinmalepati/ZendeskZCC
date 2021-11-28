import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class ZendeskZCC {

    //todo: move to common place
    static String username = "sachin.malepati@gmail.com";
    static String token = "r8E9wvEXLdlaMQIJ1arMjo0oijjByUpG2qnBSkNh";

    static String initURl = "https://zccsachin.zendesk.com/api/v2/tickets.json?per_page=2";
    static String ticketURl = "https://zccsachin.zendesk.com/api/v2/tickets/";
    static String next_page = "";
    static String previous_page = "";
    static Integer count= null;


    static JSONObject getJson(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return json;
    }

    static TicketsWrapper getTickets(String link) throws Exception {
        URL url = new URL(link);

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

    static Ticket getTicketDetails(Integer ticketID) throws Exception {
        URL url = new URL(ticketURl+ String.valueOf(ticketID) +".json");

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
            JSONObject jsonObject = getJson(response.toString());
            Ticket ticket = getTicketObj(jsonObject.getString("ticket"));

            return ticket;

        } catch (FileNotFoundException e) {
            System.out.println("Invalid ticket number!");
        }catch (Exception e) {
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
        System.out.println("Welcome to Zendesk Ticket Viewer!\n");

        while (true){
            System.out.println("\nEnter 1 for all tickets");
            System.out.println("Enter 2 for previous page");
            System.out.println("Enter 3 for next page");
            System.out.println("Enter 4 to view a ticket");
            System.out.println("Enter 5 to quit");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice){
                case 1: getAllTickets(initURl); break;
                case 2: getPrevPage(); break;
                case 3: getNextPage(); break;
                case 4: getTicketDetails(); break;
                case 5: System.out.println("Bye!"); return;
                default: System.out.println("Not a valid input. Retry!");
            }
        }
    }

    private static void getTicketDetails() throws Exception {
        System.out.println("\nEnter ticket ID");

        Scanner scanner = new Scanner(System.in);
        try{
            Integer ticketId = scanner.nextInt();

            Ticket ticket = getTicketDetails(ticketId);

            if(ticket!=null){
                ArrayList<Ticket> al = new ArrayList<>();
                al.add(ticket);
                printTickets(al);
            }
        }catch (Exception e){
            System.out.println("Invalid ticket number!");
        }
    }

    private static void getNextPage() throws Exception {
        if(next_page.trim().equals("")){
            if(previous_page.trim().equals("")){
                System.out.println("No tickets have been fetched yet.");
            }else{
                System.out.println("No next page. This is the last page.");
            }
        }else{
            getAllTickets(next_page);
        }
    }

    private static void getPrevPage() throws Exception {
        if(previous_page.trim().equals("")){
            if(next_page.trim().equals("")){
                System.out.println("No tickets have been fetched yet.");
            }else {
                System.out.println("No previous page. This is the first page.");
            }
        }else{
            getAllTickets(previous_page);
        }
    }

    private static void getAllTickets(String url) throws Exception {
        TicketsWrapper ticketsWrapper = getTickets(url);

        if(ticketsWrapper!=null){
            next_page = ticketsWrapper.next_page;
            previous_page = ticketsWrapper.previous_page;
            count = ticketsWrapper.count;
            printTickets(ticketsWrapper.listOfTickets);
        }else{
            System.out.println("Error fetching tickets");
        }
    }

    private static void printTickets(ArrayList<Ticket> listOfTickets) {
        for(Ticket t: listOfTickets){
            System.out.println("\nTicket ID: "+t.id);
            System.out.println("Priority: "+t.priority);
            System.out.println("Status: "+t.status);
            System.out.println("Subject: "+t.subject);
            System.out.println("Description: "+t.description);
        }
    }
}
