import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class, HttpsURLConnection.class, ZendeskZCC.class })
@PowerMockIgnore("jdk.internal.reflect.*")
public class ZendeskZCCTest extends TestCase {

    public static String jsonString = "{\"name\":\"test\"}";
    public static String emptyTickets = "{\"tickets\":[{\"id\":1, \"created_at\":\"2021-11-28T00:03:51Z\",\"type\":\"incident\",\"subject\":\"Sample\",\"description\":\"Hi\",\"priority\":\"normal\",\"status\":\"open\"}],\"next_page\":\"test next\",\"previous_page\":\"test prev\",\"count\":2}";
    public static String ticketDeatils = "{\"ticket\":{\"id\":1, \"created_at\":\"2021-11-28T00:03:51Z\",\"type\":\"incident\",\"subject\":\"Sample\",\"description\":\"Hi\",\"priority\":\"normal\",\"status\":\"open\"}}";

    @Mock
    private HttpsURLConnection connection;

    @Mock
    private InputStream inputStream;

    @Mock
    private InputStreamReader inputStreamReader;

    @Mock
    private ZendeskZCC zendeskZCC;

//    @Mock
//    private BufferedReader bufferedReader;

    @Test
    public void testGetJson() throws JSONException {
        JSONObject jsonObject = ZendeskZCC.getJson(jsonString);

        assert jsonObject.getString("name").equals("test");
    }

    @Test
    public void testGetAllTicketsException() throws Exception {
        TicketsWrapper ticketsWrapper = new TicketsWrapper("next", "prev", 5, new ArrayList<Ticket>());

        URL u = PowerMockito.mock(URL.class);
        String url = "https://www.google.com";
        PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
        HttpsURLConnection huc = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(connection);

        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);

        PowerMockito.when(huc.getInputStream()).thenReturn(inputStream);
        PowerMockito.whenNew(InputStreamReader.class).withArguments(inputStream).thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Matchers.any()).thenReturn(bufferedReader);

        PowerMockito.when(bufferedReader.readLine()).thenThrow(IndexOutOfBoundsException.class);

        ZendeskZCC.getAllTickets(url);

        assert ZendeskZCC.count==null;
        assert ZendeskZCC.next_page.equals("");
        assert ZendeskZCC.previous_page.equals("");
    }

    @Test
    public void testGetAllTickets() throws Exception {
        TicketsWrapper ticketsWrapper = new TicketsWrapper("next", "prev", 5, new ArrayList<Ticket>());

        URL u = PowerMockito.mock(URL.class);
        String url = "https://www.google.com";
        PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
        HttpsURLConnection huc = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(connection);

        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);

        PowerMockito.when(huc.getInputStream()).thenReturn(inputStream);
        PowerMockito.whenNew(InputStreamReader.class).withArguments(inputStream).thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Matchers.any()).thenReturn(bufferedReader);

        PowerMockito.when(bufferedReader.readLine()).thenReturn(emptyTickets).thenReturn(null);

        ZendeskZCC.getAllTickets(url);

        assert ZendeskZCC.count==2;
        assert ZendeskZCC.next_page.equals("test next");
        assert ZendeskZCC.previous_page.equals("test prev");
    }

    @Test
    public void testGetTicketDetails() throws Exception {
        TicketsWrapper ticketsWrapper = new TicketsWrapper("next", "prev", 5, new ArrayList<Ticket>());

        URL u = PowerMockito.mock(URL.class);
        String url = "https://www.google.com";
        PowerMockito.whenNew(URL.class).withArguments(Matchers.anyString()).thenReturn(u);
        HttpsURLConnection huc = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(connection);

        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);

        PowerMockito.when(huc.getInputStream()).thenReturn(inputStream);
        PowerMockito.whenNew(InputStreamReader.class).withArguments(inputStream).thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Matchers.any()).thenReturn(bufferedReader);

        PowerMockito.when(bufferedReader.readLine()).thenReturn(ticketDeatils).thenReturn(null);

        String input = "5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ZendeskZCC.getTicketDetails();

        assert ZendeskZCC.count==2;
        assert ZendeskZCC.next_page.equals("test next");
        assert ZendeskZCC.previous_page.equals("test prev");
    }

    @Test
    public void testGetTicketDetailsExcption() throws Exception {
        TicketsWrapper ticketsWrapper = new TicketsWrapper("next", "prev", 5, new ArrayList<Ticket>());

        URL u = PowerMockito.mock(URL.class);
        String url = "https://www.google.com";
        PowerMockito.whenNew(URL.class).withArguments(Matchers.anyString()).thenReturn(u);
        HttpsURLConnection huc = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(connection);

        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);

        PowerMockito.when(huc.getInputStream()).thenReturn(inputStream);
        PowerMockito.whenNew(InputStreamReader.class).withArguments(inputStream).thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Matchers.any()).thenReturn(bufferedReader);

        PowerMockito.when(bufferedReader.readLine()).thenThrow(FileNotFoundException.class);

        String input = "5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ZendeskZCC.getTicketDetails();

        assert ZendeskZCC.count==2;
        assert ZendeskZCC.next_page.equals("test next");
        assert ZendeskZCC.previous_page.equals("test prev");
    }

    @Test
    public void testGetTicketDetailsExcption2() throws Exception {
        TicketsWrapper ticketsWrapper = new TicketsWrapper("next", "prev", 5, new ArrayList<Ticket>());

        URL u = PowerMockito.mock(URL.class);
        String url = "https://www.google.com";
        PowerMockito.whenNew(URL.class).withArguments(Matchers.anyString()).thenReturn(u);
        HttpsURLConnection huc = PowerMockito.mock(HttpsURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(connection);

        BufferedReader bufferedReader = PowerMockito.mock(BufferedReader.class);

        PowerMockito.when(huc.getInputStream()).thenReturn(inputStream);
        PowerMockito.whenNew(InputStreamReader.class).withArguments(inputStream).thenReturn(inputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Matchers.any()).thenReturn(bufferedReader);

        PowerMockito.when(bufferedReader.readLine()).thenThrow(Exception.class);

        String input = "5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ZendeskZCC.getTicketDetails();

        assert ZendeskZCC.count==2;
        assert ZendeskZCC.next_page.equals("test next");
        assert ZendeskZCC.previous_page.equals("test prev");
    }

    @Test
    public void getNextTest() throws Exception {
        ZendeskZCC.next_page="";
        ZendeskZCC.previous_page="";

        ZendeskZCC.getNextPage();
        ZendeskZCC.getPrevPage();

        ZendeskZCC.previous_page="s";
        ZendeskZCC.getNextPage();

        ZendeskZCC.next_page="s";
        ZendeskZCC.previous_page="";
        ZendeskZCC.getPrevPage();

    }

    @Test
    public void testMain() throws Exception {
        String input = "5";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ZendeskZCC.main(null);
    }

}