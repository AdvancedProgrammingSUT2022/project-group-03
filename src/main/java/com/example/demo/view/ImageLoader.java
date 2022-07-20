package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.TileType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.util.HashMap;

public class ImageLoader {
    private static final HashMap<String, Image> images = new HashMap<>();
    private static final HashMap<String, Font> fonts = new HashMap<>();

    static {
        try {
            for (TileType tile : TileType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/tiles/" + tile + ".png"), 300, 0, true, true, true);
                images.put(tile.toString(), image);
            }
            for (FeatureType feature : FeatureType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/features/" + feature + ".png"), 300, 0, true, true, true);
                images.put(feature.toString(), image);
            }
            for (ResourcesTypes resource : ResourcesTypes.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/resources/" + resource + ".png"), 30, 0, true, true, true);
                images.put(resource.toString(), image);
            }
            for (UnitType unit : UnitType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/units/" + unit + ".png"), 100, 0, true, true, true);
                images.put(unit.toString(), image);
            }
            for (ImprovementType improvementType : ImprovementType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/improvements/" + improvementType + ".png"), 120, 0, true, true, true);
                images.put(improvementType.toString(), image);
            }
            for (TechnologyType technologyType : TechnologyType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/technology/technology/" + technologyType + ".png"), 120, 0, true, true, true);
                images.put(technologyType.toString(), image);
            }
            for (BuildingType buildingType : BuildingType.VALUES) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/buildings/" + buildingType + ".png"), 120, 0, true, true, true);
                images.put(buildingType.toString(), image);
            }
            Image image = new Image(HelloApplication.getResource("/com/example/demo/assets/gold.png"), 25, 0, true, true, true);
            images.put("gold", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/happiness.png"), 25, 0, true, true, true);
            images.put("happiness", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/science.png"), 25, 0, true, true, true);
            images.put("science", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/technology.png"), 25, 0, true, true, true);
            images.put("technology", image);

            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/CLOUD.png"), 200, 0, true, true, true);
            images.put("CLOUD", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/riverDown.png"), 120, 0, true, true, true);
            images.put("riverDown", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/buildings/city.png"), 120, 0, true, true, true);
            images.put("city", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/fog.png"), 120, 0, true, true, true);
            images.put("fog", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/citizen.png"), 120, 0, true, true, true);
            images.put("citizen", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/noCitizen.png"), 120, 0, true, true, true);
            images.put("noCitizen", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/iconoff.png"), 30, 0, true, true, true);
            images.put("techIconOff", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/iconon.png"), 30, 0, true, true, true);
            images.put("techIconOn", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/locked.png"), 500, 0, true, true, true);
            images.put("techLocked", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/treeLines.png"), 7000, 0, true, true, true);
            images.put("techBackground", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/unlocked.png"), 500, 0, true, true, true);
            images.put("techUnlocked", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/back.png"), 120, 0, true, true, true);
            images.put("back", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/search.png"), 120, 0, true, true, true);
            images.put("search", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/chooseTechIconOff.png"), 30, 0, true, true, true);
            images.put("chooseTechIconOff", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/chooseTechIconOn.png"), 30, 0, true, true, true);
            images.put("chooseTechIconOn", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/treeNoLine.jpg"), 7000, 0, true, true, true);
            images.put("treeNoLine", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/unitsPanelIconOn.png"), 7000, 0, true, true, true);
            images.put("unitsPanelIconOn", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/unitsPanelIconOn.png"), 30, 0, true, true, true);
            images.put("unitsPanelIconOn", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/technology/unitsPanelIconOff.png"), 30, 0, true, true, true);
            images.put("unitsPanelIconOff", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/ruins.png"), 100, 0, true, true, true);
            images.put("ruins", image);
//            fonts.put("impactFont",new Font("/com/example/demo/font/impact.ttf",30));

        } catch (RuntimeException e) {
            System.out.println("There is a problem in loading images in ImageLoader.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Image get(String name) {
        if (!images.containsKey(name))
            System.out.println("There is no image named " + name + " in the ImageLoader.");
        return images.get(name);
    }
//    public static Font getFont(String name)
//    {
//        if(!fonts.containsKey(name))
//            System.out.println("There is no image font " + name + " in the ImageLoader.");
//        return fonts.get(name);
//    }
}
