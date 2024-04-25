package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.RecipeUpdater;
import com.apollor.spycer.utils.Statistics;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private boolean deletePaneShown;

    @FXML
    public void initialize(){
        final String descBoxDefaultStyle = "-fx-background-color: #2C3E50";
        final double deleteButtonThreshold = 205.0; //includes margin

        descBox.setStyle(descBoxDefaultStyle);

        AtomicReference<Double> initialDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> finalDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> deltaDragPos = new AtomicReference<>(0.0);

        deletePane.setOnMouseEntered(event -> {
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
        deletePane.setOnMouseExited(event -> {
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
        deletePane.setOnMouseClicked(event -> {
            Animation animation = AnimationFactory.generateOpacityTransition(
                    rootPane,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    true
            );
            animation.setOnFinished(e -> {
                Home.dynamicAdjust(rootPane);
//                ((VBox) Application.rootBorderPane.getCenter()).getChildren().remove(rootPane);
            });
            animation.play();
        });

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
                    initialDragPos.set(event.getSceneX() - rootPane.getTranslateX());
                    finalDragPos.set(Double.MIN_VALUE);
                }
                else {
                    dragging = true;

                    finalDragPos.set(event.getSceneX());
                    deltaDragPos.set((finalDragPos.get() - initialDragPos.get()));

                    if(deltaDragPos.get() > 21 && deltaDragPos.get() < deleteButtonThreshold){
                        deletePaneShown = true;
                        BorderPane.setMargin(deletePane, new Insets(0, 15,0,0));
                        deleteButton.setHeight(rootPane.getHeight());
                        deleteButton.setWidth(deltaDragPos.get() - 15.0);

                        deletePane.getChildren().removeIf(x -> Objects.equals(x.getId(), "text"));

                        Text deleteText = new Text("Delete");
                        deleteText.setId("text");
                        deleteText.setFill(Color.web("#2C3E50"));
                        deleteText.setStyle("-fx-font-weight: bold;");
                        deleteText.setFont(new Font("Arial serif", 32));
                        deleteText.setText(computeMaximumText(deleteText, deltaDragPos.get() - 25));
                        deleteText.setLayoutX((deltaDragPos.get() - deleteText.getLayoutBounds().getWidth() - 15) / 2);
                        deleteText.setLayoutY((rootPane.getHeight() + 32) / 2);
                        deletePane.getChildren().add(deleteText);
                    }
                    else if(deletePaneShown && deltaDragPos.get() < 0 && deleteButton.getWidth() > 0){
                        double deltaWidth = deleteButtonThreshold + deltaDragPos.get();
                        deleteButton.setWidth(deltaWidth);

                        Text deleteText = (Text) deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).toList().get(0);
                        deleteText.setText("Delete");
                        deleteText.setText(computeMaximumText(deleteText, deltaWidth - 25));
                        deleteText.setLayoutX((deltaWidth - deleteText.getLayoutBounds().getWidth() - 15) / 2);
                    }
                }
            }
        });
        contentPane.setOnMouseReleased(event -> {
            if(dragging) {
                if(deltaDragPos.get() >= deleteButtonThreshold){
                    double initialWidth = deleteButton.getWidth();
                    double deltaWidth = deleteButtonThreshold - 15 - initialWidth;
                    Text x = (Text) deletePane.getChildren().stream().filter(y -> y.getClass().equals(Text.class)).toList().get(0);
                    Animation animation = new Transition() {
                        {
                            setCycleDuration(Duration.millis(75));
                            setInterpolator(Interpolator.EASE_OUT);
                        }
                        @Override
                        protected void interpolate(double v) {
                            deleteButton.setWidth(initialWidth + deltaWidth * v);
                            double dw = initialWidth + deltaWidth * v;
                            x.setText("Delete");
                            x.setText(computeMaximumText(x, dw));
                            x.setLayoutX((dw - x.getLayoutBounds().getWidth()) / 2);
                        }
                    };
                    animation.play();
                }
                if(deltaDragPos.get() > 125 && deltaDragPos.get() < deleteButtonThreshold){
                    Animation animation = AnimationFactory.generateTransformTransition(
                            deleteButton,
                            Interpolator.EASE_IN,
                            Duration.millis(100),
                            new double[]{deleteButtonThreshold - 15 - deleteButton.getWidth(), 0}
                    );

                    Text n = (Text) deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).toList().get(0);
                    Animation animation2 = AnimationFactory.generateTranslateTransition(
                            n,
                            Interpolator.EASE_IN,
                            Duration.millis(100),
                            new double[]{((deleteButtonThreshold - n.getLayoutBounds().getWidth() - 15) / 2) - n.getLayoutX(), 0}
                    );
                    animation.play();
                    animation2.play();
                    animation.setOnFinished(e -> {
                        deleteButton.setWidth(deleteButtonThreshold - 15);
                        n.setLayoutX((deleteButtonThreshold - n.getLayoutBounds().getWidth() - 15) / 2);
                    });
                }
                else if(deltaDragPos.get() < 125 && deltaDragPos.get() > 0){
                    deletePaneShown = false;
                    deletePane.getChildren().removeIf(x -> x.getClass().equals(Text.class));
                    BorderPane.setMargin(deletePane, new Insets(0,0,0,0));
                    Animation animation = AnimationFactory.generateTransformTransition(
                            deleteButton,
                            Interpolator.EASE_OUT,
                            Duration.millis(100),
                            new double[]{-deleteButton.getWidth(), 0}
                    );
                    animation.play();
                }
                else if(deltaDragPos.get() < 0 && deltaDragPos.get() > -75){
                    double initialWidth = deleteButtonThreshold + deltaDragPos.get();
                    double deltaWidth = deleteButtonThreshold - 15 - initialWidth;
                    Text x = (Text) deletePane.getChildren().stream().filter(y -> y.getClass().equals(Text.class)).toList().get(0);
                    Animation animation = new Transition() {
                        {
                            setCycleDuration(Duration.millis(75));
                            setInterpolator(Interpolator.EASE_OUT);
                        }
                        @Override
                        protected void interpolate(double v) {
                            deleteButton.setWidth(initialWidth + deltaWidth * v);
                            double dw = initialWidth + deltaWidth * v;
                            x.setText("Delete");
                            x.setText(computeMaximumText(x, dw));
                            x.setLayoutX((dw - x.getLayoutBounds().getWidth() - 15) / 2);
                        }
                    };
                    animation.play();
                }
                else if(deltaDragPos.get() < -75 && deltaDragPos.get() > -deleteButtonThreshold){
                    deletePaneShown = false;
                    double initialWidth = deleteButtonThreshold + deltaDragPos.get();
                    Text x = (Text) deletePane.getChildren().stream().filter(y -> y.getClass().equals(Text.class)).toList().get(0);
                    Animation animation = new Transition() {
                        {
                            setCycleDuration(Duration.millis(75));
                            setInterpolator(Interpolator.EASE_OUT);
                        }
                        @Override
                        protected void interpolate(double v) {
                            BorderPane.setMargin(deletePane, new Insets(0, 15 - 15*v, 0, 0));
                            deleteButton.setWidth(initialWidth - initialWidth * v);
                            double deltaWidth = initialWidth - initialWidth * v;
                            x.setText("Delete");
                            x.setText(computeMaximumText(x, deltaWidth));
                            x.setLayoutX((deltaWidth - x.getLayoutBounds().getWidth() - 15) / 2);
                        }
                    };
                    animation.play();
                }
                else if(deltaDragPos.get() <= -deleteButtonThreshold){
                    Animation animation = new Transition() {
                        {
                            setCycleDuration(Duration.millis(25));
                            setInterpolator(Interpolator.EASE_OUT);
                        }
                        @Override
                        protected void interpolate(double v) {
                            BorderPane.setMargin(deletePane, new Insets(0, 15 - 15*v, 0, 0));
                        }
                    };
                    animation.play();
                }
                initialDragPos.set(Double.MAX_VALUE);
                finalDragPos.set(Double.MAX_VALUE);
                deltaDragPos.set(0.0);
                dragging = false;
            }
            else{
                System.out.println("no drag detected, displaying recipe: " + fnameText.getText());
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/RecipePage.fxml"));
                try{
                    ScrollPane page = loader.load();
                    RecipeUpdater.updateRecipePage(page, fnameText.getText());
                    Application.rootBorderPane.setCenter(page);
                } catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private String computeMaximumText(Text text, double size){
        String s = text.getText();
        while(text.getLayoutBounds().getWidth() > size && !s.isEmpty()){
            s = s.substring(0, s.length() - 1);
            text.setText(s);
        }
        return s;
    }
}
