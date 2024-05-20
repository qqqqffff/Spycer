package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.Session;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
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

public class Login {

    @FXML private VBox rootPane;
    @FXML private Label spycerTitleLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink createAccountLink;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailTextField;
    @FXML private CheckBox stayLoggedInCheckBox;
    @FXML private GridPane loginForm;

    @FXML
    public void initialize(){
        //TODO: clear state when logging in
        //TODO: make the title static
        createAccountLink.setOnMouseClicked(event -> {
            //TODO: display animation
            Animation animation = AnimationFactory.generateTranslateTransition2(
                    loginForm,
                    Interpolator.EASE_OUT,
                    Duration.millis(300),
                    new double[]{loginForm.getWidth() + Application.rootAnchorPane.getWidth()/2, 0}
            );

            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/NewUser.fxml"));
            VBox pane;
            try {
                pane = loader.load();
                pane.setTranslateX(-Application.rootAnchorPane.getWidth());
                pane.setLayoutY(Application.rootBorderPane.getTop().getLayoutBounds().getHeight());
                Application.rootAnchorPane.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Animation animation2 = AnimationFactory.generateTranslateTransition2(
                    pane,
                    Interpolator.EASE_IN,
                    Duration.millis(300),
                    new double[]{Application.rootAnchorPane.getWidth(), 0}
            );
            animation2.setOnFinished(finish -> {
                Application.rootAnchorPane.getChildren().remove(pane);
                Application.rootBorderPane.setCenter(pane);
            });
            animation.play();
            animation2.play();
        });

        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField));

        passwordField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(passwordField));
        passwordField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(passwordField));

        loginButton.setOnAction(event -> {
            try {
                if(SessionHandler.attemptLogin(
                        emailTextField.getText(),
                        passwordField.getText(),
                        stayLoggedInCheckBox.isSelected())){
                    FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
                    Application.rootBorderPane.setCenter(loader.load());
                    loader = new FXMLLoader(Application.class.getResource("views/Header.fxml"));
                    Application.rootBorderPane.setTop(loader.load());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(loginButton));
        loginButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(loginButton));
    }
}
