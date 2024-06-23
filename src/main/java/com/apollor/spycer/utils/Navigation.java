package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Navigation {
    public static Map<String, Map<String, String>> generateDefaultNavigationData(String... params){
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> fileMap = new HashMap<>();
        Map<String, String> pageMap = new HashMap<>();
        Map<String, String> optionsMap = new HashMap<>();
        params = params == null ? new String[]{"null", "null"} : params;
        fileMap.put("file", params.length < 2 ? "null" : params[1]);
        pageMap.put("page", params[0]);
        data.put("file", fileMap);
        data.put("page", pageMap);
        data.put("options", optionsMap);
        return data;
    }
    public static Node navigate(Map<String, Map<String, String>> data) throws IOException {
        Node pane = new FXMLLoader(Application.class.getResource(data.get("page").get("page"))).load();;
        StateManager.updateState(data);

        if(!data.get("file").get("file").equals("null")){
            RecipeHandler.updateRecipePage((ScrollPane) pane, data.get("file").get("file") + "/recipe.json");
        }

        assert pane != null;
        return pane;
    }
}
