import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

public class TicketsWrapperTest {

    @Test
    public void constructorTest(){
        TicketsWrapper t = new TicketsWrapper(JSONObject.NULL,JSONObject.NULL, 1, new ArrayList<>());
        assert t.count == 1;
        assert t.next_page.equals("");
        assert t.previous_page.equals("");
        assert t.listOfTickets.size()==0;
    }

    @Test
    public void constructorTest2(){
        TicketsWrapper t = new TicketsWrapper("next page","previous page", 1, new ArrayList<>());
        assert t.count == 1;
        assert t.next_page.equals("next page");
        assert t.previous_page.equals("previous page");
        assert t.listOfTickets.size()==0;
    }

}