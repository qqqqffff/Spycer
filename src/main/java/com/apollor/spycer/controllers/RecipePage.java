package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class RecipePage {

    @FXML private Button backButton;
    @FXML private Label titleText;
    @FXML private Button editRecipeButton;
    @FXML private Button deleteRecipeButton;
    @FXML private Button settingsButton;
    @FXML private Label authorText;
    @FXML private Label ratingText;

    @FXML private VBox ingredientsBox;
    @FXML private VBox proceduresBox;
    @FXML private VBox notesBox;

    @FXML private Button syncPantryButton;
    @FXML private Button switchProceduresType;

    @FXML
    public void initialize(){
        backButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(backButton));
        backButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(backButton));
        backButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
            try {
                Application.rootBorderPane.setCenter(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        editRecipeButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(editRecipeButton));
        editRecipeButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(editRecipeButton));

        deleteRecipeButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(deleteRecipeButton));
        deleteRecipeButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(deleteRecipeButton));

        settingsButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(settingsButton));
        settingsButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(settingsButton));

        syncPantryButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(syncPantryButton));
        syncPantryButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(syncPantryButton));
    }
}
