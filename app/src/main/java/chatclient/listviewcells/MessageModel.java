package chatclient.listviewcells;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.util.Objects;

public class MessageModel {
    private final StringProperty author = new SimpleStringProperty();
    private final StringProperty text   = new SimpleStringProperty();
    private final StringProperty timestamp = new SimpleStringProperty();
    private final ObjectProperty<Image> avatar = new SimpleObjectProperty<>();
    private final static Image DEFAULT_AVATAR = new Image("icons/user.png");
    public MessageModel(String author, String text, String timestamp, Image avatar) {
        this.author.set(author);
        this.text.set(text);
        this.timestamp.set(timestamp);
        this.avatar.set(Objects.requireNonNullElse(avatar, DEFAULT_AVATAR));
    }
    public String getAuthor()        { return author.get(); }
    public String getText()          { return text.get(); }
    public String getTimestamp() { return timestamp.get(); }
    public Image getAvatar()         { return avatar.get(); }
}
