package com.example.demo.view.model;

import com.example.demo.controller.TechnologyAndProductionController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.view.ImageLoader;
import com.example.demo.view.StageController;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class GraphicTechnology {
    private final TechnologyType technologyType;
    private ImageView format;
    private ImageView technologyPhoto;
    private final HBox unlocks = new HBox();
    private final Text text = new Text();
    private Text remainingCyclesText = new Text();
    private boolean isDone = false;
    private ChooseTechnologyMenu chooseTechnologyMenu;

    public GraphicTechnology(TechnologyType technologyType, Pane pane, Technology technology, double xx, double yy, ChooseTechnologyMenu chooseTechnologyMenu) {
        this.chooseTechnologyMenu = chooseTechnologyMenu;
        unlocks.setSpacing(6);
        this.technologyType = technologyType;
        loadImages(technologyType, pane, technology);
        setPosition(technologyType, technology, xx, yy);
    }

    private void loadImages(TechnologyType technologyType, Pane pane, Technology technology) {
        text.setText(technologyType.toString());
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).doesContainTechnology(technologyType) == 1) {
            format = new ImageView(ImageLoader.get("techUnlocked"));
            isDone = true;
        } else
            format = new ImageView(ImageLoader.get("techLocked"));
        technologyPhoto = new ImageView(ImageLoader.get(technologyType.toString()));
        for (ResourcesTypes value : ResourcesTypes.VALUES) {
            if (value.technologyTypes == technologyType) {
                addToUnlocks(value.toString());
            }
        }
        for (ImprovementType value : ImprovementType.values()) {
            if (value.prerequisitesTechnologies == technologyType) {
                addToUnlocks(value.toString());
            }
        }
        for (UnitType value : UnitType.values()) {
            if (value.technologyRequired == technologyType) {
                addToUnlocks(value.toString());
            }
        }
        pane.getChildren().addAll(format, technologyPhoto, unlocks, text);
        if (technology != null && !isDone) {
            remainingCyclesText.setText(TechnologyAndProductionController.cyclesToComplete(technology) + " cycles");
            pane.getChildren().add(remainingCyclesText);
        }
    }

    private void addToUnlocks(String string) {
        ImageView imageView = new ImageView(ImageLoader.get(string));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
//        imageView.setTo;
        Label label = new Label();
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(imageView);
        label.setTooltip(new Tooltip(string));
        unlocks.getChildren().add(label);
    }

    private void clicked(Technology technology) {
        TechnologyAndProductionController.addTechnologyToProduction(technology);
        chooseTechnologyMenu.renderTechnologies(true);
    }

    private void setPosition(TechnologyType technologyType, Technology technology, double xx, double yy) {

        Pair<Double, Double> pair = getXY(technologyType);
        double x, y;
        if (technology == null) {
            x = pair.getKey();
            y = pair.getValue();
        } else {
            x = xx;
            y = yy;
            remainingCyclesText.setX(x + getFormatFitWidth() * 0.8);
            remainingCyclesText.setY(y + 16);
            if (chooseTechnologyMenu != null && !isDone) {
                format.setOnMouseClicked(event -> clicked(technology));
                format.setCursor(Cursor.HAND);
                technologyPhoto.setOnMouseClicked(event -> clicked(technology));
                technologyPhoto.setCursor(Cursor.HAND);
                unlocks.setOnMouseClicked(event -> clicked(technology));
                unlocks.setCursor(Cursor.HAND);
                text.setOnMouseClicked(event -> clicked(technology));
                text.setCursor(Cursor.HAND);
                remainingCyclesText.setOnMouseClicked(event -> clicked(technology));
                remainingCyclesText.setCursor(Cursor.HAND);

            }
        }

        text.setX(x + 70);
        text.setY(y + 15);

        technologyPhoto.setFitWidth(41);
        technologyPhoto.setFitHeight(41);
        technologyPhoto.setX(x + 14);
        technologyPhoto.setY(y + 11);

        unlocks.setLayoutX(x + 70);
        unlocks.setLayoutY(y + 25);


        format.setFitWidth(getFormatFitWidth());
        format.setFitHeight(getFormatFitHeight());
        format.setX(x);
        format.setY(y);
    }

    public static Pair<Double, Double> getXY(TechnologyType technologyType) {
        Double thisX = null, thisY = null;
        switch (technologyType) {
            case AGRICULTURE -> {
                thisX = 61.0;
                thisY = 46.0;
            }
            case POTTERY -> {
                thisX = 492.0;
                thisY = 46.0;
            }
            case ANIMAL_HUSBANDRY -> {
                thisX = 492.0;
                thisY = 163.0;
            }
            case MINING -> {
                thisX = 492.0;
                thisY = 459.0;
            }
            case ARCHERY -> {
                thisX = 492.0;
                thisY = 585.0;
            }
            case CALENDAR -> {
                thisX = 894.0;
                thisY = 46.0;
            }
            case WRITING -> {
                thisX = 894.0;
                thisY = 163.0;
            }
            case TRAPPING -> {
                thisX = 894.0;
                thisY = 289.0;
            }
            case THE_WHEEL -> {
                thisX = 894.0;
                thisY = 415.0;
            }
            case MASONRY -> {
                thisX = 894.0;
                thisY = 529.0;
            }
            case BRONZE_WORKING -> {
                thisX = 894.0;
                thisY = 648.0;
            }
            case PHILOSOPHY -> {
                thisX = 1317.0;
                thisY = 164.0;
            }
            case HORSEBACK_RIDING -> {
                thisX = 1317.0;
                thisY = 320.0;
            }
            case MATHEMATICS -> {
                thisX = 1317.0;
                thisY = 415.0;
            }
            case CONSTRUCTION -> {
                thisX = 1317.0;
                thisY = 529.0;
            }
            case IRON_WORKING -> {
                thisX = 1317.0;
                thisY = 648.0;
            }
            case THEOLOGY -> {
                thisX = 1759.0;
                thisY = 37.0;
            }
            case CIVIL_SERVICE -> {
                thisX = 1759.0;
                thisY = 243.0;
            }
            case CURRENCY -> {
                thisX = 1759.0;
                thisY = 415.0;
            }
            case ENGINEERING -> {
                thisX = 1759.0;
                thisY = 529.0;
            }
            case METAL_CASTING -> {
                thisX = 1759.0;
                thisY = 648.0;
            }
            case EDUCATION -> {
                thisX = 2163.0;
                thisY = 37.0;
            }
            case CHIVALRY -> {
                thisX = 2163.0;
                thisY = 243.0;
            }
            case MACHINERY -> {
                thisX = 2163.0;
                thisY = 529.0;
            }
            case PHYSICS -> {
                thisX = 2163.0;
                thisY = 648.0;
            }
            case STEEL -> {
                thisX = 2163.0;
                thisY = 772.0;
            }
            case BANKING -> {
                thisX = 2618.0;
                thisY = 37.0;
            }
            case ACOUSTICS -> {
                thisX = 2618.0;
                thisY = 164.0;
            }
            case PRINTING_PRESS -> {
                thisX = 2618.0;
                thisY = 529.0;
            }
            case GUN_POWDER -> {
                thisX = 2618.0;
                thisY = 772.0;
            }
            case ECONOMICS -> {
                thisX = 3053.0;
                thisY = 37.0;
            }
            case SCIENTIFIC_THEORY -> {
                thisX = 3053.0;
                thisY = 164.0;
            }
            case ARCHAEOLOGY -> {
                thisX = 3053.0;
                thisY = 281.0;
            }
            case METALLURGY -> {
                thisX = 3053.0;
                thisY = 669.0;
            }
            case CHEMISTRY -> {
                thisX = 3053.0;
                thisY = 772.0;
            }
            case BIOLOGY -> {
                thisX = 3496.0;
                thisY = 281.0;
            }
            case RIFLING -> {
                thisX = 3496.0;
                thisY = 669.0;
            }
            case MILITARY_SCIENCE -> {
                thisX = 3496.0;
                thisY = 772.0;
            }
            case FERTILIZER -> {
                thisX = 3496.0;
                thisY = 883.0;
            }
            case STEAM_POWER -> {
                thisX = 3950.0;
                thisY = 164.0;
            }
            case DYNAMITE -> {
                thisX = 3950.0;
                thisY = 669.0;
            }
            case RAILROAD -> {
                thisX = 4393.0;
                thisY = 46.0;
            }
            case ELECTRICITY -> {
                thisX = 4393.0;
                thisY = 164.0;
            }
            case REPLACEABLE_PARTS -> {
                thisX = 4393.0;
                thisY = 335.0;
            }
            case TELEGRAPH -> {
                thisX = 4823.0;
                thisY = 164.0;
            }
            case RADIO -> {
                thisX = 4823.0;
                thisY = 274.0;
            }
            case COMBUSTION -> {
                thisX = 5266.0;
                thisY = 598.0;
            }

        }
        thisX = StageController.getStage().getHeight() / 1080 * thisX;
        thisY = StageController.getStage().getHeight() / 1080 * thisY;
        return new Pair<>(thisX, thisY);
    }

    public static double getFormatFitWidth() {
        return 367;
    }

    public static double getFormatFitHeight() {
        return 80;
    }
}
