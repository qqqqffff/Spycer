package com.apollor.spycer.utils;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Objects;

public class RecipeUpdater {
    public static void updateRecipe(BorderPane pane, Map<String, Map<Integer, String[]>> data){
        VBox center = (VBox) pane.getCenter();
        for(Node n : center.getChildren()){
            if(Objects.equals(n.getId(), "recipeTitleText")){
                try{
                    String rating = data.get("options").get(0)[1];
                    ((Text) n).setText(rating);
                } catch(NullPointerException ignored){ }
            }
            else if(Objects.equals(n.getId(), "subHBox")){
                for(Node i : ((HBox) n).getChildren()){
                    if(Objects.equals(i.getId(), "ratingText")){
                        try{
                            String rating = "Rating: " + data.get("options").get(1)[1] + " / 5.0";
                            ((Text) i).setText(rating);
                        } catch(NullPointerException ignored){
                            ((Text) i).setText("Rating: Unrated");
                        }

                    }
                    else if(Objects.equals(i.getId(), "cooktimeText")) {
                        try{
                            String rating = "Time: " + data.get("options").get(2)[1];
                            ((Text) i).setText(rating);
                        } catch(NullPointerException ignored){
                            ((Text) i).setText("Time: Undetermined");
                        }
                    }
                }
            }
            else if(Objects.equals(n.getId(), "tagsText")){
                try{
                    String tags = "Tags: " + extrapolateTags(data.get("tags"));
                    ((Text) n).setText(tags);
                }catch (NullPointerException ignored){
                    ((Text) n).setText("Tags: None");
                }
            }
        }
    }

    private static String extrapolateTags(Map<Integer, String[]> data){
        StringBuilder retString = new StringBuilder();
        for(String[] value : data.values()){
            retString.append(value[1]).append(", ");
        }
        return retString.substring(0, retString.length() - 2);
    }
}
