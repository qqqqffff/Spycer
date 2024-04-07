package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.JsonLoader;
import com.apollor.spycer.utils.RecipeUpdater;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class Home {
    @FXML private AnchorPane homeAnchorPane;
    @FXML private BorderPane homeBorderPane;
    @FXML private TextField recipeSearchField;
    @FXML private Button createRecipeButton;

    @FXML
    public void initialize(){
        String ascending = "↑";
        String descending = "↓";
        String defaultSortBy = "name";

        createRecipeButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/RecipeForm.fxml"));
            try {
                homeBorderPane.setEffect(new GaussianBlur());
                ScrollPane form = fxmlLoader.load();
                form.setId("recipe_form");
                form.setLayoutX((1280-794) / 2.0);
                form.setLayoutY(50);
                homeAnchorPane.getChildren().add(form);
                disableBackground();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        File datadir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/");
        for(File dir : Objects.requireNonNull(datadir.listFiles())){
            if(dir.getName().contains("_recipe.json")){
                try {
                    Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(dir);
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                    BorderPane recipe = fxmlLoader.load();
                    RecipeUpdater.updateRecipe(recipe, data);
                    ((VBox) homeBorderPane.getCenter()).getChildren().add(recipe);
                } catch (IOException ignored) {
                    System.out.println("Trouble loading recipe");
                }
            }
        }
    }

    private void disableBackground(){

    }
}