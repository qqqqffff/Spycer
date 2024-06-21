package com.apollor.spycer;

import com.apollor.spycer.utils.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static final String baseApplicationLink = "http://localhost:8000";
    public static final Duration defaultTokenExpireTime = Duration.ofDays(30);
    public static AnchorPane rootAnchorPane;
    public static BorderPane rootBorderPane;
    public static String stylesheetLink;
    public static final File datadir = new File(Paths.get("").toAbsolutePath() + "/data/");
    public static final String geolocationKey;

    //TODO: this will be preformed on the backend eventually
    static {
        String tempGeoKey;
        try {
            tempGeoKey = new BufferedReader(new FileReader(Paths.get("").toAbsolutePath() + "/geolocation/key.txt")).readLine().trim();
        } catch (IOException e) {
            System.out.println("geolocation key not found");
            tempGeoKey = null;
        }
        geolocationKey = tempGeoKey;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stylesheetLink = String.valueOf(Application.class.getResource("styles/Stylesheet.css"));
        stylesheetLink = stylesheetLink.substring(stylesheetLink.indexOf("file:") + 5);
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

        try {
            if (!SessionHandler.checkSessionToken()) {
                fxmlLoader = new FXMLLoader(Application.class.getResource("views/Login.fxml"));
                rootBorderPane.setCenter(fxmlLoader.load());
                fxmlLoader = new FXMLLoader(Application.class.getResource("views/LoginHeader.fxml"));
                rootBorderPane.setTop(fxmlLoader.load());
            } else if (state != null && !state.get("file").get("file").equals("null")) {
                System.out.println("Loading recipe: " + state.get("file").get("file"));
                File recipe = new File(datadir.getAbsolutePath() + "/" + state.get("file").get("file"));
                Map<String, Map<Integer, String[]>> data = JsonLoader.parseJsonRecipe(recipe);
                FXMLLoader loader = new FXMLLoader(Application.class.getResource(state.get("page").get("page")));
                ScrollPane content = loader.load();
                RecipeHandler.updateRecipePage(content, data);
                rootBorderPane.setCenter(content);
                loader = new FXMLLoader(Application.class.getResource("views/Header.fxml"));
                rootBorderPane.setTop(loader.load());
            } else if (state != null && !Objects.equals(state.get("page").get("page"), "views/Home.fxml")){
                fxmlLoader = new FXMLLoader(Application.class.getResource(state.get("page").get("page")));
                rootBorderPane.setCenter(fxmlLoader.load());
                fxmlLoader = new FXMLLoader(Application.class.getResource("views/Header.fxml"));
                rootBorderPane.setTop(fxmlLoader.load());
            } else {
                fxmlLoader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
                rootBorderPane.setCenter(fxmlLoader.load());
                fxmlLoader = new FXMLLoader(Application.class.getResource("views/Header.fxml"));
                rootBorderPane.setTop(fxmlLoader.load());
            }
        } catch(IOException ignored){
            //note: exception only occurs with a failed connection to DB, thus skipping login and logging in as a Guest
            //TODO: display to the user that they are currently a guest user
            System.out.println("Logging in as a guest");
            fxmlLoader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
            rootBorderPane.setCenter(fxmlLoader.load());
            fxmlLoader = new FXMLLoader(Application.class.getResource("views/Header.fxml"));
            rootBorderPane.setTop(fxmlLoader.load());
        }

        Scene scene = new Scene(rootAnchorPane, 1280, 800);
        ColorHandler.applyPalette(0);
        stage.setTitle("Spycer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("this happens on close");
    }

    public static void main(String[] args) {
        launch(args);
    }
}