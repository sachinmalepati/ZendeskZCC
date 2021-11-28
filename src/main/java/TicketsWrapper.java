import org.json.JSONObject;

import java.util.ArrayList;

public class TicketsWrapper {
    public String next_page;
    public String previous_page;
    public Integer count;
    public ArrayList<Ticket> listOfTickets;

    public TicketsWrapper(Object next_page, Object previous_page, Object count, ArrayList<Ticket> listdata) {
        this.next_page = JSONObject.NULL.equals(next_page)?"" : (String) next_page;
        this.previous_page = JSONObject.NULL.equals(previous_page)?"" : (String) previous_page;
        this.count = (Integer) count;
        this.listOfTickets = listdata;
    }
}
