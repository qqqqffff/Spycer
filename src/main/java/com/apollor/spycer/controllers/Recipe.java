package com.apollor.spycer.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Recipe {
    @FXML public HBox subHBox;
    @FXML private BorderPane rootPane;
    @FXML private Text recipeTitleText;
    @FXML private Text ratingText;
    @FXML private Text cooktimeText;
    @FXML private ImageView recipeTumbnail;

    @FXML
    public void initialize(){
        rootPane.setOnMouseClicked(event -> {
            System.out.println(event);
        });

        rootPane.setOnMouseEntered(event -> rootPane.setStyle("-fx-background-color: lightgrey"));
        rootPane.setOnMouseExited(event -> rootPane.setStyle("-fx-background-color: white"));
    }
}
