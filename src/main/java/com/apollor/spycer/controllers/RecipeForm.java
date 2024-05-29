package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.*;
import com.google.gson.stream.JsonWriter;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecipeForm {
    public RecipeForm(){
        try {
            if(checkInProgressRecipe()){

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private TextField titleTextField;
    @FXML private Slider ratingSlider;
    @FXML private Button addIngredientButton;
    @FXML private Button removeIngredientButton;
    @FXML private Button addProcedureButton;
    @FXML private Button removeProcedureButton;
    @FXML private Button addNotesButton;
    @FXML private Button removeNotesButton;
    @FXML private Button addTagsButton;
    @FXML private Button removeTagsButton;

    @FXML private VBox ingredientBox;
    @FXML private VBox procedureBox;
    @FXML private VBox noteBox;
    @FXML private VBox tagsBox;

    @FXML private Button cancelFormButton;
    @FXML private Button createRecipeButton;

    private Map<Integer, String[]> ingredientsList;
    private Map<Integer, String[]> proceduresList;
    private Map<Integer, String[]> notesList;
    private Map<Integer, String[]> tagsList;

    private File recipeFile;

    @FXML
    public void initialize(){
        ingredientsList = new HashMap<>();
        proceduresList = new HashMap<>();
        notesList = new HashMap<>();
        tagsList = new HashMap<>();

        //TODO: make it so that you can lists at a time from multiple
        //TODO: make it so that adding a new item turns off remove mode
        //TODO: fix formatting of cancel and create button when scroll overflow

        titleTextField.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(titleTextField));
        titleTextField.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(titleTextField));

        addIngredientButton.setOnAction(event -> {
            HBox box = createGroup(0);
            ingredientBox.getChildren().add(box);
        });
        addIngredientButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(addIngredientButton));
        addIngredientButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(addIngredientButton));

        removeIngredientButton.setOnAction(event -> {
            if(removeIngredientButton.getText().equals("-")){
                removeIngredientButton.setText("✔");
                toggleRemoveButtons(removeIngredientButton, true);
                makeItemsRemovable(ingredientBox, ingredientsList);
            }
            else{
                removeIngredientButton.setText("-");
                toggleRemoveButtons(removeIngredientButton, false);
                revertItems(ingredientBox);
            }
        });
        removeIngredientButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(removeIngredientButton));
        removeIngredientButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(removeIngredientButton));


        addProcedureButton.setOnAction(event -> {
            HBox box = createGroup(1);
            procedureBox.getChildren().add(box);
        });
        addProcedureButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(addProcedureButton));
        addProcedureButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(addProcedureButton));

        removeProcedureButton.setOnAction(event -> {
            if(removeProcedureButton.getText().equals("-")){
                removeProcedureButton.setText("✔");
                toggleRemoveButtons(removeProcedureButton, true);
                makeItemsRemovable(procedureBox, proceduresList);
            }
            else{
                removeProcedureButton.setText("-");
                toggleRemoveButtons(removeProcedureButton, false);
                revertItems(procedureBox);
            }
        });
        removeProcedureButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(removeProcedureButton));
        removeProcedureButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(removeProcedureButton));

        addNotesButton.setOnAction(event -> {
            HBox box = createGroup(2);
            noteBox.getChildren().add(box);
        });
        addNotesButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(addNotesButton));
        addNotesButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(addNotesButton));

        removeNotesButton.setOnAction(event -> {
            if(removeNotesButton.getText().equals("-")){
                removeNotesButton.setText("✔");
                toggleRemoveButtons(removeNotesButton, true);
                makeItemsRemovable(noteBox, notesList);
            }
            else{
                removeNotesButton.setText("-");
                toggleRemoveButtons(removeNotesButton, false);
                revertItems(noteBox);
            }
        });
        removeNotesButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(removeNotesButton));
        removeNotesButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(removeNotesButton));

        addTagsButton.setOnAction(event -> {
            if(tagsList.size() < 3){
                HBox box = createGroup(3);
                tagsBox.getChildren().add(box);
            }
            if(tagsList.size() == 3){
                addTagsButton.setOpacity(0);
                addTagsButton.setDisable(true);
            }
        });
        addTagsButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(addTagsButton));
        addTagsButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(addTagsButton));

        removeTagsButton.setOnAction(event -> {
            if(removeTagsButton.getText().equals("-")){
                removeTagsButton.setText("✔");
                toggleRemoveButtons(removeTagsButton, true);
                makeItemsRemovable(tagsBox, tagsList);
            }
            else{
                removeTagsButton.setText("-");
                toggleRemoveButtons(removeTagsButton, false);
                revertItems(tagsBox);
            }
        });
        removeTagsButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(removeTagsButton));
        removeTagsButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(removeTagsButton));

        createRecipeButton.setOnAction(event -> {
            try {

                RecipeHandler.compileRecipe(aggregateRecipe());
                //TODO: eliminate unnecessary IO
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                BorderPane recipe = loader.load();
                RecipeHandler.updateRecipe(recipe, JsonLoader.parseJsonRecipe(recipeFile), recipeFile.getName());
                ((VBox) Application.rootBorderPane.getCenter()).getChildren().add(recipe);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            Application.rootAnchorPane.getChildren().removeIf(i -> Objects.equals(i.getId(), "recipe_form"));
            Application.rootBorderPane.setEffect(null);
            Application.rootBorderPane.setDisable(false);
        });
        createRecipeButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(createRecipeButton));
        createRecipeButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(createRecipeButton));

        cancelFormButton.setOnAction(event -> {
            //TODO: add in progress recipe saving
            Application.rootAnchorPane.getChildren().removeIf(i -> Objects.equals(i.getId(), "recipe_form"));
            Application.rootBorderPane.setEffect(null);
            Application.rootBorderPane.setDisable(false);
        });
        cancelFormButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(cancelFormButton));
        cancelFormButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(cancelFormButton));
    }

    //TODO: validate inputs
    public Map<String, Map<Integer, String[]>> aggregateRecipe(){
        Map<String, Map<Integer, String[]>> data = new HashMap<>();
        data.put("ingredients", ingredientsList);
        data.put("procedures", proceduresList);
        data.put("notes", notesList);
        data.put("tags", tagsList);
        Map<Integer, String[]> optionsList = new HashMap<>();
        optionsList.put(0, new String[]{titleTextField.getText()});
        optionsList.put(1, new String[]{String.format("%.1f", ratingSlider.getValue())});
        optionsList.put(2, new String[]{SessionHandler.getLoggedInUser().displayName});
        data.put("options", optionsList);

        return data;
    }

    private HBox createGroup(int type){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxHeight(200);
        box.setSpacing(25);

        Label indentChar = new Label(">");
        indentChar.setId("indentChar");
        indentChar.setFont(new Font(20));
        HBox.setMargin(indentChar, new Insets(0, 0, 0, 10));
        box.getChildren().add(indentChar);

        TextArea input_a = new TextArea();
        input_a.setPrefWidth(315);
        input_a.setMaxHeight(35);
        input_a.setWrapText(true);
        input_a.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_a));
        input_a.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_a));

        TextArea input_b = new TextArea();
        input_b.setPrefWidth(315);
        input_b.setMaxHeight(35);
        input_b.setWrapText(true);
        input_b.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_b));
        input_b.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_b));

        SimpleStringProperty sp_a = new SimpleStringProperty();
        SimpleStringProperty sp_b = new SimpleStringProperty();
        input_a.textProperty().bindBidirectional(sp_a);
        input_b.textProperty().bindBidirectional(sp_b);

        if(type == 0){
            input_a.setPromptText("Ingredient Name");
            input_b.setPromptText("Quantity");
            box.getChildren().add(input_a);
            box.getChildren().add(input_b);
            box.setId(String.valueOf(ingredientsList.size()));
            ingredientsList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            sp_b.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            return box;
        }
        else if(type == 1){
            input_a.setPromptText("Procedure");
            return procedureBox(indentChar, input_a, sp_a);
        }
        else if(type == 2){
            input_a.setPromptText("Note");
            input_a.setPrefWidth(650);
            box.getChildren().add(input_a);
            box.setId(String.valueOf(notesList.size()));
            notesList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue()}));
            return box;
        }
        else if(type == 3){
            input_a.setPromptText("Tag");
            input_a.setPrefWidth(650);
            box.getChildren().add(input_a);
            box.setId(String.valueOf(tagsList.size()));
            tagsList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> tagsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue()}));
            return box;
        }
        return null;
    }

    private HBox procedureBox(Label indentChar, TextArea field, SimpleStringProperty sp){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        field.setPrefWidth(400);
        box.setId(String.valueOf(tagsList.size()));
        proceduresList.put(Integer.parseInt(box.getId()), null);
        box.setMaxHeight(200);
        box.getChildren().add(indentChar);
        HBox.setMargin(indentChar, new Insets(0, 25, 0, 10));
        BorderPane pane = new BorderPane();
        box.getChildren().add(pane);
        BorderPane.setMargin(field, new Insets(0,25,0,0));
        pane.setLeft(field);

        HBox numericalFields = new HBox();
        numericalFields.setAlignment(Pos.CENTER_LEFT);
        TextArea hrInput = createNumericalInput();
        numericalFields.getChildren().add(hrInput);
        hrInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(hrInput));
        hrInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(hrInput));

        Label hrLabel = new Label("H");
        HBox.setMargin(hrLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(hrLabel);
        SimpleStringProperty spHr = new SimpleStringProperty();
        hrInput.textProperty().bindBidirectional(spHr);

        TextArea minInput = createNumericalInput();
        numericalFields.getChildren().add(minInput);
        minInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(minInput));
        minInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(minInput));

        Label minLabel = new Label("M");
        HBox.setMargin(minLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(minLabel);
        SimpleStringProperty spMin = new SimpleStringProperty();
        minInput.textProperty().bindBidirectional(spMin);

        TextArea secInput = createNumericalInput();
        numericalFields.getChildren().add(secInput);
        secInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(secInput));
        secInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(secInput));

        Label secLabel = new Label("S");
        secLabel.setFont(new Font(16));
        HBox.setMargin(secLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(secLabel);
        SimpleStringProperty spSec = new SimpleStringProperty();
        secInput.textProperty().bindBidirectional(spSec);

        pane.setRight(numericalFields);

        sp.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spHr.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spMin.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spSec.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));

        return box;
    }

    private TextArea createNumericalInput(){
        TextArea ta = new TextArea();
        ta.setPrefWidth(50);
        ta.setPrefHeight(35);
        ta.setWrapText(true);
        return ta;
    }

    //TODO: implement me
    public boolean checkInProgressRecipe() throws IOException {
        File f = new File(Application.datadir.getAbsolutePath() + "/inprogress_recipe.json");
        return f.exists();
    }


    //TODO: handle procedure box special case (indenting)
    private void makeItemsRemovable(VBox box, Map<Integer, String[]> map){
        for(Node n : box.getChildren()){
            if(n.getClass() == HBox.class){
                ((HBox) n).getChildren().removeIf(x -> Objects.equals(x.getId(), "indentChar"));

                for(Node i : ((HBox) n).getChildren()){
                    i.setDisable(true);
                }

                n.setStyle("-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #000000;");
                n.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(n));
                n.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(n));
                n.setOnMouseClicked(event -> {
                    map.remove(Integer.parseInt(n.getId()));
                    shiftMap(map);
                    box.getChildren().remove(n);
                });

                Button deleteItemButton = new Button("-");
                deleteItemButton.setId("deleteItemButton");
                deleteItemButton.setOnAction(action -> {
                    map.remove(Integer.parseInt(n.getId()));
                    shiftMap(map);
                    box.getChildren().remove(n);
                });
                HBox.setMargin(deleteItemButton, new Insets(0, 0, 0, 10));
                ((HBox) n).getChildren().add(0, deleteItemButton);
            }
        }
    }

    private void revertItems(VBox box){
        for(Node n : box.getChildren()){
            if(n.getClass() == HBox.class){
                ((HBox) n).getChildren().removeIf(x -> Objects.equals(x.getId(), "deleteItemButton"));

                for(Node i : ((HBox) n).getChildren()){
                    i.setDisable(false);
                }

                n.setStyle("");
                n.setOnMouseClicked(null);
                n.setOnMouseEntered(null);
                n.setOnMouseExited(null);

                Label indentChar = new Label(">");
                indentChar.setId("indentChar");
                indentChar.setFont(new Font(20));
                HBox.setMargin(indentChar, new Insets(0, 0, 0, 10));
                ((HBox) n).getChildren().add(0, indentChar);
            }
        }
    }

    private void toggleRemoveButtons(Button exception, boolean state){
        if(!exception.equals(removeIngredientButton)){
            removeIngredientButton.setDisable(state);
        }
        if(!exception.equals(removeProcedureButton)){
            removeProcedureButton.setDisable(state);
        }
        if(!exception.equals(removeNotesButton)){
            removeNotesButton.setDisable(state);
        }
        if(!exception.equals(removeTagsButton)){
            removeTagsButton.setDisable(state);
        }
    }

    private void shiftMap(Map<Integer, String[]> map){
        int counter = 0;
        for(int i : map.keySet()){
            if(counter < i){
                String[] temp = map.get(i);
                map.remove(i);
                map.put(i - (i - counter), temp);
            }
            counter++;
        }
    }
}
