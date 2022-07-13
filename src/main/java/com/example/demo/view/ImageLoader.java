package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.TileType;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import static com.example.demo.model.Units.UnitType.SETTLER;

public class ImageLoader {
    private static final HashMap<String, Image> images = new HashMap<>();

    static {
        try {
            for (TileType tile : TileType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/tiles/" + tile + ".png"), 400, 0, true, true, true);
                images.put(tile.toString(), image);
            }
            for (FeatureType feature : FeatureType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/features/" + feature + ".png"), 400, 0, true, true, true);
                images.put(feature.toString(), image);
            }
            for (ResourcesTypes resource : ResourcesTypes.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/resources/" + resource + ".png"), 100, 0, true, true, true);
                images.put(resource.toString(), image);
            }
            for (UnitType unit : UnitType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/units/" + unit + ".png"), 120, 0, true, true, true);
                images.put(unit.toString(), image);
            }
            for (ImprovementType improvementType : ImprovementType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/improvements/" + improvementType + ".png"), 120, 0, true, true, true);
                images.put(improvementType.toString(), image);
            }
            Image image = new Image(HelloApplication.getResource("/com/example/demo/assets/gold.png"), 25, 0, true, true, true);
            images.put("gold", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/happiness.png"), 25, 0, true, true, true);
            images.put("happiness", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/science.png"), 25, 0, true, true, true);
            images.put("science", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/assets/technology.png"), 25, 0, true, true, true);
            images.put("technology", image);

            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/CLOUD.png"), 400, 0, true, true, true);
            images.put("CLOUD", image);
            image = new Image(HelloApplication.getResource("/com/example/demo/tiles/riverDown.png"), 120, 0, true, true, true);
            images.put("riverDown",image);
            image = new Image(HelloApplication.getResource("/com/example/demo/buildings/city.png"), 120, 0, true, true, true);
            images.put("city",image);

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
}
