package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.utils.JsonLoader;
import com.apollor.spycer.utils.RecipeUpdater;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    @FXML
    public void initialize(){
        ingredientsList = new HashMap<>();
        proceduresList = new HashMap<>();
        notesList = new HashMap<>();
        tagsList = new HashMap<>();

        addIngredientButton.setOnAction(event -> {
            HBox box = createGroup(0);
            ingredientBox.getChildren().add(box);
        });

        addProcedureButton.setOnAction(event -> {
            HBox box = createGroup(1);
            procedureBox.getChildren().add(box);
        });

        addNotesButton.setOnAction(event -> {
            HBox box = createGroup(2);
            noteBox.getChildren().add(box);
        });

        addTagsButton.setOnAction(event -> {
            if(tagsList.size() < 3){
                HBox box = createGroup(3);
                tagsBox.getChildren().add(box);
            }
        });

        createRecipeButton.setOnAction(event -> {
            try {
                compileJson();
                //TODO: eliminate unnecessary IO
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/Recipe.fxml"));
                BorderPane recipe = loader.load();
                RecipeUpdater.updateRecipe(recipe, JsonLoader.parseJsonRecipe(new File(
                        Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/" +
                                titleTextField.getText() + "_recipe.json")));
                ((VBox) Application.rootBorderPane.getCenter()).getChildren().add(recipe);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Application.rootAnchorPane.getChildren().removeIf(i -> Objects.equals(i.getId(), "recipe_form"));
            Application.rootBorderPane.setEffect(null);
        });

        cancelFormButton.setOnAction(event -> {
            Application.rootAnchorPane.getChildren().removeIf(i -> Objects.equals(i.getId(), "recipe_form"));
            Application.rootBorderPane.setEffect(null);
        });
    }

    public void parseLists(Map<String, String> ingredientsList,
                           Map<String, String> proceduresList,
                           Map<String, String> notesList){

    }

    private HBox createGroup(int type){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxHeight(200);
        box.setSpacing(25);

        Label indentChar = new Label(">");
        indentChar.setFont(new Font(20));
        HBox.setMargin(indentChar, new Insets(0, 0, 0, 10));
        box.getChildren().add(indentChar);

        TextArea input_a = new TextArea();
        input_a.setPrefWidth(315);
        input_a.setMaxHeight(35);
        input_a.setWrapText(true);

        TextArea input_b = new TextArea();
        input_b.setPrefWidth(315);
        input_b.setMaxHeight(35);
        input_b.setWrapText(true);

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

            sp_a.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            sp_b.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            ingredientsList.put(ingredientsList.size(), new String[]{sp_a.getValue(), sp_b.getValue()});
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

            sp_a.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue()}));
            return box;
        }
        else if(type == 3){
            input_a.setPromptText("Tag");
            input_a.setPrefWidth(650);
            box.getChildren().add(input_a);
            box.setId(String.valueOf(tagsList.size()));

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
        Label hrLabel = new Label("H");
        HBox.setMargin(hrLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(hrLabel);
        SimpleStringProperty spHr = new SimpleStringProperty();
        hrInput.textProperty().bindBidirectional(spHr);

        TextArea minInput = createNumericalInput();
        numericalFields.getChildren().add(minInput);
        Label minLabel = new Label("M");
        HBox.setMargin(minLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(minLabel);
        SimpleStringProperty spMin = new SimpleStringProperty();
        minInput.textProperty().bindBidirectional(spMin);

        TextArea secInput = createNumericalInput();
        numericalFields.getChildren().add(secInput);
        Label secLabel = new Label("S");
        secLabel.setFont(new Font(16));
        HBox.setMargin(secLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(secLabel);
        SimpleStringProperty spSec = new SimpleStringProperty();
        secInput.textProperty().bindBidirectional(spSec);

        pane.setRight(numericalFields);

        sp.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue(), spMin.getValue(), spSec.getValue()}));
        spHr.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue(), spMin.getValue(), spSec.getValue()}));
        spMin.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue(), spMin.getValue(), spSec.getValue()}));
        spSec.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue(), spMin.getValue(), spSec.getValue()}));

        return box;
    }

    private TextArea createNumericalInput(){
        TextArea ta = new TextArea();
        ta.setPrefWidth(50);
        ta.setPrefHeight(35);
        ta.setWrapText(true);
        return ta;
    }

    private void compileJson() throws IOException {
        Path curdir = Paths.get("").toAbsolutePath();
        JsonWriter jw = getJsonWriter(curdir);
        jw.beginObject().name("title").value(titleTextField.getText());
        jw.name("ingredients").beginArray();
        for(String[] item : ingredientsList.values()){
            jw.beginObject().name(item[0]).value(item[1]).endObject();
        }
        jw.endArray().name("procedures").beginArray();
        for(String[] item : proceduresList.values()){
            jw.beginObject().name(item[0]).value(item[1]).endObject();
        }
        jw.endArray().name("notes").beginArray();
        int i = 0;
        for(String[] item : notesList.values()){
            jw.beginObject().name("note " + i).value(item[0]).endObject();
            i++;
        }
        jw.endArray().endObject();

        jw.close();
    }

    private JsonWriter getJsonWriter(Path curdir) throws IOException {
        File data = new File(curdir + "/src/main/java/com/apollor/spycer/data/");
        if(!data.exists()){
            if(!data.mkdir()) throw new IOException("Unable to create data directory");
        }

        File f = new File(curdir + "/src/main/java/com/apollor/spycer/data/"+ titleTextField.getText() + "_recipe.json");
        if(!f.createNewFile()){
            int counter = 1;
            while(f.exists()) {
                f = new File(curdir + "/src/main/java/com/apollor/spycer/data/" +
                        f.getName().replace(f.getName().contains("_recipe" + counter) ? "_recipe" + counter : "_recipe",
                        "_recipe" + ++counter));
            }
            if(!f.createNewFile()) throw new IOException("Unable to create file");
        }

        JsonWriter jw = new JsonWriter(new BufferedWriter(new FileWriter(f)));
        jw.setIndent("  ");
        return jw;
    }

    public boolean checkInProgressRecipe() throws IOException {
        Path curdir = Paths.get("").toAbsolutePath();
        File data = new File(curdir + "/src/main/java/com/apollor/spycer/data/");
        if(!data.exists()){
            if(!data.mkdir()) throw new IOException("Unable to create data directory");
        }
        File f = new File(data.getAbsolutePath() + "/inprogress_recipe.json");
        return f.exists();
    }
}
