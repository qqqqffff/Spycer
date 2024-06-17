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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Household {
    private Household(){

    }
    public Household(
            String householdId,
            String householdName,
            String userId,
            String privilege,
            boolean request
    ){
        this.householdId = householdId;
        this.householdName = householdName;
        this.userId = userId;
        this.privilege = privilege;
        this.request = request;
    }

    public String householdId;
    public String householdName;
    public String userId;
    public String privilege;
    public boolean request;

    protected static Household getByUID(String userid) throws IOException {
        String urlString = Application.baseApplicationLink + "/households/" + userid;
        AtomicReference<Household> household = new AtomicReference<>(null);

        HttpGet get = new HttpGet(urlString);

        CloseableHttpClient client = HttpClients.createDefault();
        client.execute(get, rsp -> {
            if (rsp.getCode() == 200) {
                String r = new String(rsp.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                household.set(parseHousehold(r));
            }
            return rsp;
        });
        client.close();

        return household.get();
    }

    protected static Household[] getByID(String id) throws IOException{
        String urlString = Application.baseApplicationLink + "/households/" + id;
        AtomicReference<Household[]> households = new AtomicReference<>(null);

        HttpGet get = new HttpGet(urlString);

        CloseableHttpClient client = HttpClients.createDefault();
        client.execute(get, rsp -> {
            if(rsp.getCode() == 200){
                String r = new String(rsp.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                households.set(parseSetHouseholds(r));
            }
            return rsp;
        });
        client.close();

        return households.get();
    }

    protected static String post(Household household) throws IOException{
        String urlString = Application.baseApplicationLink + "/households";
        HttpPost post = new HttpPost(urlString);

        final String jsonString = createJsonString(household);
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


    protected static String put(Household household) throws IOException {
        String urlString = Application.baseApplicationLink + "/households";
        HttpPut put = new HttpPut(urlString);

        final String jsonString = createJsonString(household);
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
        String urlString = Application.baseApplicationLink + "/households/" + id;
        HttpDelete delete = new HttpDelete(urlString);

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = (CloseableHttpResponse) client
                    .execute(delete, new CustomHttpClientResponseHandler())) {

            return String.valueOf(response.getCode());
        }
    }

    private static Household parseHousehold(String jsonHousehold){
        Household household = new Household();

        for(String i : jsonHousehold.split("\n")){
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
                case "household_id": {
                    household.householdId = value;
                }
                case "household_name": {
                    household.householdName = value;
                }
                case "userid": {
                    household.userId = value;
                }
                case "privilege": {
                    household.privilege = value;
                }
                case "request": {
                    household.request = Boolean.parseBoolean(value);
                }
            }
        }

        return household;
    }

    private static Household[] parseSetHouseholds(String jsonHousehold){
        List<Household> householdList = new ArrayList<>();
        for(String i : jsonHousehold.split("[{]")){
            if(i.contains("\"")) householdList.add(parseHousehold(i));
        }
        if(householdList.size() == 0) return null;
        Household[] households = new Household[householdList.size()];
        for(int i = 0; i < households.length; i++){
            households[i] = householdList.get(i);
        }
        return households;
    }

    private static String createJsonString(Household household){
        return "{\n" +
                "\t\"household_id\": \"" + household.householdId + "\",\n" +
                "\t\"household_name\": \"" + household.householdName + "\",\n" +
                "\t\"userid\": \"" + household.userId + "\",\n" +
                "\t\"privilege\": \"" + household.privilege + "\",\n" +
                "\t\"request\": " + household.request + "\n" +
                "}";
    }
}
