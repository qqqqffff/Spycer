package com.apollor.spycer.controllers;

import com.apollor.spycer.utils.AnimationFactory;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
        final String descBoxDefaultStyle = "-fx-background-color: #2C3E50";

        descBox.setStyle(descBoxDefaultStyle);
        rootPane.setOnMouseClicked(System.out::println);

        rootPane.setOnMouseEntered(event -> {
            Animation animation1 = AnimationFactory.generateFillTransition(
                    rootPane,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    "-fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: #3498DB; -fx-background-radius: 15; -fx-background-color: ",
                    29.0,
                    24.0,
                    -15.0,
                    15.0
            );
            Animation animation2 = AnimationFactory.generateFillTransition(
                    descBox,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    29.0,
                    24.0,
                    -15.0,
                    15.0
            );
            animation1.play();
            animation2.play();
        });
        rootPane.setOnMouseExited(event -> {
            Animation animation1 = AnimationFactory.generateFillTransition(
                    rootPane,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: #000000; -fx-background-radius: 15; -fx-background-color: ",
                    14.0,
                    39.0,
                    15.0,
                    -15.0
            );
            Animation animation2 = AnimationFactory.generateFillTransition(
                    descBox,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    14.0,
                    39.0,
                    15.0,
                    -15.0
            );
            animation1.play();
            animation2.play();
        });
    }
}
