package com.apollor.spycer.utils;

import java.util.regex.Pattern;

public class RecipeTimeCalculator {
    private RecipeTimeCalculator(){}
    /**
     * @param item values of the procedure array
     * @return the total amount in seconds
     */
    public static String calculateRecipeTime(String[] item){
        int period = 0;
        for(String value: item){
            if(Pattern.matches("\\d+_h", value)){
                period += Integer.parseInt(value.replace("_h", "")) * 3600;
            }
            else if(Pattern.matches("\\d+_m", value)){
                period += Integer.parseInt(value.replace("_m", "")) * 60;
            }
            else if(Pattern.matches("\\d+_s", value)){
                period += Integer.parseInt(value.replace("_s", ""));
            }
            else{
                return null;
            }
        }
        return String.valueOf(period);
    }

    /**
     * @param total total time of each procedure (value in seconds)
     * @return the formatted string
     */
    public static String formatTotalTime(int total, boolean showSeconds){
        int days = total / (3600 * 24);
        total -= days * 3600 * 24;
        int hours = total / 3600;
        total -= hours * 3600;
        int minutes = total / 60;
        total -= minutes * 60;
        int seconds = total;

        String retString = "";
        if(days > 0){
            retString += days;
            retString += days > 1 ? " days, " : " day, ";
        }
        if(hours > 0){
            retString += hours;
            retString += hours > 1 ? " hours, " : " hour, ";
        }
        if(minutes > 0){
            retString += minutes;
            retString += minutes > 1 ? " minutes, " : " minute, ";
        }
        if(seconds > 0 && showSeconds){
            retString += seconds;
            retString += seconds > 1 ? " seconds, " : " second, ";
        }
        return retString.substring(0, retString.length() - 2);
    }
}
