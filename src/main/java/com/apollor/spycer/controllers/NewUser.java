package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class NewUser {

    @FXML private VBox rootPane;
    @FXML private Label spycerTitleLabel;
    @FXML private GridPane createAccountForm;
    @FXML private  TextField displayNameTextField;
    @FXML private  TextField emailTextField;
    @FXML private  PasswordField passwordField;
    @FXML private  PasswordField confirmPasswordField;
    @FXML private  Button createAccountButton;
    @FXML private  Hyperlink loginLink;
    @FXML private  CheckBox lengthCheckBox;
    @FXML private  CheckBox capitalCheckBox;
    @FXML private  CheckBox numberCheckBox;
    @FXML private  CheckBox specialCharacterCheckBox;

    @FXML
    public void initialize() {
        //TODO: make the title static
        loginLink.setOnMouseClicked(event -> {
            Animation animation = AnimationFactory.generateTranslateTransition2(
                    createAccountForm,
                    Interpolator.EASE_OUT,
                    Duration.millis(300),
                    new double[]{-(createAccountForm.getWidth() + Application.rootAnchorPane.getWidth()/2), 0}
            );

            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Login.fxml"));
            VBox pane;
            try{
                pane = loader.load();
                pane.setTranslateX(Application.rootAnchorPane.getWidth());
                pane.setLayoutY(Application.rootBorderPane.getTop().getLayoutBounds().getHeight());
                Application.rootAnchorPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Animation animation2 = AnimationFactory.generateTranslateTransition2(
                    pane,
                    Interpolator.EASE_IN,
                    Duration.millis(300),
                    new double[]{-Application.rootAnchorPane.getWidth(), 0}
            );
            animation2.setOnFinished(finish -> {
                Application.rootAnchorPane.getChildren().remove(pane);
                Application.rootBorderPane.setCenter(pane);
            });
            animation.play();
            animation2.play();
        });
    }
}
