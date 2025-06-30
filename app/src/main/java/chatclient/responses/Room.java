package chatclient.responses;

public class Room {
    private int id;
    private String room_name;
    private int owner_id;
    private int active;
    private String created_at;


    private int user_count;

    /**
     * @return the active
     */
    public int getActive() {
        return active;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the owner_id
     */
    public int getOwner_id() {
        return owner_id;
    }

    /**
     * @return the created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @return the room_name
     */
    public String getRoom_name() {
        return room_name;
    }

    /**
     *
     * @return number of users in room
     */
    public int getUser_count() {
        return user_count;
    }

    @Override
    public String toString() {
        return "Room ID : " + this.id + "\tOwner ID:" + this.owner_id + "\tRoom name: " + this.room_name;
    }
}
