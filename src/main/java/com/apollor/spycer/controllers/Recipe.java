package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.RecipeDeleter;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Recipe {
    @FXML public HBox subHBox;
    @FXML private BorderPane rootPane;
    @FXML private BorderPane contentPane;
    @FXML private Text recipeTitleText;
    @FXML private Text ratingText;
    @FXML private Text cooktimeText;
    @FXML private Text tagsText;
    @FXML private ImageView recipeThumbnail;
    @FXML private VBox descBox;
    @FXML private Text fnameText;

    @FXML private AnchorPane deletePane;
    @FXML private Rectangle deleteButton;

    private boolean dragging;

    @FXML
    public void initialize(){
        final String descBoxDefaultStyle = "-fx-background-color: #2C3E50";

        descBox.setStyle(descBoxDefaultStyle);

        AtomicReference<Double> initialDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> finalDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> deltaDragPos = new AtomicReference<>(Double.MAX_VALUE);

        rootPane.setOnMouseEntered(event -> {
            Animation animation1 = AnimationFactory.generateFillTransition(
                    contentPane,
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
                    contentPane,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-border-radius: 15; -fx-background-radius: 15; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: #000000; -fx-background-radius: 15; -fx-background-color: ",
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
        rootPane.setOnMouseDragged(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                if(initialDragPos.get() == Double.MAX_VALUE){
                    initialDragPos.set(event.getX());
                    finalDragPos.set(Double.MIN_VALUE);
                }
                else if((finalDragPos.get() - initialDragPos.get()) > (rootPane.getWidth() / 6)){
                    rootPane.setDisable(true);
                }
                else {
                    dragging = true;
                    finalDragPos.set(event.getX());
                    if(deltaDragPos.get() < (finalDragPos.get() - initialDragPos.get())) {
                        if(deltaDragPos.get() > 25){
                            BorderPane.setMargin(deletePane, new Insets(0, 15,0,0));
                            deleteButton.setHeight(rootPane.getHeight());
                            deleteButton.setWidth(deltaDragPos.get() - 15.0);

                            deletePane.getChildren().removeIf(x -> Objects.equals(x.getId(), "text"));
                            Text deleteText = new Text("Delete");
                            deleteText.setId("text");
                            deleteText.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C3E50;");
                            deleteText.setFont(new Font("Arial serif", 32));
                            deleteText.setText(computeMaximumText(deleteText, deltaDragPos.get() - 10));
                            deleteText.setLayoutX((deltaDragPos.get() - deleteText.getLayoutBounds().getWidth() - 15) / 2);
                            deleteText.setLayoutY((rootPane.getHeight() + 32) / 2);

                            if(deletePane.getOnMouseEntered() == null) {
                                deletePane.setOnMouseEntered(event1 -> {
                                    deleteText.setStyle("-fx-font-weight: bold; -fx-text-fill: #EAECEE");
                                    Animation animation = AnimationFactory.generateFillTransition(
                                            deleteButton,
                                            Interpolator.EASE_IN,
                                            Duration.millis(150),
                                            "-fx-fill: ",
                                            9.0,
                                            92.0,
                                            5.0,
                                            -53.0
                                    );
                                    animation.play();
                                });
                                deletePane.setOnMouseEntered(event2 -> {
                                    Animation animation = AnimationFactory.generateFillTransition(
                                            deleteButton,
                                            Interpolator.EASE_OUT,
                                            Duration.millis(150),
                                            "-fx-border-color-fx-background-color: ",
                                            14,
                                            39,
                                            -5.0,
                                            53.0
                                    );
                                    animation.play();
                                });
                            }

                            deletePane.getChildren().add(deleteText);
                        }
                        else{
                            rootPane.setTranslateX((finalDragPos.get() - initialDragPos.get()));
                        }
                    }
                    deltaDragPos.set((finalDragPos.get() - initialDragPos.get()));
                }
            }
        });
        rootPane.setOnMouseReleased(event -> {
            if(dragging) {
                initialDragPos.set(Double.MAX_VALUE);
                finalDragPos.set(Double.MAX_VALUE);
                deltaDragPos.set(Double.MAX_VALUE);

                TranslateTransition transition = new TranslateTransition();
                transition.setNode(rootPane);
                transition.setFromX(rootPane.getTranslateX());
                transition.setToX(rootPane.getLayoutX());
                transition.setDuration(Duration.millis(50));
                transition.play();
            }
            else{
                //TODO: handle displaying info
                System.out.println("no drag detected");
            }
        });


    }

    private String computeMaximumText(Text text, double size){
        String s = text.getText();
        while(text.getLayoutBounds().getWidth() > size){
            s = s.substring(0, s.length() - 1);
            text.setText(s);
        }
        return s;
    }
}
