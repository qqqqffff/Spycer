package com.apollor.spycer.database;

import com.apollor.spycer.user.User;

//TODO: update with facade methods
public class Database {
    public static User getUser(String username, String password_plaintext){
        return new User(
                username
        );
    }
}
