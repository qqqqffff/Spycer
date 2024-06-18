package com.apollor.spycer.controllers;

import com.apollor.spycer.Application;
import com.apollor.spycer.database.User;
import com.apollor.spycer.utils.*;
import com.google.gson.stream.JsonWriter;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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

    private boolean[] removing = new boolean[]{false, false, false};

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
            if(removing[0]){
                removing[0] = false;
                removeIngredientButton.setText("-");
                toggleRemoveButtons(removeIngredientButton, false);
                revertItems(ingredientBox);
            }
            GridPane box = createGroup(0);
            ingredientBox.getChildren().add(box);
        });
        addIngredientButton.setOnMouseEntered(AnimationFactory.generateDefaultButtonMouseEnterAnimation(addIngredientButton));
        addIngredientButton.setOnMouseExited(AnimationFactory.generateDefaultButtonMouseExitAnimation(addIngredientButton));

        removeIngredientButton.setOnAction(event -> {
            removing[0] = !removing[0];
            if(removing[0]){
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
            GridPane box = createGroup(1);
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
            GridPane box = createGroup(2);
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
                GridPane box = createGroup(3);
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

    private GridPane createGroup(int type){
        GridPane box = new GridPane();
        box.setHgap(10);
        box.addColumn(0);
        box.addRow(0);
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPrefHeight(-1);
        rowConstraint.setMinHeight(-1);
        rowConstraint.setMaxHeight(-1);
        box.getRowConstraints().add(rowConstraint);
        ColumnConstraints indentCol = new ColumnConstraints();
        indentCol.setPercentWidth(5);
        indentCol.setPrefWidth(-1);
        indentCol.setMinWidth(-1);
        indentCol.setMaxWidth(50);
        box.getColumnConstraints().add(indentCol);

        Label indentChar = new Label(">");
        indentChar.setId("indentChar");
        indentChar.setFont(new Font(20));
        GridPane.setHalignment(indentChar, HPos.RIGHT);
        box.add(indentChar,0,0);

        if(type == 0){
            TextField input_a = new TextField();
            input_a.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_a));
            input_a.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_a));

            TextField input_b = new TextField();
            input_b.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_b));
            input_b.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_b));

            SimpleStringProperty sp_a = new SimpleStringProperty();
            SimpleStringProperty sp_b = new SimpleStringProperty();
            input_a.textProperty().bindBidirectional(sp_a);
            input_b.textProperty().bindBidirectional(sp_b);

            ColumnConstraints nameCol = new ColumnConstraints();
            nameCol.setPercentWidth(65);
            nameCol.setPrefWidth(-1);
            nameCol.setMinWidth(-1);
            nameCol.setMaxWidth(-1);

            ColumnConstraints qualityCol = new ColumnConstraints();
            qualityCol.setPercentWidth(30);
            qualityCol.setPrefWidth(-1);
            qualityCol.setMinWidth(-1);
            qualityCol.setMaxWidth(-1);

            input_a.setPromptText("Ingredient Name");
            input_b.setPromptText("Quantity");

            box.addColumn(1);
            box.addColumn(2);
            box.getColumnConstraints().add(nameCol);
            box.getColumnConstraints().add(qualityCol);
            box.add(input_a, 1, 0);
            box.add(input_b, 2, 0);

            box.setId(String.valueOf(ingredientsList.size()));
            ingredientsList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            sp_b.addListener(change -> ingredientsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue(), sp_b.getValue()}));
            return box;
        }

        else if(type == 1){
            //TODO: dynamic resizing of the TA based on wrap cols
            TextArea input_a = new TextArea();
            input_a.setPrefHeight(50);
            input_a.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_a));
            input_a.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_a));

            ColumnConstraints procedureCol = new ColumnConstraints();
            procedureCol.setPercentWidth(60);
            procedureCol.setPrefWidth(-1);
            procedureCol.setMinWidth(-1);
            procedureCol.setMaxWidth(-1);

            ColumnConstraints timeCol = new ColumnConstraints();
            timeCol.setPercentWidth(35);
            timeCol.setPrefWidth(-1);
            timeCol.setMinWidth(-1);
            timeCol.setMaxWidth(-1);

            SimpleStringProperty sp_a = new SimpleStringProperty();
            input_a.textProperty().bindBidirectional(sp_a);
            input_a.setPromptText("Procedure Name");

            box.addColumn(1);
            box.addColumn(2);
            box.getColumnConstraints().add(procedureCol);
            box.getColumnConstraints().add(timeCol);
            box.add(input_a, 1, 0);

            return procedureBox(box, sp_a);
        }
        else if(type == 2){
            //TODO: dynamic resizing of the TA based on wrap cols
            TextArea input_a = new TextArea();
            input_a.setPrefHeight(50);
            input_a.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_a));
            input_a.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_a));

            SimpleStringProperty sp_a = new SimpleStringProperty();
            input_a.textProperty().bindBidirectional(sp_a);
            input_a.setPromptText("Note");

            ColumnConstraints noteCol = new ColumnConstraints();
            noteCol.setPercentWidth(95);
            noteCol.setPrefWidth(-1);
            noteCol.setMinWidth(-1);
            noteCol.setMaxWidth(-1);

            box.addColumn(1);
            box.getColumnConstraints().add(noteCol);
            box.add(input_a, 1,0);

            box.setId(String.valueOf(notesList.size()));
            notesList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> notesList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue()}));
            return box;
        }
        else if(type == 3){
            TextField input_a = new TextField();
            input_a.setPrefHeight(50);
            input_a.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(input_a));
            input_a.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(input_a));

            SimpleStringProperty sp_a = new SimpleStringProperty();
            input_a.textProperty().bindBidirectional(sp_a);
            input_a.setPromptText("Tag");

            ColumnConstraints tagCol = new ColumnConstraints();
            tagCol.setPercentWidth(95);
            tagCol.setPrefWidth(-1);
            tagCol.setMinWidth(-1);
            tagCol.setMaxWidth(-1);

            box.addColumn(1);
            box.getColumnConstraints().add(tagCol);
            box.add(input_a, 1,0);

            box.setId(String.valueOf(tagsList.size()));
            tagsList.put(Integer.parseInt(box.getId()), null);

            sp_a.addListener(change -> tagsList.put(Integer.parseInt(box.getId()), new String[]{sp_a.getValue()}));
            return box;
        }
        return null;
    }

    private GridPane procedureBox(GridPane box, SimpleStringProperty sp){
        box.setId(String.valueOf(proceduresList.size()));
        proceduresList.put(Integer.parseInt(box.getId()), null);

        HBox numericalFields = new HBox();
        numericalFields.setAlignment(Pos.CENTER_LEFT);
        TextField hrInput = createNumericalInput("00");
        hrInput.setStyle(hrInput.getStyle() + "-fx-font-size: 16;");
        numericalFields.getChildren().add(hrInput);
        hrInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(hrInput, hrInput.getStyle()));
        hrInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(hrInput, hrInput.getStyle()));

        Label hrLabel = new Label("H");
        HBox.setMargin(hrLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(hrLabel);
        SimpleStringProperty spHr = new SimpleStringProperty();
        hrInput.textProperty().bindBidirectional(spHr);

        TextField minInput = createNumericalInput("00");
        minInput.setStyle(minInput.getStyle() + "-fx-font-size: 16;");
        numericalFields.getChildren().add(minInput);
        minInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(minInput, minInput.getStyle()));
        minInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(minInput, minInput.getStyle()));

        Label minLabel = new Label("M");
        HBox.setMargin(minLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(minLabel);
        SimpleStringProperty spMin = new SimpleStringProperty();
        minInput.textProperty().bindBidirectional(spMin);

        TextField secInput = createNumericalInput("00");
        secInput.setStyle(secInput.getStyle() + "-fx-font-size: 16;");
        numericalFields.getChildren().add(secInput);
        secInput.setOnMouseEntered(AnimationFactory.generateDefaultTextFieldMouseEnterAnimation(secInput, secInput.getStyle()));
        secInput.setOnMouseExited(AnimationFactory.generateDefaultTextFieldMouseExitAnimation(secInput, secInput.getStyle()));

        Label secLabel = new Label("S");
        secLabel.setFont(new Font(16));
        HBox.setMargin(secLabel, new Insets(0,10,0,5));
        numericalFields.getChildren().add(secLabel);
        SimpleStringProperty spSec = new SimpleStringProperty();
        secInput.textProperty().bindBidirectional(spSec);

        box.add(numericalFields, 2, 0);

        sp.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spHr.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spMin.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));
        spSec.addListener(change -> proceduresList.put(Integer.parseInt(box.getId()), new String[]{sp.getValue(), spHr.getValue()+"_h", spMin.getValue()+"_m", spSec.getValue()+"_s"}));

        return box;
    }

    private TextField createNumericalInput(String... params){
        TextField tf = new TextField();
        if(params.length > 0) tf.setPromptText(params[0]);
        tf.textProperty().addListener((c, o, n) -> {
            if(n != null && !n.matches("\\d*")) tf.setText(o);
        });
        tf.setPrefWidth(50);
        tf.setPrefHeight(50);
        return tf;
    }

    //TODO: implement me
    public boolean checkInProgressRecipe() throws IOException {
        File f = new File(Application.datadir.getAbsolutePath() + "/inprogress_recipe.json");
        return f.exists();
    }


    //TODO: handle procedure box special case (indenting)
    private void makeItemsRemovable(VBox box, Map<Integer, String[]> map){
        for(Node n : box.getChildren()){
            if(n.getClass() == GridPane.class){
                ((GridPane) n).getChildren().removeIf(x -> Objects.equals(x.getId(), "indentChar"));

                for(Node i : ((GridPane) n).getChildren()){
                    i.setDisable(true);
                }

                String style = "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #000000;";
                n.setStyle(style);
                n.setOnMouseEntered(event -> {
                    double[] delta = new double[3];
                    for(int i = 0; i < delta.length; i++){
                        delta[i] = AnimationFactory.t_secondary_hsl[i] - AnimationFactory.primary_hsl[i];
                    }
                    Animation animation = AnimationFactory.generateFillTransition(
                            n,
                            Interpolator.EASE_IN,
                            Duration.millis(150),
                            style + "-fx-background-color: ",
                            AnimationFactory.primary_hsl,
                            delta
                    );
                    animation.play();
                });
                n.setOnMouseExited(event -> {
                    double[] delta = new double[3];
                    for(int i = 0; i < delta.length; i++){
                        delta[i] = AnimationFactory.primary_hsl[i] - AnimationFactory.t_secondary_hsl[i];
                    }
                    Animation animation = AnimationFactory.generateFillTransition(
                            n,
                            Interpolator.EASE_IN,
                            Duration.millis(150),
                            style + "-fx-background-color: ",
                            AnimationFactory.t_secondary_hsl,
                            delta
                    );
                    animation.play();
                });
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
                GridPane.setHalignment(deleteItemButton, HPos.RIGHT);
                ((GridPane) n).add(deleteItemButton, 0, 0);
            }
        }
    }

    private void revertItems(VBox box){
        for(Node n : box.getChildren()){
            if(n.getClass() == GridPane.class){
                ((GridPane) n).getChildren().removeIf(x -> Objects.equals(x.getId(), "deleteItemButton"));

                for(Node i : ((GridPane) n).getChildren()){
                    i.setDisable(false);
                }

                n.setStyle("");
                n.setOnMouseClicked(null);
                n.setOnMouseEntered(null);
                n.setOnMouseExited(null);

                Label indentChar = new Label(">");
                indentChar.setId("indentChar");
                indentChar.setFont(new Font(20));
                GridPane.setHalignment(indentChar, HPos.RIGHT);
                ((GridPane) n).add(indentChar, 0, 0);
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
