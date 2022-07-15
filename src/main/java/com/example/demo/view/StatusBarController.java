package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.technologies.Technology;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StatusBarController {
    private static final ImageView GOLD_IMAGE = new ImageView(ImageLoader.get("gold"));
    private static final ImageView SCIENCE_IMAGE = new ImageView(ImageLoader.get("science"));
    private static final ImageView HAPPINESS_IMAGE = new ImageView(ImageLoader.get("happiness"));
    private static final ImageView TECHNOLOGY_IMAGE = new ImageView(ImageLoader.get("technology"));
    private static final Text goldAmount = new Text();
    private static final Text scienceAmount = new Text();
    private static final Text happinessAmount = new Text();
    private static final Text technologyName = new Text();

    public static void init(HBox statusBar) {

        update();

        statusBar.getChildren().add(GOLD_IMAGE);
        statusBar.getChildren().add(goldAmount);

        statusBar.getChildren().add(HAPPINESS_IMAGE);
        statusBar.getChildren().add(happinessAmount);

        statusBar.getChildren().add(SCIENCE_IMAGE);
        statusBar.getChildren().add(scienceAmount);

        statusBar.getChildren().add(TECHNOLOGY_IMAGE);
        statusBar.getChildren().add(technologyName);


    }

    public static void update() {
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        goldAmount.setText(civilization.getGold() + "   ");
        happinessAmount.setText(civilization.getHappiness() + "   ");
        scienceAmount.setText(civilization.collectScience() + "   ");
        Technology tech = civilization.getGettingResearchedTechnology();
        if (tech == null)
            technologyName.setText("nothing");
        else
            technologyName.setText(tech.getName());
    }
}
