package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class RecipeUpdater {

    public static void updateRecipe(BorderPane pane, Map<String, Map<Integer, String[]>> data, String fileName){
        VBox center = (VBox) ((BorderPane) pane.getCenter()).getCenter();
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
                            String rating = "Rating: " + data.get("options").get(1)[1] + " / 5";
                            ((Text) i).setText(rating);
                        } catch(NullPointerException ignored){
                            ((Text) i).setText("Rating: Unrated");
                        }

                    }
                    else if(Objects.equals(i.getId(), "cooktimeText")) {
                        try{
                            String rating = "Time: " + data.get("options").get(2)[0];
                            ((Text) i).setText(rating);
                        } catch(NullPointerException ignored){
                            ((Text) i).setText("Time: Undetermined");
                        }
                    }
                    else if(Objects.equals(i.getId(), "fnameText")){
                        ((Text) i).setText(fileName);
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

    public static void updateRecipePage(ScrollPane pane, String fileName) throws IOException {
        VBox box = (VBox) pane.getContent();
        Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(new File(Application.datadir.getAbsolutePath() + "/" +fileName));
        for(Node n : box.getChildren()){
            if(n.getClass().equals(GridPane.class)){
                for(Node i : ((GridPane) n).getChildren()){
                    switch (Objects.requireNonNullElse(i.getId(),"")){
                        case "titleText" -> ((Text) i).setText(data.get("options").get(0)[1]);
                        case "ingredientsBox" -> {
                            for(String[] value : data.get("ingredients").values()){
                                ((VBox) i).getChildren().add(createIngredientGroup(value));
                            }
                        }
                        case "proceduresBox" -> {
                            for(String[] value : data.get("procedures").values()){
                                ((VBox) i).getChildren().add(createProcedureGroup(value));
                            }
                        }
                        case "notesBox" -> {
                            for(String[] value : data.get("notes").values()){
                                ((VBox) i).getChildren().add(createNoteGroup(value));
                            }
                        }
                        case "" -> {}
                    }
                }
            }
            else if(n.getClass().equals(HBox.class)){
                for(Node i : ((HBox) n).getChildren()){
                    if(Objects.equals(i.getId(), "authorText")){
                        try{
                            ((Text) i).setText("Author: " + data.get("options").get(3)[1]);
                        } catch(NullPointerException ignored){
                            ((Text) i).setText("Author: Unknown");
                        }

                    }
                    else if(Objects.equals(i.getId(), "ratingText")){
                        try{
                            ((Text) i).setText("Rating: " + data.get("options").get(1)[1] + " / 5");
                        }catch (NullPointerException ignored){
                            ((Text) i).setText("Rating: Unrated");
                        }

                    }
                }
            }
        }
    }

    private static BorderPane createIngredientGroup(String[] data){
        BorderPane pane = new BorderPane();

        CheckBox cb = new CheckBox(data[0]);
        pane.setLeft(cb);

        Label label = new Label(data[1]);
        pane.setRight(label);

        return pane;
    }
    private static BorderPane createProcedureGroup(String[] data){
        BorderPane pane = new BorderPane();

        double width = (Application.rootAnchorPane.getWidth() - 125) * .6;
        CheckBox cb = new CheckBox(data[0]);
        cb.setWrapText(true);
        cb.setMaxWidth(width - 50);
        pane.setLeft(cb);
        //TODO: impl dynamic spacing

        return pane;
    }
    private static HBox createNoteGroup(String[] data){
        HBox pane = new HBox();
        pane.setSpacing(15);

        Label indentChar = new Label(">");
        indentChar.setId("indentChar");
        indentChar.setFont(new Font(20));
        pane.getChildren().add(indentChar);

        double width = (Application.rootAnchorPane.getWidth() - 75);
        Label label = new Label(data[1]);
        label.setWrapText(true);
        label.setMaxWidth(width - 50);
        //TODO: impl dynamic spacing
        pane.getChildren().add(label);

        return pane;
    }

    private static String extrapolateTags(Map<Integer, String[]> data){
        if(data.isEmpty()) throw new NullPointerException("Tag Dictionary Empty");
        StringBuilder retString = new StringBuilder();
        for(String[] value : data.values()){
            retString.append(value[1]).append(", ");
        }
        return retString.substring(0, retString.length() - 2);
    }
}
