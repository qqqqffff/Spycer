package com.apollor.spycer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static AnchorPane rootAnchorPane;
    public static BorderPane rootBorderPane;
    public static String stylesheetLink;
    public static final File datadir = new File(Paths.get("").toAbsolutePath() + "/src/main/java/com/apollor/spycer/data/");

    @Override
    public void start(Stage stage) throws IOException {
        if(!datadir.exists()){
            if(!datadir.mkdir()) throw new IOException("Unable to create data directory");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Root.fxml"));
        rootAnchorPane = fxmlLoader.load();
        for(Node i : rootAnchorPane.getChildren()){
            if(Objects.equals(i.getId(), "rootBorderPane")) {
                rootBorderPane = (BorderPane) i;
                break;
            }
        }

        stylesheetLink = String.valueOf(Application.class.getResource("styles/Stylesheet.css"));
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