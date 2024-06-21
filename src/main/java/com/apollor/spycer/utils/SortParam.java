package com.apollor.spycer.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.List;

public enum SortParam {
    BEST(){
        public String toString(){
            return "Best";
        }
    },
    NAME(){
        public String toString(){
            return "Name";
        }
    },
    TIME(){
        public String toString(){
            return "Time";
        }
    },
    RATING(){
        public String toString(){
            return "Rating";
        }
    };
    public static ObservableList<String> sortParameters(){
        return FXCollections.observableList(List.of(
                BEST.toString(),
                NAME.toString(),
                TIME.toString(),
                RATING.toString()
        ));
    }
    public static SortParam parse(String s){
        if(s == null) return null;
        switch (s.toUpperCase()){
            case "BEST" -> {
                return BEST;
            }
            case "NAME" -> {
                return NAME;
            }
            case "TIME" -> {
                return TIME;
            }
            case "RATING" -> {
                return RATING;
            }
        }
        return null;
    }
}
