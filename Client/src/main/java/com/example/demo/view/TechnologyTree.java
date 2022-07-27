package com.example.demo.view;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.view.model.GraphicTechnology;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class TechnologyTree implements Initializable {

    public ImageView background;
    public AnchorPane pane;
    public Pane upperMapPane;
    private final GraphicTechnology[] graphicTechnologies = new GraphicTechnology[TechnologyType.values().length];
    private MapMoveController mapMoveController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setImage(ImageLoader.get("techBackground"));
        background.setFitHeight(StageController.getScene().getHeight());
        background.setFitWidth(StageController.getScene().getHeight() * 5760 / 1080);
        Platform.runLater(this::runLater);
    }

    public void runLater() {
        mapMoveController = new MapMoveController(pane, upperMapPane, -(StageController.getScene().getHeight() * 5760 / 1080 - StageController.getScene().getWidth()), 0, 0, 0, false, false);
        renderTree();
    }

    private void renderTree() {
        for (int i = 0; i < TechnologyType.values().length; i++)
            graphicTechnologies[i] = new GraphicTechnology(TechnologyType.VALUES.get(i), upperMapPane,null,0,0,null);
        ImageView imageView = new ImageView(ImageLoader.get("back"));
        imageView.setX(20);
        imageView.setY(20);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setOnMouseClicked(event -> {
            StageController.sceneChanger("game.fxml");
        });
        pane.getChildren().add(imageView);


        imageView = new ImageView(ImageLoader.get("search"));
        imageView.setX(35);
        imageView.setY(StageController.getStage().getHeight() * 1 / 4);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        TextField textField = new TextField();
        textField.setPromptText("Type and click on the username");
        textField.setLayoutX(35 + imageView.getFitWidth() + 10);
        textField.setLayoutY(StageController.getStage().getHeight() * 1 / 4);
        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        Platform.runLater(() -> {
            vBox.setLayoutX(textField.getLayoutX());
            vBox.setLayoutY(textField.getLayoutY() + textField.getHeight());
            vBox.setPrefWidth(textField.getWidth());
        });

        textField.setOnKeyTyped(keyEvent -> {
            vBox.getChildren().clear();
            if (textField.getText().length() != 0) {
                int i = 0;
                for (TechnologyType value : TechnologyType.values()) {
                    if (value.toString().toLowerCase(Locale.ROOT).startsWith(textField.getText().toLowerCase(Locale.ROOT))) {
                        Text text = new Text(value.toString());
                        text.setLayoutY(i);
                        text.setCursor(Cursor.HAND);
                        text.setOnMouseClicked(event -> {
                            double xx = -(GraphicTechnology.getXY(value).getKey() - 30);
                            if (xx > mapMoveController.getMaxX())
                                xx = mapMoveController.getMaxX() - 1;
                            if (xx < mapMoveController.getMinX())
                                xx = mapMoveController.getMinX() + 1;
                            mapMoveController.setStartX(xx);
                            upperMapPane.setTranslateX(xx);
                            textField.setText("");
                            vBox.getChildren().clear();
                        });
                        i += 30;
                        vBox.getChildren().add(text);
                    }
                }
            }
        });
        upperMapPane.getChildren().add(vBox);
        upperMapPane.getChildren().add(textField);
//        imageView.setOnMouseClicked(event -> System.out.println("u"));
        upperMapPane.getChildren().add(imageView);
    }
}
