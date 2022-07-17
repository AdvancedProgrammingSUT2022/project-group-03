package com.example.demo;

import com.example.demo.view.CityPanel;
import com.example.demo.view.StageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        try {
            stage.getIcons().add(new Image(String.valueOf(HelloApplication.class.getResource("/com/example/demo/assets/icon.png"))));
        } catch (Exception e) {
            System.out.println("cannot load icon / " + e.getMessage());
        }
        StageController.setStage(stage);
    }

    public static String getResource(String name) {
        return Objects.requireNonNull(HelloApplication.class.getResource(name)).toExternalForm();
    }

    public static void main(String[] args) {
        launch();
    }


}