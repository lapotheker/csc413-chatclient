package chatclient.scenes;

import chatclient.UserSession;
import chatclient.listviewcells.MessageModel;
import chatclient.listviewcells.MessageListCell;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.websocket.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatRoom implements Screen {
    private final Stage stage;
    private ListView<String> usersView;
    private ObservableList<String> usersList;
    private ListView<MessageModel> messagesView;
    private ObservableList<MessageModel> messages;
    private TextField messageField;
    private Button sendButton;
    private Button leaveButton;
    private Label roomNameLabel;

    private Session webSocketSession;
    private int currentRoomId;
    private String currentRoomName;
    private Gson gson = new Gson();
    private HttpClient httpClient = HttpClient.newHttpClient();

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
        BorderPane root = new BorderPane();

        HBox topSection = new HBox();
        topSection.setPadding(new Insets(10));
        topSection.setSpacing(10);

        roomNameLabel = new Label(currentRoomName);
        roomNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        leaveButton = new Button("Leave Room");
        leaveButton.setOnAction(e -> {
            try {
                if (webSocketSession != null && webSocketSession.isOpen()) {
                    webSocketSession.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Servers serverScene = new Servers(stage);
            stage.getScene().setRoot(serverScene.build());
        });

        topSection.getChildren().addAll(roomNameLabel, spacer, leaveButton);

        HBox centerSection = new HBox(10);
        centerSection.setPadding(new Insets(10));

        VBox messagesSection = new VBox(10);
        messagesSection.setPrefWidth(400);

        Label messagesLabel = new Label("Messages:");
        messagesLabel.setStyle("-fx-font-weight: bold;");

        messagesView.setPrefHeight(300);
        messagesSection.getChildren().addAll(messagesLabel, messagesView);

        VBox usersSection = new VBox(10);
        usersSection.setPrefWidth(150);

        Label usersLabel = new Label("Users:");
        usersLabel.setStyle("-fx-font-weight: bold;");

        usersView.setPrefHeight(300);
        usersSection.getChildren().addAll(usersLabel, usersView);

        centerSection.getChildren().addAll(messagesSection, usersSection);

        HBox bottomSection = new HBox(10);
        bottomSection.setPadding(new Insets(10));

        messageField = new TextField();
        messageField.setPromptText("Type your message...");
        HBox.setHgrow(messageField, Priority.ALWAYS);

        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        messageField.setOnAction(e -> sendMessage());

        bottomSection.getChildren().addAll(messageField, sendButton);

        root.setTop(topSection);
        root.setCenter(centerSection);
        root.setBottom(bottomSection);

        return root;
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
        this.currentRoomId = roomId;
        this.currentRoomName = roomName;

        try {
            loadMessages();
            loadUsersPane();

            UserSession userSession = UserSession.getInstance();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String wsUrl = String.format("wss://csc413.ajsouza.com/chat?room_id=%d&user_id=%d&token=%s",
                    roomId, userId, userSession.getToken());

            Endpoint endpoint = new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    webSocketSession = session;
                    session.addMessageHandler(new MessageHandler.Whole<String>() {
                        @Override
                        public void onMessage(String message) {
                            handleWebSocketMessage(message);
                        }
                    });
                }

                @Override
                public void onError(Session session, Throwable throwable) {
                    throwable.printStackTrace();
                }
            };

            container.connectToServer(endpoint, URI.create(wsUrl));

            Thread.sleep(1000);

            return webSocketSession != null && webSocketSession.isOpen();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleWebSocketMessage(String message) {
        try {
            JsonObject messageObj = gson.fromJson(message, JsonObject.class);
            String event = messageObj.get("event").getAsString();

            Platform.runLater(() -> {
                switch (event) {
                    case "message":
                        String text = messageObj.get("message").getAsString();
                        String username = messageObj.get("username").getAsString();
                        MessageModel msg = new MessageModel(text, username, "now", null);
                        messages.add(msg);
                        break;
                    case "join":
                        loadUsersPane();
                        break;
                    case "leave":
                        loadUsersPane();
                        break;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty() || webSocketSession == null || !webSocketSession.isOpen()) {
            return;
        }

        try {
            UserSession userSession = UserSession.getInstance();
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("event", "message");
            messageObj.addProperty("room_id", currentRoomId);
            messageObj.addProperty("user_id", userSession.getUserId());
            messageObj.addProperty("username", userSession.getUsername());
            messageObj.addProperty("message", text);

            webSocketSession.getBasicRemote().sendText(gson.toJson(messageObj));
            messageField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * helper function to send an API request
     * to get a list of users for a given
     * chat room.
     */
    public void loadUsersPane() {
        try {
            UserSession userSession = UserSession.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://csc413.ajsouza.com/api/rooms/" + currentRoomId + "/users"))
                    .header("Authorization", "Bearer " + userSession.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            String status = responseJson.get("status").getAsString();

            if ("success".equals(status)) {
                JsonArray usersArray = responseJson.getAsJsonArray("users");
                Platform.runLater(() -> {
                    usersList.clear();
                    for (int i = 0; i < usersArray.size(); i++) {
                        JsonObject userObj = usersArray.get(i).getAsJsonObject();
                        String username = userObj.get("username").getAsString();
                        usersList.add(username);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * helper function to send an API request
     * to get a list of messages for a given
     * chat room.
     */
    public void loadMessages() {
        try {
            UserSession userSession = UserSession.getInstance();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://csc413.ajsouza.com/api/rooms/" + currentRoomId + "/messages"))
                    .header("Authorization", "Bearer " + userSession.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            String status = responseJson.get("status").getAsString();

            if ("success".equals(status)) {
                JsonArray messagesArray = responseJson.getAsJsonArray("messages");
                Platform.runLater(() -> {
                    messages.clear();
                    for (int i = 0; i < messagesArray.size(); i++) {
                        JsonObject msgObj = messagesArray.get(i).getAsJsonObject();
                        String text = msgObj.get("text").getAsString();
                        String username = msgObj.get("username").getAsString();
                        String timestamp = msgObj.get("created_at").getAsString();
                        MessageModel msg = new MessageModel(text, username, timestamp, null);
                        messages.add(msg);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}