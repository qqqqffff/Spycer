package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
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
    @FXML private CheckBox passwordMatchCheckBox;

    private Thread displayNameErrorThread;
    private Thread emailErrorThread;
    private Thread passwordErrorThread;

    @FXML
    public void initialize() {
        loginLink.setOnAction(action -> {
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

        displayNameTextField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) createAccountButton.fire();
        });
        displayNameTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[A-z ]*")) {
                displayNameTextField.setText(o == null ? "" : o);
                displayError("Only letters and spaces are allowed for display names", displayNameErrorText, displayNameErrorThread);
            }
            else if(countSpaces(n.toCharArray(),6) || n.matches("\\s+.*")){
                displayNameTextField.setText(o == null ? "" : o);
                displayError("No leading or excessive spaces", displayNameErrorText, displayNameErrorThread);
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

        emailTextField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) createAccountButton.fire();
        });
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
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField));
        emailTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                emailTextField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        passwordField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) createAccountButton.fire();
        });
        passwordField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[\\w!@#$%^&*]*")){
                passwordField.setText(o == null ? "" : o);
                displayError("Password contains invalid characters", passwordErrorText, passwordErrorThread);
            }
            else if(n.length() > 64){
                passwordField.setText(n.substring(0,64));
                displayError("Password is too long (64 max characters)", passwordErrorText, passwordErrorThread);
            }
            else if(n.matches("[\\w!@#$%^&*]*") && n.length() < 64){
                if(passwordErrorThread != null) passwordErrorThread.interrupt();
                if(passwordErrorText.isVisible()) {
                    passwordErrorText.setVisible(false);
                    passwordErrorText.setOpacity(1);
                }
                lengthCheckBox.setSelected(n.length() > 12);
                capitalCheckBox.setSelected(n.matches(".*[A-Z]+.*"));
                numberCheckBox.setSelected(n.matches(".*\\d+.*"));
                specialCharacterCheckBox.setSelected(n.matches(".*[!@#$%^&*]+.*"));
            }
        });
        passwordField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(passwordField));
        passwordField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(passwordField));
        passwordField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                passwordField.setStyle("-fx-background-color: -primary-color;");
            }
        });

        confirmPasswordField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)) createAccountButton.fire();
        });
        confirmPasswordField.textProperty().addListener((l, o, n) -> passwordMatchCheckBox.setSelected(n.matches(passwordField.getText())));
        confirmPasswordField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(confirmPasswordField));
        confirmPasswordField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(confirmPasswordField));
        confirmPasswordField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                confirmPasswordField.setStyle("-fx-background-color: -primary-color;");
            }
            else if(!n && o && !confirmPasswordField.getText().equals(passwordField.getText())){
                displayError("Passwords do not match", passwordErrorText, passwordErrorThread);
            }
        });

        createAccountButton.setOnAction(action -> {
            String code = validate(
                    displayNameTextField.getText(),
                    emailTextField.getText()
            );
            if(code.contains("0")){
                User user;
                try {
                    user = SessionHandler.createUser(
                            emailTextField.getText(),
                            displayNameTextField.getText(),
                            passwordField.getText()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(user == null){
                    displayError("Account with this email already exists", emailErrorText, emailErrorThread);
                }
                else{
                    loginLink.fire();
                }
            }
            if(code.contains("1") || code.contains("2")){
                displayError("Invalid Display Name", displayNameErrorText, displayNameErrorThread);
            }
            if(code.contains("3")){
                displayError("Invalid Email", emailErrorText, emailErrorThread);
            }
            if(code.contains("4")){
                displayError("Invalid Password(s)", passwordErrorText, passwordErrorThread);
            }

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

    private boolean countSpaces(char[] c, int limit){
        for(char i : c){
            if(String.valueOf(i).matches("\\s")) limit--;
            if(limit <= 0) return true;
        }
        return false;
    }

    private String validate(String displayName, String email){
        String code = "0";
        if(countSpaces(displayName.toCharArray(), 6) || displayName.matches("\\s+.*")){
            code = code.replace("0", "");
            code += "1";
        }
        if(!displayName.matches("[A-z ]{3,64}")){
            code = code.replace("0", "");
            code += "2";
        }
        if(!email.matches("[\\w~!$%^&*_=+}{?.-]{1,64}@\\w+.\\w{2,}")){
            code = code.replace("0", "");
            code += "3";
        }
        if(!lengthCheckBox.isSelected() ||
                !capitalCheckBox.isSelected() ||
                !numberCheckBox.isSelected() ||
                !specialCharacterCheckBox.isSelected() ||
                !passwordMatchCheckBox.isSelected()
        ){
            code = code.replace("0", "");
            code += "4";
        }

        return code;
    }
}
