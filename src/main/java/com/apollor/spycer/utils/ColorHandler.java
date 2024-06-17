package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ColorHandler {
    private ColorHandler(){}

    private static final Map<String, String> defaultPalette;
    public static Map<String, String> palette;

    static {
        defaultPalette = new HashMap<>();
        defaultPalette.put("-primary-color", "#FCF3CF");
        defaultPalette.put("-t-primary-color", "#F9E79F");
        defaultPalette.put("-secondary-color", "#FAD7A0");
        defaultPalette.put("-t-secondary-color", "#F8C471");
        defaultPalette.put("-t-tertiary-color", "#F1948A");
        defaultPalette.put("-tertiary-color", "#EC7063");
        defaultPalette.put("-error-color", "#E74C3C");
        defaultPalette.put("-contrast-color", "#000000");
        defaultPalette.put("-t-contrast-color", "#616161");
        palette = defaultPalette;
    }

    /**
     * Applies the user identified color style
     * @param identifier 0 = default,
     */
    public static void applyPalette(int identifier) throws IOException {
        switch (identifier){
            case 0: palette = defaultPalette;
            case 1: {

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
    }

    public static String hslToHex(double h, double s, double l) {
        double r, g, b;

        h /= 360.0;
        s /= 100.0;
        l /= 100.0;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRGB(p, q, h + 1.0/3.0);
            g = hueToRGB(p, q, h);
            b = hueToRGB(p, q, h - 1.0/3.0);
        }

        int red = (int) Math.round(r * 255);
        int green = (int) Math.round(g * 255);
        int blue = (int) Math.round(b * 255);

        Color color = Color.rgb(red, green, blue);
        return "#" + color.toString().substring(2).toUpperCase();
    }

    private static double hueToRGB(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6.0) return p + (q - p) * 6.0 * t;
        if (t < 1.0 / 2.0) return q;
        if (t < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6.0;
        return p;
    }

    public static double[] hsvToHSL(double[] hsv){
        double h = hsv[0];
        double s = hsv[1] / 100;
        double v = hsv[2] / 100;
        double l = v - (v * s) / 2;

        if (l != 0) {
            if (l == 1) {
                s = 0;
            } else if (l < 0.5) {
                s = s * v / (l * 2);
            } else {
                s = s * v / (2 - l * 2);
            }
        }

        return new double[]{h, s*100, l*100};
    }

    private static double compare(boolean direction, double... d){
        double a = direction ? Double.MIN_VALUE : Double.MAX_VALUE;

        for(double i : d){
            a = direction ? Math.max(i, a) : Math.min(i, a);
        }

        return a;
    }

    public static double[] hexToHSV(String hex){
        String vals = hex.split("#")[1];
        double r, g, b;

        r = Integer.parseInt(vals.substring(0,2), 16)/255.0;
        g = Integer.parseInt(vals.substring(2,4), 16)/255.0;
        b = Integer.parseInt(vals.substring(4,6), 16)/255.0;

        double max = compare(true, r, g, b), min = compare(false, r, g, b);
        double d = max - min;
        double h = -1, s, v;

        if(max == min) h = 0;
        else if(max == r) h = (60 * ((g - b) / d) + 360) % 360;
        else if(max == g) h = (60 * ((b - r) / d) + 360) % 360;
        else if(max == b) h = (60 * ((r - g) / d) + 360) % 360;

        if(max == 0) s = 0;
        else s = (d / max) * 100;

        v = max * 100;

        return new double[]{h, s, v};
    }
}
