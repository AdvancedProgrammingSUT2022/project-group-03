package com.example.demo.controller;

import com.example.demo.HelloApplication;
import javafx.scene.media.AudioClip;

import java.util.HashMap;

public class Music {
    private static final HashMap<String, AudioClip> audioClips = new HashMap<>();
    private static boolean enabled = true;

    static {
        addMusic("menu", "/com/example/demo/music/menu.mp3", true);
        addMusic("game", "/com/example/demo/music/game.mp3", true);
    }

    public static void addMusic(String name, String path, boolean repeat) {
        try {
            String x = HelloApplication.getResource(path);
            AudioClip audioClip = new AudioClip(x);
            if (repeat)
                audioClip.setCycleCount(AudioClip.INDEFINITE);
            audioClips.put(name, audioClip);
        } catch (Exception e) {
            System.out.println("cannot load music / " + e.getMessage());
        }
    }

    public static void play(String name) {
        if (enabled) {
            stopAll();
            audioClips.get(name).play();
        }
    }

    public static void stop(String name) {
        audioClips.get(name).stop();
    }

    private static void stopAll() {
        for (AudioClip audioClip : audioClips.values())
            audioClip.stop();
    }

    public void setVolume(String name, double volume) {
        audioClips.get(name).stop();
        audioClips.get(name).play(volume);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        Music.enabled = enabled;
        if (!enabled)
            stopAll();
    }
}
