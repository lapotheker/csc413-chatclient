package chatclient.scenes;

import chatclient.UserSession;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
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

public class Login implements Screen {
    private Stage mainStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button resetButton;
    private Gson gson = new Gson();
    private HttpClient httpClient = HttpClient.newHttpClient();

    public Login(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Builds login Scene
     * Should allow users to do the following:
     *  -- enter username
     *  -- enter password (passwords should be hidden)
     *  -- button to try to log in
     *  -- button to reset fields
     */
    public Pane build() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Chat Client Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300);

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        loginButton = new Button("Login");
        loginButton.setOnAction(this::handleLogin);
        loginButton.setStyle("-fx-background-color: #3aed4f; -fx-text-fill: white;");

        resetButton = new Button("Reset");
        resetButton.setOnAction(this::handleReset);
        resetButton.setStyle("-fx-background-color: #636363; -fx-text-fill: white;");

        buttonBox.getChildren().addAll(loginButton, resetButton);

        root.getChildren().addAll(titleLabel, usernameLabel, usernameField,
                passwordLabel, passwordField, buttonBox);

        return root;
    }

    /**
     * attempts to log user in with a given username
     * and password via the login REST API endpoint.
     * If login success should transtion to the Server Scene
     * @param ev
     */
    private void handleLogin(ActionEvent ev) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showPopUp("Please enter both username and password");
            return;
        }

        loginButton.setDisable(true);
        loginButton.setText("Logging in...");

        JsonObject loginData = new JsonObject();
        loginData.addProperty("username", username);
        loginData.addProperty("password", password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://csc413.ajsouza.com/users/login/app"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(loginData)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
            String status = responseJson.get("status").getAsString();

            if ("success".equals(status)) {
                UserSession userSession = UserSession.getInstance();
                userSession.initSession(
                        responseJson.get("username").getAsString(),
                        responseJson.get("user_id").getAsInt(),
                        responseJson.get("token").getAsString()
                );

                Platform.runLater(() -> {
                    Servers serverScene = new Servers(mainStage);
                    mainStage.getScene().setRoot(serverScene.build());
                });
            } else {
                String message = responseJson.get("message").getAsString();
                showPopUp("Login failed: " + message);
            }

        } catch (Exception e) {
            showPopUp("Error connecting to server: " + e.getMessage());
        } finally {
            Platform.runLater(() -> {
                loginButton.setDisable(false);
                loginButton.setText("Login");
            });
        }
    }

    /**
     * resets text fields on button press
     * @param ev
     */
    private void handleReset(ActionEvent ev) {
        usernameField.clear();
        passwordField.clear();
        usernameField.requestFocus();
    }

    /**
     * OPTIONAL
     * but can be used to show pop-up windows for
     * error events during login
     * @param message the message to show in pup
     */
    private void showPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}