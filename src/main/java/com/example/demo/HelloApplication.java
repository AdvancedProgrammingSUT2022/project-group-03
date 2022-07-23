package com.example.demo;

import com.example.demo.controller.NetworkController;
import com.example.demo.view.StageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    public static final int SERVER_PORT = 1990;
    private static Stage globeStage;
    private static Scene scene;
    private static Pane pane;


    @Override
    public void start(Stage stage) throws IOException {
        try {
            connect();
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
    public void connect(){
        int i;
        System.out.println("connecting...");
        for (i = 0; i < 10; i++) {
            if (!NetworkController.connect()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }else {
                break;
            }
        }
        if(i == 10) {
            System.out.println("connecting failed");
            Platform.exit();
        }
    }


}