package com.apollor.spycer.database;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.CustomHttpClientResponseHandler;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class Session {
    private Session(){

    }
    public Session(
            String sessionId,
            String userId,
            Double locationLat,
            Double locationLong,
            String sessionStart,
            String sessionEnd,
            String emailAddress
    ){
        this.sessionId = sessionId;
        this.userId = userId;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.emailAddress = emailAddress;
    }

    public String sessionId;
    public String userId;
    public Double locationLat;
    public Double locationLong;
    public String sessionStart;
    public String sessionEnd;
    public String emailAddress;

    protected static Session get(String userid) throws IOException {
        String urlString = Application.baseApplicationLink +"/session/" + userid;
        AtomicReference<Session> session = new AtomicReference<>(null);

        HttpGet get = new HttpGet(urlString);

        CloseableHttpClient client = HttpClients.createDefault();
        client.execute(get, rsp -> {
            if(rsp.getCode() == 200){
                String r = new String(rsp.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                session.set(parseSession(r));
            }
            return rsp;
        });
        client.close();

        return session.get();
    }

    protected static String post(Session session) throws IOException {
        String urlString = Application.baseApplicationLink + "/session";
        HttpPost post = new HttpPost(urlString);

        final String jsonString = createJsonString(session);
        final StringEntity entity = new StringEntity(jsonString);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(post, new CustomHttpClientResponseHandler())) {

            System.out.println(response.getCode());
        }

        return null;
    }

    protected static String delete(String id) throws IOException{
        String urlString = Application.baseApplicationLink + "/session/" + id;
        HttpDelete delete = new HttpDelete(urlString);

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(delete, new CustomHttpClientResponseHandler())) {

            System.out.println(response.getCode());
        }

        return null;
    }

    private static Session parseSession(String jsonSession){
        Session session = new Session();

        for(String i : jsonSession.split("\n")){
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
                case "session_id" -> session.sessionId = value;
                case "userid" -> session.userId = value;
                case "location_lat" -> {
                    try{
                        session.locationLat = Double.parseDouble(value);
                    }catch(NullPointerException | NumberFormatException ignored){
                        session.locationLat = null;
                    }
                }
                case "location_long" -> {
                    try{
                        session.locationLong = Double.parseDouble(value);
                    }catch(NullPointerException | NumberFormatException ignored){
                        session.locationLong = null;
                    }
                }
                case "session_start" -> session.sessionStart = value;
                case "session_end" -> session.sessionEnd = value;
                case "email_address" -> session.emailAddress = value;
            }
        }

        return session;
    }

    private static String createJsonString(Session session){
        return "{\n" +
                "\t\"session_id\": \"" + session.sessionId + "\"," +
                "\t\"userid\": \"" + session.userId + "\",\n" +
                "\t\"location_lat\": " + session.locationLat + ",\n" +
                "\t\"location_long\": " + session.locationLong + ",\n" +
                "\t\"session_start\": \"" + session.sessionStart + "\",\n" +
                "\t\"session_end\": \"" + session.sessionEnd + "\",\n" +
                "\t\"email_address\": \"" + session.emailAddress + "\"" +
                "}";
    }
}
