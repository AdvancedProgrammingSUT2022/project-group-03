package com.example.demo.view;

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

    public MapMoveController(Pane root, Pane upperMapPane,double minX, double maxX, double minY, double maxY, boolean changeY,boolean doZoom) {
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
            if(x>minX && x<maxX)
            upperMapPane.setTranslateX(x);
//            if (y < 500 && y > -mapPane.getHeight() + 500)
            if(y>minY && y<maxY && changeY)
            upperMapPane.setTranslateY(y);
        });

        if(doZoom)
        {
            //zoom in/out on map
            upperMapPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight());
            upperMapPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
            upperMapPane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
                double delta = 1.15;
                double scale = upperMapPane.getScaleY();
                scale *= (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
                if ((scale > 2.5) || (scale < 0.3))
                    return;
                upperMapPane.setScaleX(scale);
                upperMapPane.setScaleY(scale);
                //move the visible area according to the zoom state:
                double translateScale = (scrollEvent.getDeltaY() > 0) ? (delta) : (1 / delta);
                upperMapPane.setTranslateX(upperMapPane.getTranslateX() * translateScale);
                upperMapPane.setTranslateY(upperMapPane.getTranslateY() * translateScale);
            });
        }
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
}
