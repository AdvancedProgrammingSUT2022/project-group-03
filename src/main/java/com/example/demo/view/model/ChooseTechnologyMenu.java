package com.example.demo.view.model;

import com.example.demo.controller.TechnologyAndProductionController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.view.ImageLoader;
import com.example.demo.view.Panels;
import com.example.demo.view.StageController;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseTechnologyMenu implements Initializable {

    public Pane upperMapPane;
    public ImageView background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Panels.setBackground(background,"treeNoLine");
        Platform.runLater(() -> renderTechnologies(false));

    }

    public void renderTechnologies(boolean fromOutside) {
        upperMapPane.getChildren().removeIf(child -> child != background);
        ArrayList<Technology> technologies = TechnologyAndProductionController.initializeResearchInfo();
        GraphicTechnology[] graphicTechnologies = new GraphicTechnology[technologies.size()];
        Text optionsText = new Text("Possible Researches: ");
        optionsText.setFont(Font.font(25));
        optionsText.setFill(Color.WHITE);
        optionsText.setX(StageController.getStage().getWidth() * 0.4 + GraphicTechnology.getFormatFitWidth() / 2 - optionsText.getLayoutBounds().getWidth() / 2);
        optionsText.setY(StageController.getStage().getHeight() * 0.04);
        upperMapPane.getChildren().add(optionsText);
        Text clickToStart = new Text("(Click on the technologies to start producing them)");
        clickToStart.setFont(Font.font(20));
        clickToStart.setFill(Color.WHITE);
        clickToStart.setX(StageController.getStage().getWidth() * 0.4 + GraphicTechnology.getFormatFitWidth() / 2 - clickToStart.getLayoutBounds().getWidth() / 2);
        clickToStart.setY(StageController.getStage().getHeight() * 0.07);
        upperMapPane.getChildren().add(clickToStart);
        for (int i = 0; i < technologies.size(); i++) {
            double y = GraphicTechnology.getFormatFitHeight() * 1.5 * (i + 0.8);
            graphicTechnologies[i] = new GraphicTechnology(technologies.get(i).getTechnologyType(),
                    upperMapPane, technologies.get(i),
                    StageController.getStage().getWidth() * (4 + Math.floor(y / StageController.getStage().getHeight())) / 10, y, this);
        }
        Text scienceText = new Text("Science: " + GameController.getCivilizations().get(GameController.getPlayerTurn()).collectScience());
        scienceText.setX(10);
        scienceText.setY(StageController.getStage().getHeight() * 0.93);
        scienceText.setFont(Font.font(25));
        scienceText.setFill(Color.WHITE);
        upperMapPane.getChildren().add(scienceText);
        Technology lastTechIndex = null;
        ArrayList<Technology> researches = GameController.getCivilizations().get(GameController.getPlayerTurn()).getResearches();
        for (int i = researches.size() - 1; i >= 0; i--) {
            if (researches.get(i).getRemainedCost() == 0) {
                lastTechIndex = researches.get(i);
                break;
            }
        }
        assert lastTechIndex != null;
        new GraphicTechnology(lastTechIndex.getTechnologyType(), upperMapPane, lastTechIndex,
                StageController.getStage().getWidth() - GraphicTechnology.getFormatFitWidth(),
                StageController.getStage().getHeight() - GraphicTechnology.getFormatFitHeight(), this);
        Text lastProducedTech = new Text("Last Produced Technology:");
        lastProducedTech.setFont(Font.font(25));
        lastProducedTech.setFill(Color.WHITE);
        lastProducedTech.setX(StageController.getStage().getWidth() - GraphicTechnology.getFormatFitWidth());
        lastProducedTech.setY(StageController.getStage().getHeight() - GraphicTechnology.getFormatFitHeight() - 20);
        upperMapPane.getChildren().add(lastProducedTech);

        lastTechIndex = GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology();
        if (lastTechIndex != null) {
            new GraphicTechnology(lastTechIndex.getTechnologyType(), upperMapPane, lastTechIndex,
                    0, StageController.getStage().getHeight() * 0.04, null);
        }


        Text gettingResearchedText = new Text("Getting Researched Technology:");
        if(lastTechIndex==null)
            gettingResearchedText.setText("No technologies are getting researched");
        gettingResearchedText.setFont(Font.font(30));
        gettingResearchedText.setFill(Color.WHITE);
        gettingResearchedText.setX(10);
        gettingResearchedText.setY(25);
        upperMapPane.getChildren().add(gettingResearchedText);

        Panels.setBackButton(upperMapPane,StageController.getStage().getWidth() - 40);
    }

}
