package chatclient.scenes;


import chatclient.listviewcells.MessageModel;
import chatclient.listviewcells.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class ChatRoom implements Screen {
    private final Stage stage;
    private ListView<String> usersView;
    private ObservableList<String> usersList;
    private ListView<MessageModel> messagesView;
    private ObservableList<MessageModel> messages;
   

    public ChatRoom(Stage primaryStage) {
        this.stage = primaryStage;
        this.messages = FXCollections.observableArrayList();
        messagesView = new ListView<>(this.messages);
        this.messagesView.setCellFactory(lv -> new MessageListCell(lv));
        this.usersList = FXCollections.observableArrayList();
        usersView = new ListView<>(usersList);
    }

    /**
     * builds chat room scene.
     * Should allow the user to do the following:
     *  -- see all messages for chat room
     *  -- see all users in chat room
     *  -- a text field to type in a message
     *  -- a button to send a message
     *  -- a button to leave the room.
     */
    public Pane build() {
        return new Pane();
    }

    /**
     * Connect a WS to the server to recieve and send messages
     * It is recommended this function be called BEFORE switching to the 
     * chatroom scene.
     * @param roomId id of room to join
     * @param userId userId of logged in user joining the room
     * @param roomName name of room you are joining
     * @return a boolean whether or not the connection was successful.
     */
    public boolean initWebsocket(int roomId, int userId, String roomName) {
        return false;
    }

    /**
     * helper function to send an API request
     * to get a list of users for a given
     * chat room.
     */
    public void loadUsersPane() {
        
    }

    /**
     * helper function to send an API request
     * to get a list of messages for a given
     * chat room.
     */
    public void loadMessages() {
      
    }
}
