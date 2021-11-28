import org.json.JSONObject;
import org.junit.Test;

public class TicketTest {
    @Test
    public void constructorTest(){
        Ticket t = new Ticket(JSONObject.NULL,JSONObject.NULL, 1, JSONObject.NULL, JSONObject.NULL, JSONObject.NULL, JSONObject.NULL);
    }
}
