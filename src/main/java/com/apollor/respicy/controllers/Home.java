package com.apollor.respicy.controllers;

import com.apollor.respicy.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Home {
    @FXML private AnchorPane homeAnchorPane;
    @FXML private BorderPane homeBorderPane;
    @FXML private TextField recipeSearchField;
    @FXML private Button createRecipeButton;

    @FXML
    public void initialize(){
        createRecipeButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/RecipeForm.fxml"));
            try {
                homeBorderPane.setEffect(new GaussianBlur());
                homeAnchorPane.getChildren().add(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}