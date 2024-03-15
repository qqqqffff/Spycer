package com.apollor.respicy.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class Recipe {
    @FXML private BorderPane rootPane;
    @FXML private Text recipeTitleText;
    @FXML private Text ratingText;
    @FXML private Text cooktimeText;
    @FXML private ImageView recipeTumbnail;
    public Recipe(
            String recipeTitle,
            String rating,
            String cookTime,
            Image thumbnail
    ){
        this.recipeTitleText.setText(recipeTitle);
        if(rating != null) this.ratingText.setText("Rating: " + rating);
        else this.ratingText.setText("Rating: Undetermined");
        if(cookTime != null) this.cooktimeText.setText("Cook Time: " + cookTime);
        else this.cooktimeText.setText("Cook Time: Undetermined");
        if(thumbnail != null) this.recipeTumbnail.setImage(thumbnail);
    }

    @FXML
    public void initialize(){
        rootPane.setOnMouseClicked(event -> {
            System.out.println(event);
        });
    }
}
