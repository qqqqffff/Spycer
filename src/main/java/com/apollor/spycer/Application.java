package com.apollor.spycer;

import com.apollor.spycer.utils.JsonLoader;
import com.apollor.spycer.utils.RecipeUpdater;
import com.apollor.spycer.utils.StateManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static AnchorPane rootAnchorPane;
    public static BorderPane rootBorderPane;
    public static String stylesheetLink;
    public static final File datadir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/");

    @Override
    public void start(Stage stage) throws IOException {
        stylesheetLink = String.valueOf(Application.class.getResource("styles/Stylesheet.css"));
        if(!datadir.exists()){
            if(!datadir.mkdir()) throw new IOException("Unable to create data directory");
        }


        Map<String, Map<String, String>> state = StateManager.readSate();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Root.fxml"));
        rootAnchorPane = fxmlLoader.load();
        for(Node i : rootAnchorPane.getChildren()){
            if(Objects.equals(i.getId(), "rootBorderPane")) {
                rootBorderPane = (BorderPane) i;
                break;
            }
        }

        if(state != null && !state.get("file").get("file").equals("null")){
            System.out.println("Loading recipe: " + state.get("file").get("file"));
            File recipe = new File(datadir.getAbsolutePath() + "/" + state.get("file").get("file"));
            Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(recipe);
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(state.get("page").get("page")));
            ScrollPane content = loader.load();
            RecipeUpdater.updateRecipePage(content, data);
            rootBorderPane.setCenter(content);
        }

        Scene scene = new Scene(rootAnchorPane, 1280, 800);
        stage.setTitle("Respicy");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("this happens on close");
    }

    public static void main(String[] args) {
        launch();
    }
}