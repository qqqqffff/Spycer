package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.*;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Home {
    @FXML private VBox contentBox;

    @FXML
    public void initialize(){
        List<String> recipes = SessionHandler.getUserRecipes();
        for(File dir : Objects.requireNonNull(Application.datadir.listFiles())){
            if(dir.isDirectory() && recipes.contains(dir.getName())){
                File recipeJson = new File(dir.getAbsolutePath() + "/recipe.json");
                try {
                    Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(recipeJson);
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                    BorderPane recipe = fxmlLoader.load();
                    RecipeHandler.updateRecipe(recipe, data, dir.getName());
                    contentBox.getChildren().add(recipe);
                } catch (IOException ignored) {
                    System.out.println("Trouble loading recipe");
                }
            }
        }
    }

    public static void dynamicAdjust(Node n){
        VBox contentBox = ((VBox) ((GridPane) Application.rootBorderPane.getCenter()).getChildren().stream()
                .filter(node -> node.getClass() == VBox.class).toList().get(0));
        final Node[] itemToDelete = {null};
        for(int i = 0, j = -1; i < contentBox.getChildren().size(); i++){
            if(contentBox.getChildren().get(i).equals(n)){
                itemToDelete[0] = contentBox.getChildren().get(i);
                j = 1;
            }
            else if(j != -1){
                Node x = contentBox.getChildren().get(i);
                Animation animation = AnimationFactory.generateTranslateTransition2(
                        x,
                        Interpolator.LINEAR,
                        Duration.millis(150),
                        new double[]{0, -i * (x.getLayoutBounds().getHeight() + 15)}
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
}