package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.*;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Home {
    @FXML private VBox contentBox;
    private final static List<Map<String, Map<Integer, String[]>>> displayRecipes = new ArrayList<>();
    private static List<Map<String, Map<Integer, String[]>>> currentRecipes = new ArrayList<>();

    @FXML
    public void initialize(){
        displayRecipes.clear();
        List<String> recipes = SessionHandler.getUserRecipes();
        for(File dir : Objects.requireNonNull(Application.datadir.listFiles())){
            if(dir.isDirectory() && recipes.contains(dir.getName())){
                File recipeJson = new File(dir.getAbsolutePath() + "/recipe.json");
                try {
                    Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(recipeJson);
                    data.put("dir", Map.of(0, new String[]{dir.getName()}));
                    displayRecipes.add(data);
                } catch (IOException ignored) {
                    throw new RuntimeException("Failed to load a recipe");
                }
            }
        }

        currentRecipes.clear();
        currentRecipes.addAll(displayRecipes);
        sort(Header.sortParam.get(), Header.direction.get(), contentBox);
    }

    public static void dynamicAdjust(Node n){
        VBox contentBox = ((VBox) ((GridPane) Application.rootBorderPane.getCenter()).getChildren().stream()
                .filter(node -> node.getClass() == VBox.class).toList().get(0));
        final Node[] itemToDelete = {null};
        for(int i = 0, j = -1; i < contentBox.getChildren().size(); i++){
            if(contentBox.getChildren().get(i).equals(n)){
                itemToDelete[0] = contentBox.getChildren().get(i);
                j = i;
            }
            else if(j != -1){
                Node x = contentBox.getChildren().get(i);
                Animation animation = AnimationFactory.generateTranslateTransition2(
                        x,
                        Interpolator.LINEAR,
                        Duration.millis(200),
                        new double[]{0, -(i - j) * (x.getLayoutBounds().getHeight())}
                );
                animation.play();
                animation.setOnFinished(e -> {
                    if(itemToDelete[0] != null) {
                        contentBox.getChildren().remove(itemToDelete[0]);
                        itemToDelete[0] = null;
                    }
                    x.setTranslateY(0);
                });
            }
        }
    }

    //TODO: can be sped up
    public static void sort(SortParam param, boolean direction, VBox contentBox){
        switch (param){
            case BEST -> displayRecipes.sort((a, b) -> {
                int totalTimeA = a.get("procedures").values().stream().map(x -> {
                    try {
                        return Integer.parseInt(x[1]);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                }).reduce(0, Integer::sum);
                int totalTimeB = b.get("procedures").values().stream().map(x -> {
                    try {
                        return Integer.parseInt(x[1]);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                }).reduce(0, Integer::sum);
                int meantime = 4800;
                int stdtime = 3600;
                //running total time through normal dist
                Double timeValueA = (Math.exp(-1 * (
                        (Math.pow(totalTimeA - meantime, 2) /
                        (2 * Math.pow(stdtime, 2))))
                )) * .6;
                Double ratingValueA = ((Double.parseDouble(a.get("options").get(1)[1]) - 1) / (5.0 - 1)) * .4;
                Double bestA = timeValueA + ratingValueA;

                Double timeValueB = (Math.exp(-1 * (
                        (Math.pow(totalTimeB - meantime, 2) /
                                (2 * Math.pow(stdtime, 2))))
                )) * .6;
                Double ratingValueB = ((Double.parseDouble(b.get("options").get(1)[1]) - 1) / (5.0 - 1)) * .4;
                Double bestB = timeValueB + ratingValueB;


                return direction ? bestB.compareTo(bestA) : bestA.compareTo(bestB);
            });
            case NAME -> displayRecipes.sort((a, b) -> direction ?
                    a.get("options").get(0)[1].compareTo(b.get("options").get(0)[1]) :
                    b.get("options").get(0)[1].compareTo(a.get("options").get(0)[1]));
            case TIME -> displayRecipes.sort((a, b) -> {
                Integer totalTimeA = a.get("procedures").values().stream().map(x -> {
                    try {
                        return Integer.parseInt(x[1]);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                }).reduce(0, Integer::sum);
                Integer totalTimeB = b.get("procedures").values().stream().map(x -> {
                    try {
                        return Integer.parseInt(x[1]);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                }).reduce(0, Integer::sum);

                return direction ? totalTimeA.compareTo(totalTimeB) : totalTimeB.compareTo(totalTimeA);
            });
            case RATING -> displayRecipes.sort((a, b) -> direction ?
                    Double.compare(Double.parseDouble(b.get("options").get(1)[1]), Double.parseDouble(a.get("options").get(1)[1])) :
                    Double.compare(Double.parseDouble(a.get("options").get(1)[1]), Double.parseDouble(b.get("options").get(1)[1])));
        }
        //redraw recipes
        drawRecipes(contentBox, currentRecipes);
    }

    public static void filter(String filter, VBox contentBox){
        currentRecipes = new ArrayList<>(displayRecipes.stream().filter(x -> {
            for(String[] tags : x.get("tags").values()){
                if(tags[1].matches(".*"+filter+".*")) return true;
            }
            return x.get("options").get(0)[1].matches(".*"+filter+".*");
        }).toList());
        drawRecipes(contentBox, currentRecipes);
    }

    private static void drawRecipes(VBox contentBox, List<Map<String, Map<Integer, String[]>>> recipes){
        contentBox.getChildren().clear();
        for(Map<String, Map<Integer, String[]>> data : recipes){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                BorderPane recipe = fxmlLoader.load();
                RecipeHandler.updateRecipe(recipe, data, data.get("dir").get(0)[0]);
                contentBox.getChildren().add(recipe);
            }catch (IOException ignored) {
                throw new RuntimeException("Failed to create recipe object");
            }
        }
    }
}