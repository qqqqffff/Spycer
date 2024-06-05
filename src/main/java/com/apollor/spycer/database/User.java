package com.apollor.spycer.database;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.CustomHttpClientResponseHandler;
import javafx.scene.image.Image;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
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
            String emailAddress,
            boolean mfaPhone
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
        this.mfaPhone = mfaPhone;
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
    public boolean mfaPhone;


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

    protected static Image getUserPFP(String userid) throws IOException{
        String urlString = Application.baseApplicationLink + "/users/user-pfp/" + userid;
        AtomicReference<Image> image = new AtomicReference<>(null);
        HttpGet get = new HttpGet(urlString);

        CloseableHttpClient client = HttpClients.createDefault();
        client.execute(get, rsp -> {
            if(rsp.getCode() == 200){
                byte[] b = rsp.getEntity().getContent().readAllBytes();
                byte[] f = Base64.getDecoder().decode(new String(b, StandardCharsets.UTF_8).replace("\"","").getBytes(StandardCharsets.UTF_8));
                image.set(new Image(new ByteArrayInputStream(f)));
            }
            return rsp;
        });
        client.close();

        return image.get();
    }

    protected static String post(User user) throws IOException{
        String urlString = Application.baseApplicationLink + "/users";
        HttpPost post = new HttpPost(urlString);

        final String jsonString = createJsonString(user);
        final StringEntity entity = new StringEntity(jsonString);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(post, new CustomHttpClientResponseHandler())) {

            return String.valueOf(response.getCode());
        }
    }

    protected static String postUserPFP(File file) throws IOException {
        String urlString = Application.baseApplicationLink + "/users/user-pfp";
        HttpPost post = new HttpPost(urlString);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.EXTENDED);
        builder.addBinaryBody("file", file);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client.execute(post, new CustomHttpClientResponseHandler())){
            return String.valueOf(response.getCode());
        }
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

            return String.valueOf(response.getCode());
        }
    }

    protected static String delete(String id) throws IOException {
        String urlString = Application.baseApplicationLink + "/users/" + id;
        HttpDelete delete = new HttpDelete(urlString);

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(delete, new CustomHttpClientResponseHandler())) {

            return String.valueOf(response.getCode());
        }
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
                case "mfa_phone" -> user.mfaPhone = Boolean.parseBoolean(value);
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
                "\t\"email_address\": \"" + user.emailAddress + "\",\n" +
                "\t\"mfa_phone\": " + user.mfaPhone + "\n" +
                "}";
    }
}
