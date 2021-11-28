import org.json.JSONObject;
import org.junit.Test;

public class TicketTest {
    @Test
    public void constructorTest(){
        Ticket t = new Ticket(JSONObject.NULL,JSONObject.NULL, 1, JSONObject.NULL, JSONObject.NULL, JSONObject.NULL, JSONObject.NULL);

        assert t.id == 1;
        assert t.priority.equals("");
        assert t.description.equals("");
        assert t.type.equals("");
        assert t.subject.equals("");
        assert t.status.equals("");
        assert t.created_at.equals("");
    }

    @Test
    public void constructorTest2(){
        Ticket t = new Ticket("12324","test desc", 2, "prio", "status", "subject", "type");

        assert t.created_at.equals("12324");
        assert t.description.equals("test desc");
        assert t.id == 2;
        assert t.priority.equals("prio");
        assert t.status.equals("status");
        assert t.type.equals("type");
        assert t.subject.equals("subject");
    }
}
