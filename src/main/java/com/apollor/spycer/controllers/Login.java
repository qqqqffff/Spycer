package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Login {



    @FXML private Text incorrectLoginError;
    @FXML private Button loginButton;
    @FXML private Hyperlink createAccountLink;

    @FXML private Button viewPasswordButton;
    @FXML private TextField passwordTextField;
    @FXML private PasswordField passwordField;

    @FXML private CheckBox stayLoggedInCheckBox;
    @FXML private GridPane loginForm;

    @FXML private Text emailErrorText;
    @FXML private TextField emailTextField;

    private Thread emailErrorThread;
    private Thread loginErrorThread;

    @FXML
    public void initialize(){
        Image viewImg = new Image(Objects.requireNonNull(Application.class.getResource("images/view_icon.png")).toString());
        Image noViewImg = new Image(Objects.requireNonNull(Application.class.getResource("images/no_view_icon.png")).toString());

        //TODO: clear state when logging in
        createAccountLink.setOnMouseClicked(event -> {
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

        emailTextField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) loginButton.fire();
        });
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField));
        emailTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[\\w~!$%^&@*_=+}{?.-]*")){
                emailTextField.setText(o == null ? "" : o);
                displayError("Email address contains invalid characters", emailErrorText, emailErrorThread);
            }
            else if(n.length() > 255){
                emailTextField.setText(n.substring(0,255));
                displayError("Email address is too long (255 max characters)", emailErrorText, emailErrorThread);
            }
            else if(n.length() < 255 && n.matches("[\\w~!$%^&@*_=+}{?.-]*")){
                if(emailErrorThread != null) emailErrorThread.interrupt();
                if(emailErrorText.isVisible()) {
                    emailErrorText.setVisible(false);
                    emailErrorText.setOpacity(1);
                }
            }
        });
        emailTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                emailTextField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        passwordField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) loginButton.fire();
        });
        passwordField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(passwordField));
        passwordField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(passwordField));
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());
        passwordField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                passwordField.setStyle("-fx-background-color: -primary-color;");
            }
        });
        passwordTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                passwordTextField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        viewPasswordButton.setOpacity(0.5);
        viewPasswordButton.setOnMouseEntered(event -> {
            Animation animation = AnimationFactory.generateOpacityTransition2(
                    viewPasswordButton,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    false,
                    0.25
            );
            animation.setOnFinished(finish -> viewPasswordButton.setOpacity(0.75));
            animation.play();
        });
        viewPasswordButton.setOnMouseExited(event -> {
            Animation animation = AnimationFactory.generateOpacityTransition2(
                    viewPasswordButton,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    true,
                    0.25
            );
            animation.setOnFinished(finish -> viewPasswordButton.setOpacity(0.5));
            animation.play();
        });
        viewPasswordButton.setOnAction(event -> {
            System.out.println(passwordField.getViewOrder());
            System.out.println(passwordTextField.getViewOrder());
            if(passwordField.getViewOrder() == 0){
                //viewing
                ImageView temp = new ImageView(noViewImg);
                temp.setFitHeight(30);
                temp.setFitWidth(45);
                temp.setPreserveRatio(false);

                viewPasswordButton.setGraphic(temp);
                passwordField.setViewOrder(1);
                passwordTextField.setViewOrder(0);
            }
            else{
                //not viewing
                ImageView temp = new ImageView(viewImg);
                temp.setFitHeight(30);
                temp.setFitWidth(45);
                temp.setPreserveRatio(false);

                viewPasswordButton.setGraphic(temp);
                passwordField.setViewOrder(0);
                passwordTextField.setViewOrder(1);
            }
        });


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
                else{
                    displayError("Incorrect email or password", incorrectLoginError, loginErrorThread);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(loginButton, "-fx-font-size: 24;"));
        loginButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(loginButton, "-fx-font-size: 24;"));
    }

    private void displayError(String error, Text t, Thread tr){
        t.setText(error);
        t.setVisible(true);
        Animation animation = AnimationFactory.generateOpacityTransition(
                t,
                Interpolator.EASE_OUT,
                Duration.millis(500),
                true
        );
        animation.setOnFinished(finish -> {
            t.setVisible(false);
            t.setOpacity(1);
        });
        if(tr != null) tr.interrupt();
        tr = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                return;
            }
            animation.play();
        });
        tr.start();
    }
}
