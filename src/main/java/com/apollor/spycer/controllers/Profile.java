package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class Profile {

    @FXML private ImageView profilePicture;
    @FXML private TextField emailTextField;
    @FXML private TextField displayNameTextField;
    @FXML private Text welcomeText;
    @FXML private ComboBox<String> colorProfileComboBox;
    @FXML private Button updateFieldsButton;
    @FXML private Button logoutUserButton;
    @FXML private RadioButton tfaEmailRadioButton;
    @FXML private RadioButton tfaAppRadioButton;
    @FXML private RadioButton tfaPhoneRadioButton;
    @FXML private Button connectTFAAppButton;
    @FXML private Button connectTFAEmailButton;
    @FXML private Button connectTFAPhoneButton;

    //TODO: add validators on fields
    @FXML
    public void initialize(){
        User user = SessionHandler.getLoggedInUser();
        welcomeText.setText(welcomeText.getText().replace("$", user.displayName));

        emailTextField.setText(user.emailAddress);
        //TODO: fix animations
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(emailTextField));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(emailTextField));

        displayNameTextField.setText(user.displayName);
        //TODO: fix animations
        displayNameTextField.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(displayNameTextField));
        displayNameTextField.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(displayNameTextField));

        updateFieldsButton.setOnAction(action -> {
            User updatedUser = new User(
                    user.userId,
                    user.displayName,
                    user.mfaEmail,
                    user.mfaApp,
                    user.verified,
                    user.createdDate,
                    user.hashPW,
                    user.hashSalt,
                    user.emailAddress
            );
            //TODO: display modal to confirm and verify changes
        });
        updateFieldsButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(updateFieldsButton));
        updateFieldsButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(updateFieldsButton));

        logoutUserButton.setOnAction(action -> {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Login.fxml"));
            try{
                SessionHandler.invalidateSession();
                Application.rootBorderPane.setCenter(loader.load());
                Application.rootBorderPane.setTop(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        logoutUserButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(logoutUserButton));
        logoutUserButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(logoutUserButton));

        connectTFAEmailButton.setText(user.mfaApp ? "Connected" : "Connect");
        connectTFAEmailButton.setDisable(user.mfaApp);
        connectTFAAppButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAAppButton));
        connectTFAAppButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAAppButton));

        connectTFAEmailButton.setText(user.mfaEmail ? "Connected" : "Connect");
        connectTFAEmailButton.setDisable(user.mfaEmail);
        connectTFAEmailButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAEmailButton));
        connectTFAEmailButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAEmailButton));

        //TODO: add mfa phone field to db
        connectTFAPhoneButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAPhoneButton));
        connectTFAPhoneButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAPhoneButton));
    }
}
