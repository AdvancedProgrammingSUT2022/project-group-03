package com.example.demo.view;

import com.example.demo.model.Savings;
import com.example.demo.network.MySocketHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SavingHandler {
    private static final String manualPath = "dataBase/manualSave/";
    private static final String autoPath = "dataBase/autoSave/";

    public static int numberOfAutoSaving = 5;
    public static boolean autoSaveIsEnabled = false;
    public static boolean autoSaveAtRenderingMap = true;


    public static void save( MySocketHandler socketHandler) {
        try {
            System.gc();
            socketHandler.getObjectOutputStream().writeObject(new Savings(socketHandler.getGame().getGameController()));
            socketHandler.getObjectOutputStream().flush();
            System.gc();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving game : ");
            e.printStackTrace();
        }
    }

    public static void load(String name, boolean isManual) {
        String path;
        if (isManual)
            path = manualPath + name;
        else
            path = autoPath + name;

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectStream = new ObjectInputStream(fileInputStream);
            Savings savings = (Savings) objectStream.readObject();
            fileInputStream.close();
            objectStream.close();
            //savings.loadThisToGameController();
            System.out.println(savings.getMap().getX());
        } catch (Exception e) {
            System.out.println("Can not load game :(");
            e.printStackTrace();
        }
    }

    private static int getNumberOfSaves() {
        File directory = new File(autoPath);
        return getSaveFiles(directory).size();
    }

    private static File getNewestSave() {
        File chosenFile = null;
        long lastModifiedTime = Long.MIN_VALUE;
        File directory = new File(autoPath);
        for (File file : getSaveFiles(directory)) {
            if (file.lastModified() > lastModifiedTime) {
                chosenFile = file;
                lastModifiedTime = file.lastModified();
            }
        }
        return chosenFile;
    }


    private static String getOldestSave(String directoryFilePath) {
        File chosenFile = null;
        long time = Long.MAX_VALUE;
        File directory = new File(directoryFilePath);
        for (File file : getSaveFiles(directory)) {
            if (file.lastModified() < time) {
                chosenFile = file;
                time = file.lastModified();
            }
        }
        return (chosenFile == null) ? null : chosenFile.getPath();
    }

    public static List<String> getManualSaves() {
        List<String> result = new ArrayList<>();
        File directory = new File(manualPath);

        for (File file : getSaveFiles(directory))
            result.add(file.getName());

        Collections.reverse(result);
        return result;
    }

    public static List<String> getAutoSaves() {
        List<String> result = new ArrayList<>();
        File directory = new File(autoPath);

        for (File file : getSaveFiles(directory))
            result.add(file.getName());

        Collections.reverse(result);
        return result;
    }

    private static List<File> getSaveFiles(File directory) {
        ArrayList<File> fileArrayList = new ArrayList<>(List.of(Objects.requireNonNull(directory.listFiles(File::isFile))));
        fileArrayList.removeIf(file -> file.getName().equals(".gitkeep"));
       // String username = LoginController.getLoggedUser().getUsername();
        fileArrayList.removeIf(file -> {
            int index = file.getName().indexOf("#");
            if (index == -1)
                return false;
            return true;
        });
        return fileArrayList;
    }

    public static void deleteAllManuals() {
        File directory = new File(manualPath);
        List<File> files = getSaveFiles(directory);
        deleteFiles(files);
    }

    public static void deleteAllAutos() {
        File directory = new File(autoPath);
        List<File> files = getSaveFiles(directory);
        deleteFiles(files);
    }

    private static void deleteFiles(List<File> files) {
        try {
            for (File file : files)
                Files.delete(Paths.get(file.getPath()));
        } catch (Exception e) {
            System.out.println("Error: Can not delete file.");
        }
    }
}