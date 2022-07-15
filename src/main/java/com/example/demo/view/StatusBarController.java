package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.technologies.Technology;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StatusBarController {
    private final ImageView GOLD_IMAGE = new ImageView(ImageLoader.get("gold"));
    private final ImageView SCIENCE_IMAGE = new ImageView(ImageLoader.get("science"));
    private final ImageView HAPPINESS_IMAGE = new ImageView(ImageLoader.get("happiness"));
    private final ImageView TECHNOLOGY_IMAGE = new ImageView(ImageLoader.get("technology"));
    private final HBox statusBar;

    private Text goldAmount;
    private Text happinessAmount;
    private Text scienceAmount;
    private Text technologyName;

    public StatusBarController(HBox statusBar) {
        this.statusBar = statusBar;
        updateText();

        statusBar.getChildren().add(GOLD_IMAGE);
        statusBar.getChildren().add(goldAmount);

        statusBar.getChildren().add(HAPPINESS_IMAGE);
        statusBar.getChildren().add(happinessAmount);

        statusBar.getChildren().add(SCIENCE_IMAGE);
        statusBar.getChildren().add(scienceAmount);

        statusBar.getChildren().add(TECHNOLOGY_IMAGE);
        statusBar.getChildren().add(technologyName);
    }

    public void updateText() {
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        goldAmount = new Text(civilization.getGold() + "  ");
        happinessAmount = new Text(civilization.getHappiness() + "  ");
        //TODO: does science amount is correct?? :/
        scienceAmount = new Text("0  ");
        Technology tech = civilization.getGettingResearchedTechnology();
        if (tech == null)
            technologyName = new Text("nothing");
        else
            technologyName = new Text(tech.getName());
    }
}
