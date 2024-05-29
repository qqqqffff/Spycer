package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
import com.apollor.spycer.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile {


    @FXML private Button homeButton;
    @FXML private Text welcomeText;
    @FXML private ImageView profilePicture;

    @FXML private TextField emailTextField;
    @FXML private Text emailErrorText;

    @FXML private RadioButton tfaEmailRadioButton;
    @FXML private RadioButton tfaAppRadioButton;
    @FXML private RadioButton tfaPhoneRadioButton;
    @FXML private Button connectTFAAppButton;
    @FXML private Button connectTFAEmailButton;
    @FXML private Button connectTFAPhoneButton;

    @FXML private Text displayNameErrorText;
    @FXML private TextField displayNameTextField;

    @FXML private TextField householdTextField;
    @FXML private Button createHouseholdButton;
    @FXML private Button inviteHouseholdButton;
    @FXML private Button manageHouseholdButton;
    @FXML private Text householdErrorText;

    @FXML private ComboBox<String> colorProfileComboBox;
    @FXML private ComboBox<String> unitsComboBox;

    @FXML private Button updateFieldsButton;
    @FXML private Button logoutUserButton;

    //TODO: add validators on fields
    @FXML
    public void initialize(){
        User user = SessionHandler.getLoggedInUser();
        welcomeText.setText(welcomeText.getText().replace("$", user.displayName));

        emailTextField.setText(user.emailAddress);
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField, "-fx-font-size: 16;"));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField, "-fx-font-size: 16;"));

        displayNameTextField.setText(user.displayName);
        displayNameTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(displayNameTextField, "-fx-font-size: 16;"));
        displayNameTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(displayNameTextField, "-fx-font-size: 16;"));

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
                    user.emailAddress,
                    user.mfaPhone
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
                loader = new FXMLLoader(Application.class.getResource("views/LoginHeader.fxml"));
                Application.rootBorderPane.setTop(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        logoutUserButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(logoutUserButton));
        logoutUserButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(logoutUserButton));

        connectTFAEmailButton.setText(user.mfaApp ? "Disconnect" : "Connect");
        connectTFAEmailButton.setDisable(user.mfaApp);
        connectTFAAppButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAAppButton));
        connectTFAAppButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAAppButton));

        connectTFAEmailButton.setText(user.mfaEmail ? "Disconnect" : "Connect");
        connectTFAEmailButton.setDisable(user.mfaEmail);
        connectTFAEmailButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAEmailButton));
        connectTFAEmailButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAEmailButton));

        connectTFAPhoneButton.setText(user.mfaPhone ? "Disconnect" : "Connect");
        connectTFAPhoneButton.setDisable(user.mfaPhone);
        connectTFAPhoneButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(connectTFAPhoneButton));
        connectTFAPhoneButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(connectTFAPhoneButton));

        homeButton.setOnAction(action -> {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
            Map<String, Map<String, String>> data = new HashMap<>();
            Map<String, String> fileMap = new HashMap<>();
            Map<String, String> pageMap = new HashMap<>();
            fileMap.put("file", "null");
            pageMap.put("page", "views/Home.fxml");
            data.put("file", fileMap);
            data.put("page", pageMap);
            try{
                StateManager.updateState(data);
                Application.rootBorderPane.setCenter(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        homeButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(homeButton));
        homeButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(homeButton));
    }
}
