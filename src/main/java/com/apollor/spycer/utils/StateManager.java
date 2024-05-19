package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StateManager {
    private static final File stateFile = new File(Application.datadir.getAbsolutePath() + "/application_state.json");
    public static Map<String, Map<String, String>> readSate() throws IOException {
        System.out.println("Reading State");
        if(!checkStateFile()){
            updateState(null);
            return null;
        }
        else {
            JsonReader jr = new JsonReader(new BufferedReader(new FileReader(stateFile)));
            Map<String, Map<String, String>> dataMap = new HashMap<>();
            Map<String, String> fileMap = new HashMap<>();
            Map<String, String> pageMap = new HashMap<>();

            jr.beginObject();
            while(jr.hasNext()){
                String name = jr.nextName();
                switch (name){
                    case "page" -> pageMap.put(name, jr.nextString());
                    case "file" -> fileMap.put(name, jr.nextString());
                    case "options" -> {
                        //TODO: implement special parsing
                        jr.beginArray();
                        jr.endArray();
                    }
                }
            }
            jr.endObject();

            dataMap.put("file", fileMap);
            dataMap.put("page", pageMap);
            return dataMap;
        }
    }
    public static void updateState(Map<String, Map<String, String>> data) throws IOException {
        checkStateFile();
        System.out.println("Updating state");
        JsonWriter jw = new JsonWriter(new BufferedWriter(new FileWriter(stateFile)));
        jw.setIndent("  ");
        if(data == null){
            String page = SessionHandler.getCurrentSession() != null ? "views/Home.fxml" : "views/Login.fxml";
            jw.beginObject().name("page").value(page).name("file").value("null").name("options").beginArray().endArray().endObject();
        }
        else{
            jw.beginObject().name("page").value(data.get("page").get("page")).name("file").value(data.get("file").get("file"));
            jw.name("options").beginArray();
            //TODO: implement some special data map parsing
            jw.endArray().endObject();

        }
        jw.close();
    }
    private static boolean checkStateFile(){
        if(!stateFile.exists()){
            try {
                if(!stateFile.createNewFile()) throw new IOException("Failed to create state file");
                System.out.println("State File Created");
                return false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
