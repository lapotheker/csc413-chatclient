package chatclient.scenes;

import chatclient.UserSession;
import chatclient.responses.Room;
import chatclient.listviewcells.ChatRoomModel;
import chatclient.listviewcells.ServerListCell;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Servers implements Screen {
    private final Stage mainStage;
    private ListView<ChatRoomModel> serversView;
    private ObservableList<ChatRoomModel> servers;
    private Button refreshButton;
    private Button joinButton;
    private Button createButton;
    private Button logoutButton;
    private TextField roomNameField;
    private Gson gson = new Gson();
    private HttpClient httpClient = HttpClient.newHttpClient();

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
        BorderPane root = new BorderPane();

        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Available Chat Rooms");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER);

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(this::refreshServerList);

        joinButton = new Button("Join Room");
        joinButton.setOnAction(this::connectToRoom);
        joinButton.setDisable(true); // Disabled until room is selected

        createButton = new Button("Create Room");
        createButton.setOnAction(this::handleCreateRoom);

        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            UserSession.getInstance().clearSession();
            Login loginScene = new Login(mainStage);
            mainStage.getScene().setRoot(loginScene.build());
        });

        buttonRow.getChildren().addAll(refreshButton, joinButton, createButton, logoutButton);

        HBox createRoomSection = new HBox(10);
        createRoomSection.setAlignment(Pos.CENTER);
        roomNameField = new TextField();
        roomNameField.setPromptText("Enter room name");
        roomNameField.setMaxWidth(200);

        topSection.getChildren().addAll(titleLabel, buttonRow, createRoomSection);

        serversView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            joinButton.setDisable(newSelection == null);
        });

        root.setTop(topSection);
        root.setCenter(serversView);

        Platform.runLater(this::refreshServerList);

        return root;
    }

    /**
     * Executed when refresh server list button is pressed
     * @param ev action event from button pressed
     */
    private void refreshServerList(ActionEvent ev) {
        try {
            UserSession userSession = UserSession.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://csc413.ajsouza.com/api/rooms"))
                    .header("Authorization", "Bearer " + userSession.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            String status = responseJson.get("status").getAsString();

            if ("success".equals(status)) {
                JsonArray roomsArray = responseJson.getAsJsonArray("rooms");
                Platform.runLater(() -> {
                    servers.clear();
                    for (int i = 0; i < roomsArray.size(); i++) {
                        JsonObject roomObj = roomsArray.get(i).getAsJsonObject();
                        ChatRoomModel room = new ChatRoomModel(
                                roomObj.get("room_name").getAsString(),
                                roomObj.get("owner_id").getAsInt(),
                                roomObj.get("user_count").getAsInt()
                        );
                        servers.add(room);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshServerList() {
        refreshServerList(null);
    }

    /**
     * Excuted when join room button is clicked
     * make sure to only switch to the chatroom scene
     * when the connection to the Web Socket is successful
     * @param ev
     */
    private void connectToRoom(ActionEvent ev) {
        ChatRoomModel selectedRoom = serversView.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) return;

        UserSession userSession = UserSession.getInstance();
        ChatRoom chatRoom = new ChatRoom(mainStage);
        boolean connected = chatRoom.initWebsocket(selectedRoom.getRoomId(), userSession.getUserId(), selectedRoom.getRoomName());

        if (connected) {
            Platform.runLater(() -> {
                mainStage.getScene().setRoot(chatRoom.build());
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setContentText("Failed to connect to chat room");
            alert.showAndWait();
        }
    }

    /**
     * sends a API request to make new chat room
     * execute when button is pressed
     * @param ev
     */
    private void handleCreateRoom(ActionEvent ev) {
        String roomName = roomNameField.getText().trim();
        if (roomName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Please enter a room name");
            alert.showAndWait();
            return;
        }

        JsonObject roomData = new JsonObject();
        roomData.addProperty("room_name", roomName);

        try {
            UserSession userSession = UserSession.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://csc413.ajsouza.com/api/rooms"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + userSession.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(roomData)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            String status = responseJson.get("status").getAsString();

            if ("success".equals(status)) {
                roomNameField.clear();
                refreshServerList();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Room created successfully!");
                alert.showAndWait();
            } else {
                String message = responseJson.get("message").getAsString();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Failed to create room: " + message);
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error creating room: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
