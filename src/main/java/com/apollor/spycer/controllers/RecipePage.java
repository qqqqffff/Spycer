package com.apollor.spycer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecipePage {

    @FXML private Text titleText;
    @FXML private Button editRecipeButton;
    @FXML private Button deleteRecipeButton;
    @FXML private Button settingsButton;
    @FXML private Text authorText;
    @FXML private Text ratingText;

    @FXML private VBox ingredientsBox;
    @FXML private VBox proceduresBox;
    @FXML private VBox notesBox;

    @FXML private Button syncPantryButton;
    @FXML private Button switchProceduresType;

    @FXML
    public void initialize(){

    }
}
