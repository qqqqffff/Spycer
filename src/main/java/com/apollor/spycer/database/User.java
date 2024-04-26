package com.apollor.spycer.database;

import java.util.UUID;

public class User {
    public User(
            UUID userId,
            String displayName,
            String emailAddress
    ){
        this.userId = userId;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
    }
    public UUID userId;
    public String displayName;
    public String emailAddress;
    private static User loggedInUser;
    private static final User guestUser = new User(null,"Guest", null);

    //TODO: implement me
    private static void checkSessionToken(Session session){

    }
    //TODO: implement me
    private static void attemptLogin(String email, String password_plaintext){
        loggedInUser = Database.getUser(email, password_plaintext);
    }

    public static User getUser(){
        if(loggedInUser == null){
            return guestUser;
        }
        return loggedInUser;
    }
}
