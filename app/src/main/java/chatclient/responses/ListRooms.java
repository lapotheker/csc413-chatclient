package chatclient.responses;

import java.util.List;

public class ListRooms {
    private String status;
    private String message;
    private int rooms_count;
    private List<Room> rooms;

    public List<Room> getRooms() {
        return rooms;
    }

    public int getRooms_count() {
        return rooms_count;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

}
