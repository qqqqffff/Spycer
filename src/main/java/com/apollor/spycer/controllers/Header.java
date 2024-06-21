package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.AnimationFactory;
import com.apollor.spycer.utils.SortParam;
import com.apollor.spycer.utils.StateManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Header {
    @FXML private Button profileButton;
    @FXML private TextField recipeSearchField;
    @FXML private Button createRecipeButton;
    @FXML private Button sortDirectionButton;
    @FXML private ComboBox<String> sortParameterComboBox;

    @FXML
    public void initialize(){
        //todo: replace with icons
        Image ascending = new Image(Objects.requireNonNull(Application.class.getResource("images/up_icon.png")).toString());
        Image descending = new Image(Objects.requireNonNull(Application.class.getResource("images/down_icon.png")).toString());
        AtomicBoolean direction = new AtomicBoolean(true);
        AtomicReference<SortParam> sortParam = new AtomicReference<>(SortParam.BEST);

        createRecipeButton.setOpacity(0.75);
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
        createRecipeButton.setOnMouseEntered(AnimationFactory.generateDefaultImageButtonEnterAnimation(createRecipeButton, "-fx-border-color: -t-contrast-color;"));
        createRecipeButton.setOnMouseExited(AnimationFactory.generateDefaultImageButtonExitAnimation(createRecipeButton, "-fx-border-color: transparent;"));

        recipeSearchField.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(recipeSearchField));
        recipeSearchField.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(recipeSearchField));

        sortDirectionButton.setOpacity(0.75);
        sortDirectionButton.setOnAction(action -> {
            //ascending
            if(direction.get()){
                ImageView temp = new ImageView(descending);
                temp.setFitHeight(30);
                temp.setFitWidth(30);
                temp.setPreserveRatio(false);
                sortDirectionButton.setGraphic(temp);
            }
            //descending
            else{
                ImageView temp = new ImageView(ascending);
                temp.setFitHeight(30);
                temp.setFitWidth(30);
                temp.setPreserveRatio(false);
                sortDirectionButton.setGraphic(temp);
            }
            direction.set(!direction.get());
            Home.sort(sortParam.get(), direction.get(),
                    (VBox) ((GridPane) Application.rootBorderPane.getCenter()).getChildren().stream().filter(x -> x.getClass() == VBox.class).toList().get(0));
        });
        sortDirectionButton.setOnMouseEntered(AnimationFactory.generateDefaultImageButtonEnterAnimation(sortDirectionButton, "-fx-border-color: -t-contrast-color;"));
        sortDirectionButton.setOnMouseExited(AnimationFactory.generateDefaultImageButtonExitAnimation(sortDirectionButton, "-fx-border-color: transparent;"));

        sortParameterComboBox.getItems().addAll(SortParam.sortParameters());
        sortParameterComboBox.setValue(sortParam.get().toString());
        sortParameterComboBox.getSelectionModel().selectedItemProperty().addListener((c, o, n) -> {
            sortParam.set(SortParam.parse(n));
            Home.sort(sortParam.get(), direction.get(),
                    (VBox) ((GridPane) Application.rootBorderPane.getCenter()).getChildren().stream().filter(x -> x.getClass() == VBox.class).toList().get(0));
        });
        sortParameterComboBox.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(sortParameterComboBox));
        sortParameterComboBox.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(sortParameterComboBox));

        profileButton.setOpacity(0.75);
        profileButton.setOnAction(action -> {
            //todo: handle navigation in state manager class
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Profile.fxml"));
            Map<String, Map<String, String>> data = new HashMap<>();
            Map<String, String> fileMap = new HashMap<>();
            Map<String, String> pageMap = new HashMap<>();
            fileMap.put("file", "null");
            pageMap.put("page", "views/Profile.fxml");
            data.put("file", fileMap);
            data.put("page", pageMap);

            try{
                StateManager.updateState(data);
                Application.rootBorderPane.setCenter(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        profileButton.setOnMouseEntered(AnimationFactory.generateDefaultImageButtonEnterAnimation(profileButton, "-fx-border-color: -t-contrast-color;"));
        profileButton.setOnMouseExited(AnimationFactory.generateDefaultImageButtonExitAnimation(profileButton, "-fx-border-color: transparent;"));
    }
}
