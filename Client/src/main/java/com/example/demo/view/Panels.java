package com.example.demo.view;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Panels {

    public static ImageView setBackButton(Pane pane, double x) {
        ImageView imageView = new ImageView(ImageLoader.get("back"));
        imageView.setX(StageController.getStage().getWidth() - 40);
        imageView.setY(20);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setOnMouseClicked(event -> {
            StageController.sceneChanger("game.fxml");
        });
        pane.getChildren().add(imageView);
        return imageView;
    }

    public static void setBackground(ImageView background, String imageName) {
        background.setImage(ImageLoader.get(imageName));
        background.setFitHeight(StageController.getScene().getHeight());
        background.setFitWidth(StageController.getScene().getHeight() * 5760 / 1080);
    }

    public static Text addText(String string, double x, double y, int size, javafx.scene.paint.Color color, Pane pane) {
        Text text = new Text(string);
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFont(Font.font(size));
        if (color != null)
            text.setFill(color);
        if (pane != null)
            pane.getChildren().add(text);
        return text;
    }

    public static Button addButton(String string, double x, double y, double w, double h, Pane pane) {
        Button button = new Button(string);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefWidth(w);
        button.setPrefHeight(h);
        if (pane != null)
            pane.getChildren().add(button);
        return button;
    }

    public static Label textToLabel(Text text, Pane pane) {
        Label label = new Label();
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(text);
        label.setLayoutX(text.getLayoutX());
        label.setLayoutY(text.getLayoutY());
        pane.getChildren().add(label);
        return label;
    }

    public static Label textToLabel(Text text, double x, double y, Pane pane) {
        Label label = new Label();
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        pane.getChildren().add(label);
        return label;
    }

    static Label imageViewToLabel(ImageView imageView, Pane pane)
    {
        Label label = new Label();
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(imageView);
        label.setLayoutX(imageView.getLayoutX());
        label.setLayoutY(imageView.getLayoutY());
        label.setPrefWidth(imageView.getFitWidth());
        label.setPrefHeight(imageView.getFitHeight());
        pane.getChildren().add(label);
        return label;
    }




}
