package com.apollor.spycer.database;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.SessionHandler;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Database {
    public static User getUser(String email) throws IOException {
        return User.get(email);
    }
    public static Image getUserPFP(String userid) throws IOException{
        return User.getUserPFP(userid);
    }
    public static String postUser(User user) throws IOException {
        return User.post(user);
    }
    public static String postUserPFP(File file) throws IOException {
        String fileName = file.toURI().toString();
        if(SessionHandler.getLoggedInUser().userId == null) return "401";
        File f = new File(Application.datadir + "/" + SessionHandler.getLoggedInUser().userId + fileName.substring(fileName.indexOf(".")));
        if(!f.createNewFile()) throw new RuntimeException("Unable to create temp file");
        Files.copy(file.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
        assert(f.exists());
        String returnCode = User.postUserPFP(f);
        if(returnCode.equals("201") && !f.delete()) throw new RuntimeException("Unable to delete temp file");
        return returnCode;
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

    public static Household getHouseholdByUID(String userid) throws IOException {
        return Household.getByUID(userid);
    }
    public static Household[] getHouseholdsByID(String householdId) throws IOException {
        return Household.getByID(householdId);
    }
    public static String postHousehold(Household household) throws IOException{
        return Household.post(household);
    }
    public static String putHousehold(Household household) throws IOException {
        return Household.post(household);
    }
    public static String deleteHousehold(String householdId) throws IOException {
        return Household.delete(householdId);
    }

}
