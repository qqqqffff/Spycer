package com.apollor.spycer.user;

import com.apollor.spycer.database.Database;

public class User {
    public User(
            String userName
    ){
        this.userName = userName;
    }
    public String userName;
    private static User loggedInUser;
    private static final User guestUser = new User("Guest");

    //TODO: implement me
    private static void checkSessionToken(Token session){

    }
    //TODO: implement me
    private static void attemptLogin(String username, String password_plaintext){
        loggedInUser = Database.getUser(username, password_plaintext);
    }

    public static User getUser(){
        if(loggedInUser == null){
            return guestUser;
        }
        return loggedInUser;
    }
}
