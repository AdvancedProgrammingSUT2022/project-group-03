package com.example.demo;

import com.example.demo.view.StageController;
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
        //This line is nothing. just for testing the git.
        StageController.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }



}