package com.apollor.spycer.utils;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonLoader {
    public static Map<String, Map<Integer, String[]>> parseJsonRecipe(File f) throws IOException {
        Map<String, Map<Integer, String[]>> map = new HashMap<>();
        Map<Integer, String[]> options = new HashMap<>();
        Map<Integer, String[]> ingredients = new HashMap<>();
        Map<Integer, String[]> procedures = new HashMap<>();
        Map<Integer, String[]> notes = new HashMap<>();
        Map<Integer, String[]> tags = new HashMap<>();

        JsonReader jr = new JsonReader(new BufferedReader(new FileReader(f)));
        jr.beginObject();
        while(jr.hasNext()){
            String jsonName = jr.nextName();
            JsonParameter name = JsonParameter.parseName(jsonName);
            if(name == null){
                continue;
            }
            switch(name){
                case TITLE -> options.put(0, new String[]{jsonName, jr.nextString()});
                case RATING -> options.put(1, new String[]{jsonName, jr.nextString()});
                case INGREDIENTS -> dynamicPlacement(jr, ingredients);
                case PROCEDURES -> dynamicPlacement(jr, procedures);
                case NOTES -> dynamicPlacement(jr, notes);
                case TAGS -> dynamicPlacement(jr, tags);
            }
        }
        int totalTime = procedures.values().stream().map(x -> {
            try{
                return Integer.parseInt(x[1]);
            }catch(NumberFormatException ignored){
                return 0;
            }
        }).reduce(0, Integer::sum);
        options.put(4, new String[]{RecipeTimeCalculator.formatTotalTime(totalTime, false)});

        map.put("options", options);
        map.put("ingredients", ingredients);
        map.put("procedures", procedures);
        map.put("notes", notes);
        map.put("tags", tags);
        return map;
    }

    private static void dynamicPlacement(JsonReader reader, Map<Integer, String[]> map) throws IOException {
        reader.beginArray();
        int i = 0;
        while(reader.hasNext()){
            reader.beginObject();
            map.put(i++, new String[]{reader.nextName(), reader.nextString()});
            reader.endObject();
        }
        reader.endArray();
    }

}
