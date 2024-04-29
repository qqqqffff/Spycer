package com.apollor.spycer.database;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//TODO: update with facade methods
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


}
