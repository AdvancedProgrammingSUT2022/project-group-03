package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private static Stage globeStage;
    private static Scene scene;
    private static Pane pane;


    @Override
    public void start(Stage stage) throws IOException {
        globeStage = stage;
        sceneChanger("loginMenu.fxml");
    }

    public static void main(String[] args) {
        launch();
    }


    public static void sceneChanger(String fxmlName) throws IOException {
        globeStage.close();
        pane = FXMLLoader.load(Objects
                .requireNonNull(HelloApplication.class.getResource(fxmlName)));
        scene = new Scene(pane);
        globeStage.setTitle("Civilization Zero");
        globeStage.setScene(scene);
        globeStage.setMaximized(true);
        globeStage.setFullScreen(true);
        globeStage.show();
    }

    public static Scene getScene() {
        return scene;
    }
}