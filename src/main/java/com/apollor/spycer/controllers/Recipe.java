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
        final String defaultStyle = "-fx-background-color: white; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: black; -fx-background-radius: 15";
        final String hoverStyle = "-fx-background-color: lightgrey; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: black; -fx-background-radius: 15";

        rootPane.setOnMouseClicked(event -> {
            System.out.println(event);
        });

        rootPane.setOnMouseEntered(event -> rootPane.setStyle(hoverStyle));
        rootPane.setOnMouseExited(event -> rootPane.setStyle(defaultStyle));
    }
}
