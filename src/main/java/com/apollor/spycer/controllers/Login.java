package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Login {
    public Label spycerTitleLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink createAccountLink;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailTextField;
    @FXML private VBox loginForm;

    @FXML
    public void initialize(){
        createAccountLink.setOnMouseClicked(event -> {
            //TODO: display animation
            Animation animation = AnimationFactory.generateTranslateTransition2(
                    loginForm,
                    Interpolator.EASE_OUT,
                    Duration.millis(250),
                    new double[]{loginForm.getWidth() + Application.rootAnchorPane.getWidth()/2, 0}
            );

            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/NewUser.fxml"));
            VBox form;
            try {
                form = loader.load();
                form.setLayoutX(-Application.rootAnchorPane.getWidth());
                form.getChildren().forEach(node -> {
                    if(Objects.equals(node.getId(), "spycerTitleLabel")){
                        node.setOpacity(0);
                    }
                });
                Application.rootAnchorPane.getChildren().add(form);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Animation animation2 = AnimationFactory.generateTranslateTransition2(
                    form,
                    Interpolator.EASE_IN,
                    Duration.millis(250),
                    new double[]{Application.rootAnchorPane.getWidth(), 0}
            );
            animation.play();
            animation2.play();
        });
    }
}
