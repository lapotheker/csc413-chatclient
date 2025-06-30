package chatclient.responses;

public class CreateRoom {
    private String status;
    private String message;
    private Room room;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Room getRoom() {
        return room;
    }
}
