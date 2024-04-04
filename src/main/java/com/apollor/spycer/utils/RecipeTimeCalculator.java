package com.apollor.spycer.utils;

import org.joda.time.Period;

import java.util.Map;
import java.util.regex.Pattern;

public class RecipeTimeCalculator {
    //assuming procedure map:
    public static Period calculateRecipeTime(Map<Integer, String[]> map){
        double hours = 0;
        for(String[] value : map.values()){
            if(value[1].replace(" ", "").matches("\\d+hr&&h&&hour")){

            }
            else if()
            Pattern
        }
    }
}
