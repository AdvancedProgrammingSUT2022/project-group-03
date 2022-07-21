package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.resources.ResourcesTypes;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Diplomacy implements Initializable {

    public Pane upperMapPane;
    public ImageView background;
    private Civilization opponent = null;
    private ScrollPane knownCivilizationsScrollPane = new ScrollPane();
    private ArrayList<ResourcesTypes> opponentsResources = new ArrayList<>();
    private ScrollPane myResourcesScrollPane = new ScrollPane();
    private ScrollPane myOffersScrollPane = new ScrollPane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Panels.setBackground(background, "treeNoLine");
        Platform.runLater(this::runLater);
    }

    private void runLater() {

        //////

        for (Civilization civilization : GameController.getCivilizations()) {
            if(civilization!=GameController.getCivilizations().get(GameController.getPlayerTurn()))
                GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().add(civilization);
        }
        for (int i = 0; i < ResourcesTypes.VALUES.size(); i++) {
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().put(ResourcesTypes.VALUES.get(i),i+5);
        }
        //////
        Panels.setBackButton(upperMapPane, StageController.getStage().getWidth() - 40);
        Panels.addText("You", StageController.getStage().getWidth() * 0.23, StageController.getStage().getHeight() * 2 / 7, 25, javafx.scene.paint.Color.WHITE, upperMapPane);
        AnchorPane knownCivilizationsAnchorPane = new AnchorPane();
        for (int i = 0; i < GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().size(); i++)
        {
            Text text = Panels.addText( GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().get(i).getUser().getNickname(),
                    10, (i)*20 + 5, 17, null, null);
            Label label = Panels.textToLabel(text,knownCivilizationsAnchorPane);
            label.setCursor(Cursor.HAND);
            int finalI = i;
            label.setOnMouseClicked(event -> {
                opponent = GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().get(finalI);
                setOpponentOn();
            });
        }
        knownCivilizationsScrollPane.setContent(knownCivilizationsAnchorPane);
        knownCivilizationsScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.73);
        knownCivilizationsScrollPane.setLayoutY(StageController.getStage().getHeight() * 2 / 7);
        knownCivilizationsScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        knownCivilizationsScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.09);
        upperMapPane.getChildren().add(knownCivilizationsScrollPane);
//        Panels.addText("You",StageController.getStage().getWidth()*0.73, StageController.getStage().getHeight()*2/7,25,upperMapPane);
    }

    private void setOpponentOn()
    {
        if(opponent!=null)
        {
            upperMapPane.getChildren().remove(knownCivilizationsScrollPane);
            Panels.addText(opponent.getUser().getNickname(),
                    StageController.getStage().getWidth() * 0.73,
                    StageController.getStage().getHeight() * 2 / 7,
                    25, javafx.scene.paint.Color.WHITE,  upperMapPane);
            ArrayList<Pair<ResourcesTypes,Integer>> myResources = new ArrayList<>();
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().forEach((k,v)->{
                myResources.add(new Pair<>(k,v));
            });
            myResourcesScrollPane = new ScrollPane();
            AnchorPane myResourcesAnchorPane = new AnchorPane();
            myResourcesScrollPane.setContent(myResourcesAnchorPane);
            myResourcesScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.21);
            myResourcesScrollPane.setLayoutY(StageController.getStage().getHeight() * 2.2 / 7);
            myResourcesScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
            myResourcesScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);
            upperMapPane.getChildren().add(myResourcesScrollPane);
            for (int i = 0; i < myResources.size(); i++) {
                ImageView imageView = new ImageView(ImageLoader.get(myResources.get(i).getKey().toString()));
                imageView.setX(5);
                imageView.setY(25*i+5);
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                Label label = Panels.imageViewToLabel(imageView,myResourcesAnchorPane);
                label.setLayoutX(5);
                label.setLayoutY(25*i+5);
                label.setTooltip(new Tooltip(myResources.get(i).getKey().toString()));
                Panels.addText(": " + myResources.get(i).getValue(),
                        25, (i+1)*25 - 3, 17, null, myResourcesAnchorPane);
                label.setOnMouseClicked(event -> {
                    //add resources to offers
                    //updateMyOffers()
                });
            }
        }
    }
}
