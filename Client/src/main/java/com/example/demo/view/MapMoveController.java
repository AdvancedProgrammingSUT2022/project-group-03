package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.model.GraphicTile;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class MapMoveController {
    private double startX;
    private double startY;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
    private double maxScale;
    private static Pane upperMapPane;

    public MapMoveController(Pane root, Pane upperMapPane, double minX, double maxX, double minY, double maxY, boolean changeY, boolean doZoom) {

        MapMoveController.upperMapPane = upperMapPane;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        //move on map
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            startX = upperMapPane.getTranslateX() - mouseEvent.getScreenX();
            startY = upperMapPane.getTranslateY() - mouseEvent.getScreenY();
        });
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            double x = mouseEvent.getScreenX() + startX;
            double y = mouseEvent.getScreenY() + startY;
//            if (x < 500 && x > -mapPane.getWidth() + 700)
            if(x>getMaxX())
                x = getMaxX();
            if(x<getMinX())
                x = getMinX();
            if(y>getMaxY())
                y = getMaxY();
            if(y<getMinY())
                y = getMinY();
//            if (x > minX && x < getMaxX())
                upperMapPane.setTranslateX(x);
//            if (y < 500 && y > -mapPane.getHeight() + 500)
//            if (y > minY && y < maxY && changeY)
                upperMapPane.setTranslateY(y);
//                if(doZoom)
//                    setScaleMinMax();
            System.out.println("x: " + x + " ,y: " + y);

        });

        if (doZoom) {
            //zoom in/out on map
//            setScaleMinMax();
            upperMapPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight());
            upperMapPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
//            setMaxMin(upperMapPane.getScaleY());
            upperMapPane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
                double delta = 1.15;
                double scale = upperMapPane.getScaleY(),scaleBefore = scale;
                scale *= (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
                if ((scale > 2.5) || (scale < 0.3))
                    return;
//                if(scale<maxScale)
//                    scale= maxScale;
                upperMapPane.setScaleX(scale);
                upperMapPane.setScaleY(scale);
//                System.out.println("scale: " + scale);
//                setMaxMin(scale);
                if(scaleBefore!=scale) {//move the visible area according to the zoom state:
                    double translateScale = (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
                    upperMapPane.setTranslateX(upperMapPane.getTranslateX() * translateScale);
                    upperMapPane.setTranslateY(upperMapPane.getTranslateY() * translateScale);
                }
                System.out.println("scale: " + scale);
            });
        }
    }

    public static void showTile(GraphicTile tile) {
        upperMapPane.setScaleX(1);
        upperMapPane.setScaleY(1);
        upperMapPane.setTranslateX(-tile.getX() + 340);
        upperMapPane.setTranslateY(-tile.getY() + 270);
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setMaxMin(double scale) {
        this.maxX = (scale - 0.3269017738461676)/(2.313060765624999 - 0.3269017738461676)*(465.26987148077205 + 716.8894999522786) - 716.8894999522786;
        this.maxY = (scale - 0.3269017738461676)/(2.313060765624999 - 0.3269017738461676)*(570.5915440083327 + 408.35906282706446) - 408.35906282706446;

//        double ratio = Math.ceil((double) GameController.getMap().getX()/2) + Math.floor((double) GameController.getMap().getX()/2)/2;
//        if( GameController.getMap().getX()%2==0)
//            ratio+=0.25;
//        System.out.println("ratio: " + ratio);
//        minX= maxX-(GraphicTile.getWidth() * (1 + 2 / (Math.sqrt(3) * 15)) * ratio - StageController.getStage().getWidth())*scale;
//        System.out.println(minX);
//        this.minX = -GameControllerFX.getScreenX();
//        System.out.println("scale: " + scale);
    }

//    public void setScaleMinMax()
//    {
//        maxScale = ((maxX) + 716.8894999522786)/(465.26987148077205 + 716.8894999522786)*(2.313060765624999 - 0.3269017738461676) +0.3269017738461676;
//        System.out.println("maxScale is: " + maxScale);
//
//    }


}
