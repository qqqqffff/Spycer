package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.Database;
import com.apollor.spycer.database.Household;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SessionHandler;
import com.apollor.spycer.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.apollor.spycer.controllers.NewUser.countSpaces;
import static com.apollor.spycer.controllers.NewUser.displayError;

public class Profile {
    @FXML private Button homeButton;
    @FXML private Text welcomeText;
    @FXML private Button profilePictureButton;

    @FXML private TextField emailTextField;
    @FXML private Text emailErrorText;

    @FXML private RadioButton tfaEmailRadioButton;
    @FXML private RadioButton tfaAppRadioButton;
    @FXML private RadioButton tfaPhoneRadioButton;
    @FXML private Button connectTFAAppButton;
    @FXML private Button connectTFAEmailButton;
    @FXML private Button connectTFAPhoneButton;

    @FXML private TextField displayNameTextField;
    @FXML private Text displayNameErrorText;

    @FXML private TextField householdTextField;
    @FXML private Button createHouseholdButton;
    @FXML private Button inviteHouseholdButton;
    @FXML private Button manageHouseholdButton;
    @FXML private Text householdErrorText;

    @FXML private ComboBox<String> colorProfileComboBox;
    @FXML private ComboBox<String> unitsComboBox;

    @FXML private Button updateFieldsButton;
    @FXML private Button logoutUserButton;

    private Thread householdErrorThread;
    private Thread emailErrorThread;
    private Thread displayNameErrorThread;

    @FXML
    public void initialize() throws IOException {
        User user = SessionHandler.getLoggedInUser();
        Household household = SessionHandler.getUserHousehold();
        welcomeText.setText(welcomeText.getText().replace("$", user.displayName));

        ImageView userPFP = new ImageView(Database.getUserPFP(user.userId));
        userPFP.setFitWidth(100);
        userPFP.setFitHeight(100);
        profilePictureButton.setGraphic(userPFP);

        emailTextField.setText(user.emailAddress);
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
        emailTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(emailTextField, "-fx-font-size: 16;"));
        emailTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(emailTextField, "-fx-font-size: 16;"));
        householdTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                displayNameTextField.setStyle("-fx-background-color: -primary-color; -fx-font-size: 16;");
            }
        });

        displayNameTextField.setText(user.displayName);
        displayNameTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[A-z ]*")) {
                displayNameTextField.setText(o == null ? "" : o);
                displayError("Only letters and spaces are allowed for display names", displayNameErrorText, displayNameErrorThread);
            }
            else if(countSpaces(n.toCharArray()) || n.matches("\\s+.*")){
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
        displayNameTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(displayNameTextField, "-fx-font-size: 16;"));
        displayNameTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(displayNameTextField, "-fx-font-size: 16;"));
        householdTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                displayNameTextField.setStyle("-fx-background-color: -primary-color; -fx-font-size: 16;");
            }
        });

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

        ToggleGroup tfaGroup = new ToggleGroup();
        tfaGroup.getToggles().add(tfaEmailRadioButton);
        tfaGroup.getToggles().add(tfaAppRadioButton);
        tfaGroup.getToggles().add(tfaPhoneRadioButton);

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

        householdTextField.setText(household == null ? "" : household.householdName);
        householdTextField.textProperty().addListener((l, o, n) -> {
            if(!n.matches("[A-z\\d ]*")) {
                householdTextField.setText(o == null ? "" : o);
                displayError("Only alphanumeric characters are allowed for household names", householdErrorText, householdErrorThread);
            }
            else if(countSpaces(n.toCharArray()) || n.matches("\\s+.*")){
                householdTextField.setText(o == null ? "" : o);
                displayError("No leading or excessive spaces", householdErrorText, householdErrorThread);
            }
            else if(n.length() > 64){
                householdTextField.setText(n.substring(0,64));
                displayError("Display name is too long (64 max characters)", householdErrorText, householdErrorThread);
            }
            else if(n.matches("[A-z ]{0,64}")){
                if(householdErrorThread != null) householdErrorThread.interrupt();
                if(householdErrorText.isVisible()) {
                    householdErrorText.setVisible(false);
                    householdErrorText.setOpacity(1);
                }
            }
        });
        householdTextField.setOnMouseEntered(AnimationFactory.generateDefault2TextFieldMouseEnterAnimation(householdTextField, "-fx-font-size: 16;"));
        householdTextField.setOnMouseExited(AnimationFactory.generateDefault2TextFieldMouseExitAnimation(householdTextField, "-fx-font-size: 16;"));
        householdTextField.focusedProperty().addListener((l, o, n) -> {
            if(!o && n){
                householdTextField.setStyle("-fx-background-color: -primary-color; -fx-font-size: 16;");
            }
        });

        createHouseholdButton.setDisable(household != null);
        createHouseholdButton.setOnAction(action -> {
            String code = validateHousehold(householdTextField.getText());
            if(code.contains("0")){
                Household h = new Household(
                        UUID.randomUUID().toString(),
                        householdTextField.getText(),
                        SessionHandler.getLoggedInUser().userId,
                        "owner",
                        false
                );
                SessionHandler.setUserHousehold(h);
                createHouseholdButton.setDisable(true);
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Banner.fxml"));
                try {
                    Database.postHousehold(h);
                    BorderPane banner = loader.load();
                    banner.setLayoutX((1280 - 600) / 2.0);
                    banner.setLayoutY(100);
                    ((Label) banner.getCenter()).setText("Household Created!");
                    Application.rootAnchorPane.getChildren().add(banner);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(code.contains("1") || code.contains("2")){
                displayError("Invalid household name", householdErrorText, householdErrorThread);
            }
        });
        createHouseholdButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(createHouseholdButton));
        createHouseholdButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(createHouseholdButton));

        inviteHouseholdButton.setOnAction(action -> {

        });
        createHouseholdButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(createHouseholdButton));
        createHouseholdButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(createHouseholdButton));
    }

    private static String validateHousehold(String householdTitle){
        String code = "0";
        if(countSpaces(householdTitle.toCharArray()) || householdTitle.matches("\\s+.*")){
            code = code.replace("0", "");
            code += "1";
        }
        if(!householdTitle.matches("[A-z\\d ]{3,64}")){
            code = code.replace("0", "");
            code += "2";
        }
        return code;
    }
}
