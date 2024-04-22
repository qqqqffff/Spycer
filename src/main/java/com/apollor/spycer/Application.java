package com.apollor.spycer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static AnchorPane rootAnchorPane;
    public static BorderPane rootBorderPane;
    public static String stylesheetLink;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/Home.fxml"));
        rootAnchorPane = fxmlLoader.load();
        for(Node i : rootAnchorPane.getChildren()){
            if(Objects.equals(i.getId(), "homeBorderPane")) {
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