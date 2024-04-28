package com.apollor.spycer.utils;

import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.Session;
import com.apollor.spycer.database.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.UUID;

public class SessionHandler {
    private static User loggedInUser;
    private static final User guestUser = new User(
            null,
            "Guest",
            false,
            false,
            false,
            null,
            null,
            null,
            null
    );

    //TODO: implement me
    public static void checkSessionToken(Session session){

    }
    //TODO: implement me
    public static void attemptLogin(String email, String password_plaintext) throws IOException {
        User tempUser = Database.getUser(email);
    }

    public static User createUser(String email, String displayName, String password_plaintext) throws IOException {
        MessageDigest digest;
        String hexPasswordHash;
        String hexSalt;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();

            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            digest.update(salt);

            byte[] encodedHash = digest.digest(password_plaintext.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for(byte b : encodedHash){
                builder.append(String.format("%02x", b));
            }

            hexPasswordHash = builder.toString();

            builder = new StringBuilder();
            for(byte b : salt){
                builder.append(String.format("%02x", b));
            }
            hexSalt = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        User user = new User(
                UUID.randomUUID().toString(),
                displayName,
                false,
                false,
                false,
                "2024-04-27T16:43:46.578882-04:00",
                hexPasswordHash,
                hexSalt,
                email
        );

//        Database.postUser(user);
        return user;
    }

    public static User getLoggedInUser(){
        if(loggedInUser == null){
            return guestUser;
        }
        return loggedInUser;
    }
}
