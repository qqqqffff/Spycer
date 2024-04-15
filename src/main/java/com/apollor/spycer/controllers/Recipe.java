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
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Recipe {
    @FXML public HBox subHBox;
    @FXML private BorderPane rootPane;
    @FXML private Text recipeTitleText;
    @FXML private Text ratingText;
    @FXML private Text cooktimeText;
    @FXML private ImageView recipeTumbnail;
    @FXML private VBox descBox;
    @FXML private Text fnameText;

    @FXML
    public void initialize(){
        final String descBoxDefaultStyle = "-fx-background-color: #2C3E50";

        descBox.setStyle(descBoxDefaultStyle);
//        rootPane.setOnMouseClicked(System.out::println);


        AtomicReference<Double> initialDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> finalDragPos = new AtomicReference<>(Double.MAX_VALUE);
        AtomicReference<Double> deltaDragPos = new AtomicReference<>(Double.MAX_VALUE);

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
        rootPane.setOnMouseDragged(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                if(initialDragPos.get() == Double.MAX_VALUE){
                    initialDragPos.set(event.getX());
                    finalDragPos.set(Double.MIN_VALUE);
                }
                else if((finalDragPos.get() - initialDragPos.get()) > (rootPane.getWidth() / 4.5)){
                    displayDeleteConfirmation();
                    rootPane.setDisable(true);
                }
                else {
                    finalDragPos.set(event.getX());
                    if(deltaDragPos.get() < (finalDragPos.get() - initialDragPos.get())) {
                        rootPane.setTranslateX((finalDragPos.get() - initialDragPos.get()));

                    }
                    deltaDragPos.set((finalDragPos.get() - initialDragPos.get()));
                }
            }
        });
        rootPane.setOnMouseReleased(event -> {
            initialDragPos.set(Double.MAX_VALUE);
            finalDragPos.set(Double.MAX_VALUE);
            deltaDragPos.set(Double.MAX_VALUE);

            TranslateTransition transition = new TranslateTransition();
            transition.setNode(rootPane);
            transition.setFromX(rootPane.getTranslateX());
            transition.setToX(rootPane.getLayoutX());
            transition.setDuration(Duration.millis(50));
            transition.play();
        });


    }

    //TODO: when settings are implemented allow for users to delete without confirmation
    private void displayDeleteConfirmation(){
        for(Node i : ((VBox) Application.rootBorderPane.getCenter()).getChildren()){
            if(i.equals(rootPane)){
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/DeleteRecipeConfirmation.fxml"));
                try {
                    BorderPane confirmation = loader.load();
                    RecipeDeleter.updateConfirmation(confirmation, recipeTitleText.getText(), rootPane, fnameText.getText());
                } catch (IOException ignored) {

                }
            }
        }
    }
}
