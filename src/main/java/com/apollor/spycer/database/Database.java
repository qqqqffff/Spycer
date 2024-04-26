package com.apollor.spycer.database;

//TODO: update with facade methods
public class Database {
    public static User getUser(String username, String password_plaintext){
        return new User(
                null,
                username,
                null
        );
    }
}
