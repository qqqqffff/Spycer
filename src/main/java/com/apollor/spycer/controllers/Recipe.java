package com.apollor.spycer.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Recipe {
    @FXML public HBox subHBox;
    @FXML private BorderPane rootPane;
    @FXML private Text recipeTitleText;
    @FXML private Text ratingText;
    @FXML private Text cooktimeText;
    @FXML private ImageView recipeTumbnail;
    @FXML private VBox descBox;

    @FXML
    public void initialize(){
        final String defaultStyle = "-fx-background-color: #17202A; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: black; -fx-background-radius: 15";
        final String hoverStyle = "-fx-background-color: #566573; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: #3498DB; -fx-background-radius: 15";
        final String descBoxDefaultStyle = "-fx-background-color: #17202A";
        final String descBoxHoverStyle = "-fx-background-color: #566573";


        rootPane.setOnMouseClicked(event -> {
            System.out.println(event);
        });

        rootPane.setOnMouseEntered(event -> {
            rootPane.setStyle(hoverStyle);
            descBox.setStyle(descBoxHoverStyle);
        });
        rootPane.setOnMouseExited(event -> {
            rootPane.setStyle(defaultStyle);
            descBox.setStyle(descBoxDefaultStyle);
        });
    }
}
