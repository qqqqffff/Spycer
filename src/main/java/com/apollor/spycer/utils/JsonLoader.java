package com.apollor.spycer.utils;

import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.Arrays;
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
        options.put(0, new String[]{jr.nextName(), jr.nextString()});
        options.put(1, new String[]{jr.nextName(), jr.nextString()});

        jr.nextName();
        jr.beginArray();
        int i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            ingredients.put(i++, new String[]{jr.nextName(), jr.nextString()});
            jr.endObject();
        }

        jr.endArray();
        jr.nextName();
        jr.beginArray();
        i = 0;
        int totalTime = 0;
        while(jr.hasNext()){
            jr.beginObject();
            String[] procedure = new String[]{jr.nextName(), jr.nextString()};
            try{
                totalTime += Integer.parseInt(procedure[1]);
            }catch(NumberFormatException ignored){

            }
            procedures.put(i++, procedure);
            jr.endObject();
        }
        options.put(2, new String[]{"cookTime", RecipeTimeCalculator.formatTotalTime(totalTime, false)});

        jr.endArray();
        jr.nextName();
        jr.beginArray();
        i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            notes.put(i++, new String[]{jr.nextName(), jr.nextString()});
            jr.endObject();
        }

        jr.endArray();
        jr.nextName();
        jr.beginArray();
        i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            tags.put(i++, new String[]{jr.nextName(), jr.nextString()});
        }
        map.put("options", options);
        map.put("ingredients", ingredients);
        map.put("procedures", procedures);
        map.put("notes", notes);
        map.put("tags", tags);
        return map;
    }
}
