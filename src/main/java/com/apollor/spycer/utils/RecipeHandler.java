package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RecipeHandler {
    private static Node toDelete;
    private static String fileName;
    public static void updateConfirmation(BorderPane pane, String titleText, Node i, String f){
        toDelete = i;
        fileName = f;
    }
    public static void deleteRecipe(String titleText) throws IOException {
        ((VBox) Application.rootBorderPane.getCenter()).getChildren().removeIf(x -> x.equals(toDelete));
        File recipe_dir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/" + fileName + ".json");
        if(!recipe_dir.delete()){
            throw new IOException("Failed to delete recipe");
        }
    }

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
        System.out.println(total);
        int minutes = total / 60;
        total -= minutes * 60;
        int seconds = total;

        String retString = "";
        if(days > 0){
            retString += days;
            retString += days > 1 ? " Days, " : " Day, ";
        }
        if(hours > 0){
            retString += hours;
            retString += hours > 1 ? " Hours, " : " Hour, ";
        }
        if(minutes > 0){
            retString += minutes;
            retString += minutes > 1 ? " Minutes, " : " Minute, ";
        }
        if(seconds > 0 && showSeconds){
            retString += seconds;
            retString += seconds > 1 ? " Seconds, " : " Second, ";
        }
        return !retString.isEmpty() ? retString.substring(0, retString.length() - 2) : "Undetermined";
    }

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
        Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(new File(Application.datadir.getAbsolutePath() + "/" +fileName));
        updateRecipePage(pane, data);
    }
    public static void updateRecipePage(ScrollPane pane, Map<String, Map<Integer, String[]>> data){
        VBox box = (VBox) pane.getContent();
        for(Node n : box.getChildren()){
            if(n.getClass().equals(GridPane.class)){
                for(Node i : ((GridPane) n).getChildren()){
                    switch (Objects.requireNonNullElse(i.getId(),"")){
                        case "titleText" -> ((Label) i).setText(data.get("options").get(0)[1]);
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
                            ((Label) i).setText("Author: " + data.get("options").get(3)[1]);
                        } catch(NullPointerException ignored){
                            ((Label) i).setText("Author: Unknown");
                        }

                    }
                    else if(Objects.equals(i.getId(), "ratingText")){
                        try{
                            ((Label) i).setText("Rating: " + data.get("options").get(1)[1] + " / 5");
                        }catch (NullPointerException ignored){
                            ((Label) i).setText("Rating: Unrated");
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

        pane.setStyle("-fx-background-color: transparent");
        return pane;
    }
    private static BorderPane createProcedureGroup(String[] data){
        BorderPane pane = new BorderPane();

        double screenWidth = 1280;
        try{
            screenWidth = Application.rootAnchorPane.getWidth() == 0 ? screenWidth : Application.rootAnchorPane.getWidth();
        }catch (NullPointerException ignored) {}

        double width = (screenWidth - 125) * .6;
        CheckBox cb = new CheckBox(data[0]);
        cb.setWrapText(true);
        cb.setMaxWidth(width - 50);
        pane.setLeft(cb);

        //TODO: impl dynamic spacing
        pane.setStyle("-fx-background-color: transparent");
        return pane;
    }
    private static HBox createNoteGroup(String[] data){
        HBox pane = new HBox();
        pane.setSpacing(15);

        Label indentChar = new Label(">");
        indentChar.setId("indentChar");
        indentChar.setFont(new Font(20));
        pane.getChildren().add(indentChar);
        BorderPane.setMargin(indentChar, new Insets(5,0,0,0));

        double screenWidth = 1280;
        try{
            screenWidth = Application.rootAnchorPane.getWidth() == 0 ? screenWidth : Application.rootAnchorPane.getWidth();
        }catch (NullPointerException ignored) {}
        double width = (screenWidth - 75);
        Label label = new Label(data[1]);
        label.setWrapText(true);
        label.setMaxWidth(width - 50);
        label.setStyle("-fx-font-weight: 200; -fx-font-size: 24;");
        //TODO: impl dynamic spacing
        pane.getChildren().add(label);

        pane.setStyle("-fx-background-color: transparent");
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
