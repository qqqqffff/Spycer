package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class Banner {

    @FXML private BorderPane rootPane;
    @FXML private Button closeBannerButton;

    @FXML
    public void initialize(){
        Animation animation = AnimationFactory.generateOpacityTransition(
                rootPane,
                Interpolator.EASE_OUT,
                Duration.millis(250),
                true
        );
        animation.setOnFinished(finish -> Application.rootAnchorPane.getChildren().remove(closeBannerButton.getParent()));
        Thread killThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                return;
            }
            animation.play();
        });
        killThread.start();
        closeBannerButton.setOnAction(event -> {
            animation.play();
            killThread.interrupt();
        });


    }
}
