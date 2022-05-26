package com.example.demo.controller;

import com.example.demo.HelloApplication;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainControllerFX {
    @FXML
    public void gameMenu()
    {

    }
    @FXML
    public void profileMenu()
    {

    }
    @FXML
    public void scoreBoard()
    {

    }
    @FXML
    public void chat()
    {

    }
    @FXML
    public void logout() throws IOException {
        HelloApplication.sceneChanger("loginMenu.fxml");
    }

}
