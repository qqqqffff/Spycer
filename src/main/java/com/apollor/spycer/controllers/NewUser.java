package com.apollor.spycer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NewUser {
    public Label spycerTitleLabel;
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
        capitalCheckBox.setOnMouseClicked(null);
    }
}
