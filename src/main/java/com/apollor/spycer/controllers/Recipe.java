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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
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
        final double deleteButtonThreshold = 190.0;

        descBox.setStyle(descBoxDefaultStyle);

        AtomicReference<Double> initialDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> finalDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> deltaDragPos = new AtomicReference<>(0.0);

        contentPane.setOnMouseEntered(event -> {
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
        contentPane.setOnMouseExited(event -> {
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
        contentPane.setOnMouseDragged(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                if(initialDragPos.get() == Double.MAX_VALUE){
                    initialDragPos.set(event.getX());
                    finalDragPos.set(Double.MIN_VALUE);
                }
                else if((finalDragPos.get() - initialDragPos.get()) >= deleteButtonThreshold){
                    deleteButton.setWidth(deleteButtonThreshold - 15);
                    contentPane.setDisable(true);
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
                            deleteText.setFill(Color.web("#2C3E50"));
                            deleteText.setStyle("-fx-font-weight: bold;");
                            deleteText.setFont(new Font("Arial serif", 32));
                            deleteText.setText(computeMaximumText(deleteText, deltaDragPos.get() - 10));
                            deleteText.setLayoutX((deltaDragPos.get() - deleteText.getLayoutBounds().getWidth() - 15) / 2);
                            deleteText.setLayoutY((rootPane.getHeight() + 32) / 2);

                            if(deletePane.getOnMouseEntered() == null) {
                                deletePane.setOnMouseEntered(event1 -> {
                                    deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).forEach(x -> ((Text) x).setFill(Color.web("#EAECEE")));
                                    deleteButton.setStroke(Color.web("#3498DB"));
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
                                deletePane.setOnMouseExited(event1 -> {
                                    deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).forEach(x -> ((Text) x).setFill(Color.web("#2C3E50")));
                                    deleteButton.setStroke(Color.BLACK);
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
                TranslateTransition backwardTransition = new TranslateTransition();

                backwardTransition.setNode(rootPane);
                backwardTransition.setFromX(rootPane.getTranslateX());
                backwardTransition.setToX(rootPane.getLayoutX());
                backwardTransition.setDuration(Duration.millis(50));

                if(deltaDragPos.get() > 125 && deltaDragPos.get() < deleteButtonThreshold - 15){
                    contentPane.setDisable(true);
                    Animation animation = AnimationFactory.generateTransformTransition(
                            deleteButton,
                            Interpolator.EASE_IN,
                            Duration.millis(100),
                            new double[]{deleteButtonThreshold - 15 - deleteButton.getWidth(), 0}
                    );

                    Node n = deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).toList().get(0);
                    TranslateTransition animation2 = new TranslateTransition();
                    animation2.setNode(n);
                    animation2.setFromX(n.getLayoutX());
                    animation2.setToX((deleteButtonThreshold - n.getLayoutBounds().getWidth()) / 2);
                    animation2.setInterpolator(Interpolator.EASE_IN);
                    animation2.setDuration(Duration.millis(100));

                    animation.setOnFinished(e -> backwardTransition.play());
                    animation.play();
                    animation2.play();
                }
                else if(deltaDragPos.get() < 125){
                    deletePane.getChildren().removeIf(x -> x.getClass().equals(Text.class));
                    BorderPane.setMargin(deletePane, new Insets(0,0,0,0));
                    Animation animation = AnimationFactory.generateTransformTransition(
                            deleteButton,
                            Interpolator.EASE_OUT,
                            Duration.millis(100),
                            new double[]{-deleteButton.getWidth(), 0}
                    );
                    animation.setOnFinished(e -> backwardTransition.play());
                    animation.play();
                }
                else{
                    backwardTransition.play();
                }
                initialDragPos.set(Double.MAX_VALUE);
                finalDragPos.set(Double.MAX_VALUE);
                deltaDragPos.set(0.0);
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
