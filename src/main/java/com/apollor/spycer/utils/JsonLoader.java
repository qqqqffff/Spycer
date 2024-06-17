package com.apollor.spycer.utils;

import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonLoader {
    /**
     * Recipe Json Parser:
     * 5 Maps found with the key of the map names:
     * 1) options map
     * 1a) title
     * 1b) rating
     * 1c) total time
     * 1d) author
     * 2) ingredients
     * 3) procedures
     * 4) notes
     * 5) tags
     * @param f file to parse
     * @return Map of all the data contained in the recipe json
     * @throws IOException if json file dne
     */
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
                case TITLE: {
                    options.put(0, new String[]{jsonName, jr.nextString()});
                    break;
                }
                case RATING: {
                    options.put(1, new String[]{jsonName, jr.nextString()});
                    break;
                }
                case AUTHOR: {
                    options.put(3, new String[]{jsonName, jr.nextString()});
                    break;
                }
                case INGREDIENTS: {
                    dynamicPlacement(jr, ingredients);
                    break;
                }
                case PROCEDURES: {
                    dynamicPlacement(jr, procedures);
                    break;
                }
                case NOTES: {
                    dynamicPlacement(jr, notes);
                    break;
                }
                case TAGS: {
                    dynamicPlacement(jr, tags);
                    break;
                }
            }
        }
        int totalTime = procedures.values().stream().map(x -> {
            try{
                return Integer.parseInt(x[1]);
            }catch(NumberFormatException ignored){
                return 0;
            }
        }).reduce(0, Integer::sum);
        options.put(2, new String[]{RecipeHandler.formatTotalTime(totalTime, false)});

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
