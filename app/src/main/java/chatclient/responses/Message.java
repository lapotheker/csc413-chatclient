package chatclient.responses;

public class Message {
    private int id;
    private String text;
    private int room_id;
    private String created_at;
    private String username;

    public Message(int id, String text, int room_id, String created_at, String username) {
        this.id = id;
        this.text = text;
        this.room_id = room_id;
        this.created_at = created_at;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return this.username;
    }
}
