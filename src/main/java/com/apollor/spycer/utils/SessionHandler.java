package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.Session;
import com.apollor.spycer.database.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.UUID;

public class SessionHandler {
    private static User loggedInUser;
    private static Session userSession;
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

    public static void checkSessionToken(User user) throws IOException {
        Session session = Database.getSession(user.userId);
        if(session != null){
            Instant ts = new Timestamp(System.currentTimeMillis()).toInstant();
            Instant i = Instant.parse(session.sessionEnd);

            if(ts.isBefore(i)){
                userSession = session;
            }else{
                Database.deleteSession(session.sessionId);
                userSession = null;
            }
        }
        else{
            System.out.println("fail");
        }
    }

    public static boolean attemptLogin(String email, String password_plaintext) throws IOException {
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

                String i = new Timestamp(System.currentTimeMillis()).toInstant().toString();
                String zid = ZoneId.systemDefault().getRules().getOffset(Instant.parse(i)).toString();
                String ts_a = i.substring(0,i.length() - 1) + "000" + zid;

                i = new Timestamp(System.currentTimeMillis() + Application.defaultTokenExpireTime.toMillis()).toInstant().toString();
                String ts_b = i.substring(0,i.length() - 1) + "000" + zid;

                double[] latlong = Geolocation.findLatLong();

                Database.postSession(new Session(
                        UUID.randomUUID().toString(),
                        tempUser.userId,
                        latlong == null ? null : latlong[0],
                        latlong == null ? null : latlong[1],
                        ts_a,
                        ts_b
                ));
                return true;
            }

            return false;

        }catch(NoSuchAlgorithmException ignored){

        }
        return false;
    }

    //TODO: handle duplicate response
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
                email
        );

        Database.postUser(user);
        return user;
    }

    public static User getLoggedInUser(){
        if(loggedInUser == null){
            return guestUser;
        }
        return loggedInUser;
    }
}
