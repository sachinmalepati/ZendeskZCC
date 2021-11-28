import org.json.JSONObject;

public class Ticket {
    public String created_at;
    public String description;
    public Integer id;
    public String priority;
    public String status;
    public String subject;
    public String type;

    public Ticket(Object created_at, Object description, Object id, Object priority, Object status, Object subject, Object type) {
        this.created_at = JSONObject.NULL.equals(created_at)?"" : (String) created_at;
        this.description = JSONObject.NULL.equals(description)?"" : (String) description;
        this.id = (Integer) id;
        this.priority = JSONObject.NULL.equals(priority)?"" : (String) priority;
        this.status = JSONObject.NULL.equals(status)?"" : (String) status;
        this.subject = JSONObject.NULL.equals(subject)?"" : (String) subject;
        this.type = JSONObject.NULL.equals(type)?"" : (String) type;
    }
}
