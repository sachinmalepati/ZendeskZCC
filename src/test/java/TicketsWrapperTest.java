import junit.framework.TestCase;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

public class TicketsWrapperTest {

    @Test
    public void constructorTest(){
        TicketsWrapper t = new TicketsWrapper(JSONObject.NULL,JSONObject.NULL, 1, new ArrayList<>());
        assert t.count == 1;
    }

}