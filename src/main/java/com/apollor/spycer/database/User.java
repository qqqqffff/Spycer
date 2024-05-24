package com.apollor.spycer.database;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.CustomHttpClientResponseHandler;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


public class User {
    private User(){

    }
    public User(
            String userId,
            String displayName,
            boolean mfaEmail,
            boolean mfaApp,
            boolean verified,
            String createdDate,
            String hashPW,
            String hashSalt,
            String emailAddress
    ){
        this.userId = userId;
        this.displayName = displayName;
        this.mfaEmail = mfaEmail;
        this.mfaApp = mfaApp;
        this.verified = verified;
        this.createdDate = createdDate;
        this.hashPW = hashPW;
        this.hashSalt = hashSalt;
        this.emailAddress = emailAddress;
    }

    public String userId;
    public String displayName;
    public String emailAddress;
    public boolean mfaEmail;
    public boolean mfaApp;
    public boolean verified;
    public String createdDate;
    public String hashPW;
    public String hashSalt;


    protected static User get(String email) throws IOException {
        String urlString = Application.baseApplicationLink + "/users/" + email;
        AtomicReference<User> user = new AtomicReference<>(null);

        HttpGet get = new HttpGet(urlString);

        CloseableHttpClient client = HttpClients.createDefault();
        client.execute(get, rsp -> {
            if (rsp.getCode() == 200) {
                String r = new String(rsp.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                user.set(parseUser(r));
            }
            return rsp;
        });
        client.close();

        return user.get();
    }

    protected static String post(User user) throws IOException{
        String urlString = Application.baseApplicationLink + "/users";
        HttpPost post = new HttpPost(urlString);

        final String jsonString = createJsonString(user);
        final StringEntity entity = new StringEntity(jsonString);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        int code;

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(post, new CustomHttpClientResponseHandler())) {

            code = response.getCode();
        }

        return String.valueOf(code);
    }


    protected static String put(User user) throws IOException {
        String urlString = Application.baseApplicationLink + "/users";
        HttpPut put = new HttpPut(urlString);

        final String jsonString = createJsonString(user);
        final StringEntity entity = new StringEntity(jsonString);
        put.setEntity(entity);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(put, new CustomHttpClientResponseHandler())) {

            System.out.println(response.getCode());
        }

        return null;
    }

    protected static String delete(String id) throws IOException {
        String urlString = Application.baseApplicationLink + "/users/" + id;
        HttpDelete delete = new HttpDelete(urlString);

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(delete, new CustomHttpClientResponseHandler())) {

            System.out.println(response.getCode());
        }

        return null;
    }

    private static User parseUser(String jsonUser){
        User user = new User();

        for(String i : jsonUser.split("\n")){
            if(!i.contains("\"")) continue;

            String key = i.substring(i.indexOf("\"") + 1, i.indexOf(":") - 1);

            int finalLength = i.length() - 1;
            int initialLength = i.indexOf(":") + 2;
            String value = i.substring(initialLength);
            if(!value.contains(",")){
                finalLength++;
            }
            if(value.contains("\"")){
                initialLength++;
                finalLength--;
            }
            value = i.substring(initialLength, finalLength);

            switch(key){
                case "userid" -> user.userId = value;
                case "display_name" -> user.displayName = value;
                case "mfa_email" -> user.mfaEmail = Boolean.parseBoolean(value);
                case "mfa_app" -> user.mfaApp = Boolean.parseBoolean(value);
                case "verified" -> user.verified = Boolean.parseBoolean(value);
                case "created_date" -> user.createdDate = value;
                case "hash_pw" -> user.hashPW = value;
                case "hash_salt" -> user.hashSalt = value;
                case "email_address" -> user.emailAddress = value;
            }
        }

        return user;
    }

    private static String createJsonString(User user){
        return "{\n" +
                "\t\"userid\": \"" + user.userId + "\",\n" +
                "\t\"display_name\": \"" + user.displayName + "\",\n" +
                "\t\"mfa_email\": " + user.mfaEmail + ",\n" +
                "\t\"mfa_app\": " + user.mfaApp + ",\n" +
                "\t\"verified\": " + user.verified + ",\n" +
                "\t\"created_date\": \"" + user.createdDate + "\",\n" +
                "\t\"hash_pw\": \"" + user.hashPW + "\",\n" +
                "\t\"hash_salt\": \"" + user.hashSalt + "\",\n" +
                "\t\"email_address\": \"" + user.emailAddress + "\"\n" +
                "}";
    }
}
