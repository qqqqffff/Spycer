package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class RecipeDeleter {
    private static Node toDelete;
    private static String fileName;
    public static void updateConfirmation(BorderPane pane, String titleText, Node i, String f){
        toDelete = i;
        fileName = f;

    }
    public static void deleteRecipe(String titleText) throws IOException {
        ((VBox) Application.rootBorderPane.getCenter()).getChildren().removeIf(x -> x.equals(toDelete));
        File recipe_dir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/" + fileName + ".json");
        if(!recipe_dir.delete()){
            throw new IOException("Failed to delete recipe");
        }
    }
}
