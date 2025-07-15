package chatclient.listviewcells;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ChatRoomModel {
    private final SimpleStringProperty roomName = new SimpleStringProperty();
    private final SimpleIntegerProperty ownerId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty roomId = new SimpleIntegerProperty();

    public ChatRoomModel(int roomId, String roomName, int ownerId) {
        this.roomId.set(roomId);
        this.roomName.set(roomName);
        this.ownerId.set(ownerId);
    }

    public String getRoomName() {
        return roomName.get();
    }

    public SimpleStringProperty roomNameProperty() {
        return roomName;
    }

    public int getOwnerId() {
        return ownerId.get();
    }

    public SimpleIntegerProperty ownerIdProperty() {
        return ownerId;
    }

    public int getRoomId() {
        return roomId.get();
    }

    public SimpleIntegerProperty roomIdProperty() {
        return roomId;
    }
}
