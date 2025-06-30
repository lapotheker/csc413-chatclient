package chatclient.responses;

import java.util.List;

public class ListUsers {
    private String state;
    private String message;
    private Room room;
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public Room getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }



}
