package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.TileType;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageLoader {
    private static final HashMap<String, Image> images = new HashMap<>();

    static {
        try {
            for (TileType tile : TileType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/tiles/" + tile + ".png"), 150, 0, true, true, true);
                images.put(tile.toString(), image);
            }
            for (FeatureType feature : FeatureType.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/features/" + feature + ".png"), 150, 0, true, true, true);
                images.put(feature.toString(), image);
            }
            for (ResourcesTypes resource : ResourcesTypes.values()) {
                Image image = new Image(HelloApplication.getResource("/com/example/demo/resources/" + resource + ".png"), 30, 0, true, true, true);
                images.put(resource.toString(), image);
            }
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
