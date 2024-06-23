package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.*;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
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
import java.util.HashMap;
import java.util.Map;
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
        final double deleteButtonThreshold = 205.0; //includes margin

        AtomicReference<Double> initialDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> finalDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> deltaDragPos = new AtomicReference<>(0.0);

        deletePane.setOnMouseEntered(event -> {
            deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).forEach(x -> ((Text) x).setFill(Color.web("#616161")));
            deleteButton.setStroke(Color.web("#616161"));
            double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-tertiary-color")));
            double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-tertiary-color")));

            double[] delta_hsl = new double[3];
            for(int i = 0; i < hsl1.length; i++){
                delta_hsl[i] = hsl2[i] - hsl1[i];
            }
            Animation animation = AnimationFactory.generateFillTransition(
                    deleteButton,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-fill: ",
                    hsl1,
                    delta_hsl
            );
            animation.play();
        });
        deletePane.setOnMouseExited(event -> {
            deletePane.getChildren().stream().filter(x -> x.getClass().equals(Text.class)).forEach(x -> ((Text) x).setFill(Color.web("#000000")));
            deleteButton.setStroke(Color.BLACK);
            double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-tertiary-color")));
            double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-tertiary-color")));

            double[] delta_hsl = new double[3];
            for(int i = 0; i < hsl1.length; i++){
                delta_hsl[i] = hsl1[i] - hsl2[i];
            }
            Animation animation = AnimationFactory.generateFillTransition(
                    deleteButton,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-fill: ",
                    hsl2,
                    delta_hsl
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
                //TODO: when ready for release handle IO deletion
            });
            animation.play();
        });

        contentPane.setOnMouseEntered(event -> {
            double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-secondary-color")));
            double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-secondary-color")));

            double[] delta_hsl = new double[3];
            for(int i = 0; i < hsl1.length; i++){
                delta_hsl[i] = hsl2[i] - hsl1[i];
            }
            Animation animation1 = AnimationFactory.generateFillTransition(
                    contentPane,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    "-fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: -t-contrast-color; -fx-background-radius: 15; -fx-background-color: ",
                    hsl1,
                    delta_hsl
            );
            animation1.play();
        });
        contentPane.setOnMouseExited(event -> {
            double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-secondary-color")));
            double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-secondary-color")));

            double[] delta_hsl = new double[3];
            for(int i = 0; i < hsl1.length; i++){
                delta_hsl[i] = hsl1[i] - hsl2[i];
            }
            Animation animation1 = AnimationFactory.generateFillTransition(
                    contentPane,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-border-radius: 15; -fx-background-radius: 15; -fx-border-width: 3; -fx-border-radius: 15; -fx-border-color: -contrast-color; -fx-background-radius: 15; -fx-background-color: ",
                    hsl2,
                    delta_hsl
            );
            animation1.play();
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
//                Map<String, Map<String, String>> data = new HashMap<>();
//                Map<String, String> fileMap = new HashMap<>();
//                Map<String, String> pageMap = new HashMap<>();
//                fileMap.put("file", fnameText.getText());
//                pageMap.put("page", "views/RecipePage.fxml");
//                data.put("file", fileMap);
//                data.put("page", pageMap);
//
//
//                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/RecipePage.fxml"));
                try{
//                    StateManager.updateState(data);
//                    ScrollPane page = loader.load();
//                    RecipeHandler.updateRecipePage(page, fnameText.getText() + "/recipe.json");
                    Node page = Navigation.navigate(Navigation.generateDefaultNavigationData("views/RecipePage.fxml", fnameText.getText()));
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
