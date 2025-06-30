package chatclient.scenes;


import chatclient.listviewcells.ChatRoomModel;
import chatclient.listviewcells.ServerListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Servers implements Screen {
    private final Stage mainStage;
    private ListView<ChatRoomModel> serversView;
    private ObservableList<ChatRoomModel> servers;
    

    public Servers(Stage s) {
        this.mainStage = s;
        this.servers = FXCollections.observableArrayList();
        this.serversView = new ListView<>(this.servers);
        this.serversView.setCellFactory(lv -> new ServerListCell(lv));
        
    }

    /**
     * builds the Server List Screen
     * This screen should allow users to do the following:
     *  -- Show list of chat rooms to join
     *  -- refresh/fill list of servers window from remote API.
     *  -- logout (also transitions to login screen)
     *  -- make a new room (this can be pop-up or a hidden form that shows when needed.)
     *  -- join a chat room (this also requires connecting a WS BEFORE switch Scenes)
     */
    public Pane build() {
        
        return new Pane();
    }

    /**
     * Executed when refresh server list button is pressed
     * @param ev action event from button pressed
     */
    private void refreshServerList(ActionEvent ev) {

    }

    /**
     * Excuted when join room button is clicked
     * make sure to only switch to the chatroom scene
     * when the connection to the Web Socket is successful
     * @param ev
     */
    private void connectToRoom(ActionEvent ev) {

    }

    /**
     * sends a API request to make new chat room
     * execute when button is pressed
     * @param ev
     */
    private void handleCreateRoom(ActionEvent ev) {
        
    }
}
