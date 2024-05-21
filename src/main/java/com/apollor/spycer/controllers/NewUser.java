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
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Pattern;


public class NewUser {

    @FXML private Text displayNameErrorText;
    @FXML private Text emailErrorText;
    @FXML private Text passwordErrorText;
    @FXML private VBox rootPane;
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

    private Thread displayNameErrorThread;
    private Thread emailErrorThread;
    private Thread passwordErrorThread;

    private boolean[] passwordChecks = new boolean[]{false, false, false, false, false}; //0=len, 1=capital, 3=number, 4=special, 5=match

    @FXML
    public void initialize() {
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

        displayNameTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[A-z ]*")) {
                displayNameTextField.setText(o == null ? "" : o);
                displayError("Only letters and spaces are allowed for display names", displayNameErrorText, displayNameErrorThread);
            }
            else if(n.matches("\\s{6,64}")){
                displayNameTextField.setText(o == null ? "" : o);
                displayError("That is enough spaces", displayNameErrorText, displayNameErrorThread);
            }
            else if(n.length() > 64){
                displayNameTextField.setText(n.substring(0,64));
                displayError("Display name is too long (64 max characters)", displayNameErrorText, displayNameErrorThread);
            }
            else if(n.matches("[A-z ]{0,64}")){
                if(displayNameErrorThread != null) displayNameErrorThread.interrupt();
                if(displayNameErrorText.isVisible()) {
                    displayNameErrorText.setVisible(false);
                    displayNameErrorText.setOpacity(1);
                }
            }
        });
        displayNameTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(displayNameTextField));
        displayNameTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(displayNameTextField));
        displayNameTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                displayNameTextField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        //TODO submission check to ending part
//        if(!n.matches("[\\w~!$%^&*_=+}{?.-]{1,64}@\\w+.\\w{2,}")){
//            System.out.println("invalid email");
//        }
//        else{
//            System.out.println("valid email");
//        }
        emailTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[\\w~!$%^&*_=+}{?.-]*")){
                emailTextField.setText(o == null ? "" : o);
                displayError("Email address contains invalid characters", emailErrorText, emailErrorThread);
            }
            if(n.length() > 255){
                emailTextField.setText(n.substring(0,255));
                displayError("Email address is too long (255 max characters)", emailErrorText, emailErrorThread);
            }
        });
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField));
        emailTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                emailTextField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        passwordField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[\\w!@#$%^&*]*")){
                passwordField.setText(o == null ? "" : o);
                displayError("Password contains invalid characters", emailErrorText, emailErrorThread);
            }
            else if(n.length() > 64){
                passwordField.setText(n.substring(0,64));
                displayError("Password is too long (64 max characters)", passwordErrorText, passwordErrorThread);
            }

//            if(n.matches())
        });
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
