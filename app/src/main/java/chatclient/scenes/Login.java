package chatclient.scenes;


import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Login implements Screen {
    private Stage mainStage;

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
        return new Pane();
    }

    /**
     * attempts to log user in with a given username
     * and password via the login REST API endpoint.
     * If login success should transtion to the Server Scene
     * @param ev
     */
    private void handleLogin(ActionEvent ev) {

    }

    /**
     * resets text fields on button press
     * @param ev
     */
    private void handleReset(ActionEvent ev) {
       
    }

    /**
     * OPTIONAL
     * but can be used to show pop-up windows for
     * error events during login
     * @param message the message to show in pup
     */
    private void showPopUp(String message) {

    }
}
