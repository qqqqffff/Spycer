package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.JsonLoader;
import com.apollor.spycer.utils.RecipeUpdater;
import com.apollor.spycer.utils.SortParam;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class Home {
    @FXML private AnchorPane homeAnchorPane;
    @FXML private BorderPane homeBorderPane;
    @FXML private TextField recipeSearchField;
    @FXML private Button createRecipeButton;
    @FXML private Button sortDirectionButton;
    @FXML private ComboBox<SortParam> sortParameterComboBox;


    @FXML
    public void initialize(){
        String ascending = "↑";
        String descending = "↓";
        SortParam defaultSortBy = SortParam.NAME;

        File datadir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/");
        for(File dir : Objects.requireNonNull(datadir.listFiles())){
            if(dir.getName().matches(".*_recipe\\d*[.]json")){
                try {
                    Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(dir);
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                    BorderPane recipe = fxmlLoader.load();
                    RecipeUpdater.updateRecipe(recipe, data, dir.getName());
                    ((VBox) homeBorderPane.getCenter()).getChildren().add(recipe);
                } catch (IOException ignored) {
                    System.out.println("Trouble loading recipe");
                }
            }
        }

        createRecipeButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/RecipeForm.fxml"));
            try {
                homeBorderPane.setEffect(new GaussianBlur());
                ScrollPane form = fxmlLoader.load();
                form.setId("recipe_form");
                form.setLayoutX((1280-794) / 2.0);
                form.setLayoutY(50);
                homeAnchorPane.getChildren().add(form);
                Application.rootBorderPane.setDisable(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        createRecipeButton.setOnMouseEntered(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    createRecipeButton,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    9.0,
                    92.0,
                    5.0,
                    -53.0
            );
            animation.play();
        });
        createRecipeButton.setOnMouseExited(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    createRecipeButton,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    14,
                    39,
                    -5.0,
                    53.0
            );
            animation.play();
        });

        recipeSearchField.setOnMouseEntered(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    recipeSearchField,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    29.0,
                    24.0,
                    -15.0,
                    15.0
            );
            animation.play();
        });
        recipeSearchField.setOnMouseExited(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    recipeSearchField,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    14.0,
                    39,
                    15.0,
                    -15.0
            );
            animation.play();
        });

        sortDirectionButton.setOnAction(action -> {
            //ascending
            if(sortDirectionButton.getText().equals(ascending)){
                sortDirectionButton.setText(descending);
            }
            //descending
            else{
                sortDirectionButton.setText(ascending);
            }
        });
        sortDirectionButton.setOnMouseEntered(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    sortDirectionButton,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    9.0,
                    92.0,
                    5.0,
                    -53.0
            );
            animation.play();
        });
        sortDirectionButton.setOnMouseExited(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    sortDirectionButton,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    14,
                    39,
                    -5.0,
                    53.0
            );
            animation.play();
        });

        sortParameterComboBox.setOnMouseEntered(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    sortParameterComboBox,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    9.0,
                    92.0,
                    5.0,
                    -53.0
            );
            animation.play();
        });
        sortParameterComboBox.setOnMouseExited(event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    sortParameterComboBox,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    14,
                    39,
                    -5.0,
                    53.0
            );
            animation.play();
        });
    }
}