package com.apollor.spycer.controllers;

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
            Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(100));
                    setInterpolator(Interpolator.EASE_IN);
                }
                @Override
                protected void interpolate(double v) {
                    double saturation = 29 - (15 * v);
                    double lightness = 24 + (15 * v);

                    Color c = Color.web("hsl(210, " + saturation +"%, " + lightness + "%)");
                    rootPane.setStyle("-fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: #3498DB; -fx-background-radius: 15; -fx-background-color: #"+ c.toString().substring(2) +";");
                    descBox.setStyle("-fx-background-color: #"+ c.toString().substring(2) +";");
                }
            };
            animation.play();
        });
        rootPane.setOnMouseExited(event -> {
            Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(100));
                    setInterpolator(Interpolator.EASE_OUT);
                }
                @Override
                protected void interpolate(double v) {
                    double saturation = 14 + (15 * v);
                    double lightness = 39 - (15 * v);

                    Color c = Color.web("hsl(210, " + saturation +"%, " + lightness + "%)");
                    rootPane.setStyle("-fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: black; -fx-background-radius: 15; -fx-background-color: #"+ c.toString().substring(2) +";");
                    descBox.setStyle("-fx-background-color: #"+ c.toString().substring(2) +";");
                }
            };
            animation.play();
        });
    }
}
