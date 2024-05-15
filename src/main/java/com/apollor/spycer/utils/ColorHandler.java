package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ColorHandler {
    private ColorHandler(){}

    public static Map<String, String> defaultPalette;

    static {
        defaultPalette = new HashMap<>();
        defaultPalette.put("-primary-color", "#FCF3CF");
        defaultPalette.put("-secondary-color", "#FAD7A0");
        defaultPalette.put("-t-secondary-color", "#F8C471");
        defaultPalette.put("-t-tertiary-color", "#F1948A");
        defaultPalette.put("-tertiary-color", "#EC7063");
        defaultPalette.put("-error-color", "#E74C3C");
        defaultPalette.put("-contrast-color", "#000");
    }

    /**
     * Applies the user identified color style
     * @param identifier 0 = default,
     */
    public static void applyPalette(int identifier) throws IOException {
        Map<String, String> palette = null;
        switch (identifier){
            case 0 -> palette = defaultPalette;
            case 1 -> {

            }
            default -> {
                return;
            }
        }
        assert palette != null;

        File f = new File(Application.stylesheetLink);
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        Map<String, String> finalPalette = palette;
        reader.lines().forEach(line -> {
            if(line.matches(".*-.*:.*#.*;")) {
                String key = line.substring(line.indexOf("-"), line.indexOf(":"));
                String value = line.substring(line.indexOf(": ") + 2, line.indexOf(";"));
                if (finalPalette.containsKey(key) && !finalPalette.get(key).equals(value)) {
                    sb.append('\t').append(key).append(": ").append(finalPalette.get(key)).append(";\n");
                }
                else {
                    sb.append(line).append('\n');
                }
            }
            else{
                sb.append(line).append('\n');
            }
        });

        reader.close();
        FileWriter writer = new FileWriter(f);
        writer.write(sb.toString());
        writer.close();

        System.out.println("the css has been written");

    }
}
