package com.apollor.respicy.utils;

import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonLoader {
    public static Map<String, Map<Integer, String[]>> parseJsonRecipe(File f) throws IOException {
        Map<String, Map<Integer, String[]>> map = new HashMap<>();
        Map<Integer, String[]> options = new HashMap<>();
        Map<Integer, String[]> ingredients = new HashMap<>();
        Map<Integer, String[]> procedures = new HashMap<>();
        Map<Integer, String[]> notes = new HashMap<>();

        JsonReader jr = new JsonReader(new BufferedReader(new FileReader(f)));
        jr.beginObject();
        options.put(0, new String[]{jr.nextName(), jr.nextString()});
        jr.nextName();
        jr.beginArray();
        int i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            ingredients.put(i++, new String[]{jr.nextName(), jr.nextString()});
            jr.endObject();
        }
        i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            procedures.put(i++, new String[]{jr.nextName(), jr.nextString()});
            jr.endObject();
        }
        i = 0;
        while(jr.hasNext()){
            jr.beginObject();
            notes.put(i++, new String[]{jr.nextName(), jr.nextString()});
            jr.endObject();
        }
        map.put("options", options);
        map.put("ingredients", ingredients);
        map.put("procedures", procedures);
        map.put("notes", notes);
        return map;
    }
}
