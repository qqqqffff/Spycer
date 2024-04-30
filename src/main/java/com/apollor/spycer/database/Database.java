package com.apollor.spycer.database;

import java.io.IOException;

public class Database {
    public static User getUser(String email) throws IOException {
        return User.get(email);
    }
    public static String postUser(User user) throws IOException {
        return User.post(user);
    }
    public static String putUser(User user) throws IOException {
        return User.put(user);
    }
    public static String deleteUser(String userid) throws IOException {
        return User.delete(userid);
    }

    public static Session getSession(String userid) throws IOException{
        return Session.get(userid);
    }
    public static String postSession(Session session) throws IOException {
        return Session.post(session);
    }
    public static String deleteSession(String sessionId) throws IOException{
        return Session.delete(sessionId);
    }

}
