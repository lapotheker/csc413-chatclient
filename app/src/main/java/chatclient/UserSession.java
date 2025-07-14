package chatclient;

import java.util.Objects;

public class UserSession {
    private static UserSession session = null;
    private String username;
    private int userId;
    private String token;

    private UserSession(){}

    /**
     * returns an instance of UserSession. If not initialized, one is
     * created
     * @return a singleton instance of UserSession
     */
    public static UserSession getInstance(){
        if (session == null) {
            session = new UserSession();
        }
        return session;
    }

    /**
     *
     * @param username username of user
     * @param userId user's userId
     * @param token user's API token.
     */
    public void initSession(String username, int userId, String token){
        this.username = username;
        this.userId = userId;
        this.token = token;
    }

    /**
     * Get a users username from session
     * @return users username
     */
    public String getUsername() {
        if(Objects.isNull(username) || username.length() == 0){
            throw new IllegalStateException("User session it not correctly initialized :: username is missing");
        }
        return username;
    }

    /**
     * Get a users id from session
     * @return users id
     */
    public int getUserId() {
        if(this.userId <= 0){
            throw new IllegalStateException("User session it not correctly initialized :: userId is missing");
        }
        return userId;
    }

    /**
     * Get users API Token
     * @return users token to access API
     */
    public String getToken() {
        if(Objects.isNull(token)){
            throw new IllegalStateException("User session it not correctly initialized :: token is missing");
        }
        return token;
    }

    /**
     * used to clear current user session
     */
    public void clearSession(){
        this.userId = -1;
        this.token = null;
        this.username = null;
        UserSession.session = null;
    }
}