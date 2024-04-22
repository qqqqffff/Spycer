package com.apollor.spycer.utils;

import java.util.Locale;

public enum JsonParameter {
    TITLE,
    RATING,
    TAGS,
    PROCEDURES,
    INGREDIENTS,
    AUTHOR,
    NOTES;


    public String toString(){
        return this.name();
    }

    public static JsonParameter parseName(String name){
        switch (name.toUpperCase(Locale.ROOT)) {
            case "TITLE" -> {
                return JsonParameter.TITLE;
            }
            case "RATING" -> {
                return JsonParameter.RATING;
            }
            case "TAGS" -> {
                return JsonParameter.TAGS;
            }
            case "PROCEDURES" -> {
                return JsonParameter.PROCEDURES;
            }
            case "INGREDIENTS" -> {
                return JsonParameter.INGREDIENTS;
            }
            case "NOTES" -> {
                return JsonParameter.NOTES;
            }
            case "AUTHOR" -> {
                return JsonParameter.AUTHOR;
            }
        }
        return null;
    }
}


