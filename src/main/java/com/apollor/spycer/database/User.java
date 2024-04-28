package com.apollor.spycer.database;

import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


public class User {
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
        String urlString = "http://localhost:8080/users/" + email;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(urlString);
        get.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(get, resp -> {
            return resp;
        });
//        URL url = new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        connection.setConnectTimeout(5000);
//        connection.setReadTimeout(5000);
//
//        int status = connection.getResponseCode();
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//
//        Gson gson = new Gson();
//        System.out.println(status + "," + content);
        return null;
    }

    protected static String post(User user) throws IOException{
        String urlString = "http://localhost:8080/users";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        Gson gson = new Gson();

//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("userid", user.userId);
//        parameters.put("mfa_email", String.valueOf(user.mfaEmail));
//        parameters.put("mfa_app", String.valueOf(user.mfaApp));
//        parameters.put("verified", String.valueOf(user.verified));
//        parameters.put("display_name", user.displayName);
//        parameters.put("created_date", user.createdDate.toString());
//        parameters.put("hash_pw", user.hashPW);
//        parameters.put("hash_salt", user.hashSalt);
//        parameters.put("email_address", user.emailAddress);

        //TODO: fix gson's shitty json formatter
        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(gson.toJson(user, User.class));
        out.flush();
        out.close();

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

//        assert status == 200;
//        assert !content.isEmpty();
        System.out.println(status + "," + content);

        return null;
    }


    protected static String put(UUID id, User user){
        return null;
    }

    protected static String delete(UUID id){
        return null;
    }


}
