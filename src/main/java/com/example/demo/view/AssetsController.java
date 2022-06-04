package com.example.demo.view;

import com.example.demo.HelloApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class AssetsController {
    private static ArrayList<Image> userAvatarImages;
    public static void openLeadersAvatars(){
        userAvatarImages = new ArrayList<>();
        for (UserIcon value : UserIcon.getVALUES()) {
            userAvatarImages.add(new Image(HelloApplication.class.getResource(value.image).toExternalForm()));
        }
    }

    public static ArrayList<Image> getUserAvatarImages() {
        return userAvatarImages;
    }

    public static void closeLeadersAvatars(){
        userAvatarImages = new ArrayList<>();
    }
}
