package com.example.demo.view;

import com.example.demo.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageController {
    private static Stage stage;
    private static Scene scene;
    private static Pane pane;



    public static void setStage(Stage stage) {
        StageController.stage = stage;
        stage.setTitle("Civilization Zero");
        try {
            pane = FXMLLoader.load(Objects
                    .requireNonNull(HelloApplication.class.getResource("loginMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();

    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void sceneChanger(String fxmlName) throws IOException {
        pane = FXMLLoader.load(Objects
                .requireNonNull(HelloApplication.class.getResource(fxmlName)));
        scene.setRoot(pane);
    }
    public static void errorMaker(String header, String content,Alert.AlertType type) {
        Alert errorAlert = new Alert(type);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.initOwner(StageController.getStage());
        errorAlert.showAndWait();
    }
}
