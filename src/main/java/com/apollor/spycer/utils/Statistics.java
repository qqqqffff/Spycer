package com.apollor.spycer.utils;

import java.util.List;

public class Statistics {
    private Statistics(){}

    public static double std(List<Double> list){
        double mean = mean(list);
        return Math.sqrt(list.parallelStream().reduce(0.0, (sum, x) -> Math.pow(x - mean, 2))) / list.size();
    }
    public static double mean(List<Double> list){
        return list.parallelStream().reduce(0.0, Double::sum) / list.size();
    }
}
