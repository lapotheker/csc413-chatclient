package chatclient.responses;

import java.text.DateFormat;
import java.util.Date;

public class WebSocketMessage {
    private String event;
    private String message;
    private int user_id;
    private int room_id;
    private String username;
    private String timestamp;

    public String getTimestamp() {
       return this.timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getUsername() {
        return username;
    }
}
