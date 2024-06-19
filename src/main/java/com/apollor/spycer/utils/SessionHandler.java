package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SessionHandler {
    private static User loggedInUser;
    private static Session userSession;
    private static final List<String> userRecipes;
    private static Household userHousehold;
    private static final File sessionToken = new File(Application.datadir + "/session_token.json");
    private static final User guestUser = new User(
            null,
            "Guest",
            false,
            false,
            false,
            null,
            null,
            null,
            null,
            false
    );

    static {
        userRecipes = new ArrayList<>();
        if(!sessionToken.exists()) {
            try {
                if(!sessionToken.createNewFile()){
                    throw new RuntimeException("failed to create session token");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkSessionToken() throws IOException {
        Session session = null;
        try{
            session = readLocalSessionToken();
        }catch(IOException ignored){}

        if(session == null) return false;
        Session db_session = Database.getSession(session.userId);
        User user = Database.getUser(session.emailAddress);
        Household household = Database.getHouseholdByUID(session.userId);

        if(user == null ||
                db_session == null ||
                !user.userId.equals(db_session.userId) ||
                !db_session.userId.equals(session.userId)
        ) {
            invalidateSession();
            return false;
        }

        userSession = session;
        loggedInUser = user;
        userHousehold = household;
        return true;
    }

    public static boolean attemptLogin(String email, String password_plaintext, boolean stayLoggedIn) throws IOException {
        User tempUser = Database.getUser(email);
        if(tempUser == null) return false;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] salt = new byte[tempUser.hashSalt.length() / 2];
            String hexSalt = tempUser.hashSalt;
            for(int i = 0; i < salt.length; i++){
                int index = i * 2;

                int val = Integer.parseInt(hexSalt.substring(index, index + 2), 16);
                salt[i] = (byte) val;
            }

            digest.update(salt);
            byte[] encodedHash = digest.digest(password_plaintext.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for(byte b: encodedHash){
                sb.append(String.format("%02x", b));
            }

            boolean success = sb.toString().equals(tempUser.hashPW);

            if(success){
                loggedInUser = tempUser;
                String[] recipes = RecipeHandler.introspectRecipes(loggedInUser.userId);
                if(stayLoggedIn) {
                    Session uSess = Database.getSession(tempUser.userId);

                    Session session;

                    if (uSess == null) {
                        session = createNewSession(tempUser.userId, tempUser.emailAddress, true);
                        Database.postSession(session);
                        userSession = session;
                    } else {
                        userSession = uSess;
                    }

                    createLocalSessionToken(userSession, recipes);
                }

                return true;
            }

            return false;

        }catch(NoSuchAlgorithmException ignored){

        }
        return false;
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

        String i = new Timestamp(System.currentTimeMillis()).toInstant().toString();
        String zid = ZoneId.systemDefault().getRules().getOffset(Instant.parse(i)).toString();
        String ts = i.substring(0,i.length() - 1) + "000" + zid;

        User user = new User(
                UUID.randomUUID().toString(),
                displayName,
                false,
                false,
                false,
                ts,
                hexPasswordHash,
                hexSalt,
                email,
                false
        );

        if(!Database.postUser(user).equals("201")){
            return null;
        }
        return user;
    }

    public static User getLoggedInUser(){
        if(loggedInUser == null){
            return guestUser;
        }
        return loggedInUser;
    }

    public static Session getCurrentSession(){
        if(!sessionToken.exists()) return null;
        if(userSession != null) return userSession;
        return null;
    }

    public static Household getUserHousehold(){
        return userHousehold;
    }

    public static void setUserHousehold(Household household){
        userHousehold = household;
    }

    public static List<String> getUserRecipes() {
        return Collections.unmodifiableList(userRecipes);
    }

    private static void createLocalSessionToken(Session session, String... recipes) throws IOException {
        JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(sessionToken)));
        writer.setIndent("  ");
        writer.beginObject().name("session_id").value(session.sessionId);
        writer.name("userid").value(session.userId);
        writer.name("location_lat").value(session.locationLat);
        writer.name("location_long").value(session.locationLong);
        writer.name("session_start").value(session.sessionStart);
        writer.name("session_end").value(session.sessionEnd);
        writer.name("email_address").value(session.emailAddress);
        writer.name("recipes").beginArray();
        for(String recipe : recipes){
            writer.value(recipe);
        }
        writer.endArray();
        writer.endObject().close();
    }

    public static void addRecipe(Session session, String recipe) throws IOException {
        String[] recipes = new String[userRecipes.size() + 1];
        for(int i = 0; i < userRecipes.size(); i++){
            recipes[i] = userRecipes.get(i);
        }
        recipes[userRecipes.size()] = recipe;
        userRecipes.add(recipe);
        createLocalSessionToken(session, recipes);
    }

    private static Session readLocalSessionToken() throws IOException{
        JsonReader reader = new JsonReader(new BufferedReader(new FileReader(sessionToken)));
        Session session = new Session(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false
        );
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();

            switch(name){
                case "session_id": {
                    session.sessionId = reader.nextString();
                    break;
                }
                case "userid": {
                    session.userId = reader.nextString();
                    break;
                }
                case "location_lat": {
                    session.locationLat = reader.nextDouble();
                    break;
                }
                case "location_long": {
                    session.locationLong = reader.nextDouble();
                    break;
                }
                case "session_start": {
                    session.sessionStart = reader.nextString();
                    break;
                }
                case "session_end": {
                    session.sessionEnd = reader.nextString();
                    break;
                }
                case "email_address": {
                    session.emailAddress = reader.nextString();
                    break;
                }
                case "stay_logged_in": {
                    session.stayLoggedIn = reader.nextBoolean();
                    break;
                }
                case "recipes" : {
                    reader.beginArray();
                    while(reader.hasNext()){
                        userRecipes.add(reader.nextString());
                    }
                    reader.endArray();
                    break;
                }
            }
        }
        if(!validateSession(session)){
            return null;
        }
        return session;
    }

    private static Session createNewSession(String userid, String email_address, boolean stay_logged_in) throws IOException {
        String i = new Timestamp(System.currentTimeMillis()).toInstant().toString();
        String zid = ZoneId.systemDefault().getRules().getOffset(Instant.parse(i)).toString();
        String ts_a = i.substring(0,i.length() - 1) + "000" + zid;

        i = new Timestamp(System.currentTimeMillis() + Application.defaultTokenExpireTime.toMillis()).toInstant().toString();
        String ts_b = i.substring(0,i.length() - 1) + "000" + zid;

        double[] latlong = Geolocation.findLatLong();

        return new Session(
                UUID.randomUUID().toString(),
                userid,
                latlong == null ? null : latlong[0],
                latlong == null ? null : latlong[1],
                ts_a,
                ts_b,
                email_address,
                stay_logged_in
        );
    }

    //TODO: implement me
    private static boolean validateSession(Session session) {
        Instant ts = new Timestamp(System.currentTimeMillis()).toInstant();
        Instant i = Instant.parse(session.sessionEnd);

        return ts.isBefore(i);
    }

    public static void invalidateSession() throws IOException {
        if(userSession != null) Database.deleteSession(userSession.sessionId);
        userSession = null;
        loggedInUser = null;
        FileWriter writer = new FileWriter(sessionToken);
        writer.write("");
        writer.close();
    }
}
