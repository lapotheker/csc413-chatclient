package chatclient;


import chatclient.scenes.ChatRoom;
import chatclient.scenes.Login;
import chatclient.scenes.Screen;
import chatclient.scenes.Servers;

import java.util.Map;
import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static final Map<String, Screen> screens = new HashMap<>();
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)  {
        /*
         * sets app wide css, can be changed or use custom CSS theme,
         * check the app/src/resources/css for more themes.
        */
        Application.setUserAgentStylesheet("css/cupertino-dark.css");
        // create Login Scene
        Login loginScreen = new Login(primaryStage);
        // create Server Screen Scene
        Servers serverScreen = new Servers(primaryStage);
        // create Chat Room Scene
        ChatRoom cr =  new ChatRoom(primaryStage);
        /**
         * add scenese to Map to easing switching.
         */
        screens.put("login",loginScreen);
        screens.put("servers",serverScreen);
        screens.put("chatroom", cr);        Scene login = new Scene(screens.get("login").build());
        primaryStage.setTitle("CSC 413 Chat App");
        primaryStage.setScene(login);
        primaryStage.show();


    }

    public static Screen getScreen(String screenName){
        return App.screens.get(screenName);
    }

   
}
