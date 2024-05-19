package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SortParam;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;

import java.io.IOException;

public class Header {
    @FXML private Button profileButton;
    @FXML private TextField recipeSearchField;
    @FXML private Button createRecipeButton;
    @FXML private Button sortDirectionButton;
    @FXML private ComboBox<SortParam> sortParameterComboBox;

    @FXML
    public void initialize(){
        String ascending = "↑";
        String descending = "↓";
        SortParam defaultSortBy = SortParam.NAME;

        createRecipeButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/RecipeForm.fxml"));
            try {
                Application.rootBorderPane.setEffect(new GaussianBlur());
                Application.rootBorderPane.setDisable(true);

                ScrollPane form = fxmlLoader.load();
                form.setId("recipe_form");
                form.setLayoutX((1280-794) / 2.0);
                form.setLayoutY(50);
                Application.rootAnchorPane.getChildren().add(form);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        createRecipeButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(createRecipeButton));
        createRecipeButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(createRecipeButton));

        recipeSearchField.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(recipeSearchField));
        recipeSearchField.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(recipeSearchField));

        sortDirectionButton.setOnAction(action -> {
            //ascending
            if(sortDirectionButton.getText().equals(ascending)){
                sortDirectionButton.setText(descending);
            }
            //descending
            else{
                sortDirectionButton.setText(ascending);
            }
        });
        sortDirectionButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(sortDirectionButton));
        sortDirectionButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(sortDirectionButton));

        sortParameterComboBox.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(sortParameterComboBox));
        sortParameterComboBox.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(sortParameterComboBox));

        profileButton.setOnAction(action -> {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Profile.fxml"));
            try{
                Application.rootBorderPane.setCenter(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        profileButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(profileButton));
        profileButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(profileButton));
    }
}
