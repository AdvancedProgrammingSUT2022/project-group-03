package com.example.demo.view;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HealthBar {
    private final ImageView format;
    private final ImageView greenBar;
    private final ImageView blueBar;
    private final HealthyBeing healthyBeing;
    private final Text text;

    public HealthBar(HealthyBeing healthyBeing, Pane pane) {
        format = new ImageView();
        format.setImage(ImageLoader.get("healthBarFormat"));
        greenBar = new ImageView();
        greenBar.setImage(ImageLoader.get("healthBarGreen"));
        blueBar = new ImageView();
        blueBar.setImage(ImageLoader.get("healthBarBlue"));
        this.healthyBeing = healthyBeing;
        text = new Text();
        pane.getChildren().addAll(format,greenBar,blueBar,text);
        fixSizeFormat();

        interpolate();
    }
    protected void interpolate() {
        fixSizeFormat();
        greenBar.setFitWidth(getColorBarMaxWidth() *
                healthyBeing.greenBarPercent());
        greenBar.setFitHeight(format.getFitHeight());

        blueBar.setFitWidth(getColorBarMaxWidth() *
                healthyBeing.blueBarPercent());
        blueBar.setFitHeight(format.getFitHeight());

        greenBar.setOpacity(1);
        if (healthyBeing.greenBarPercent() == 0)
            greenBar.setOpacity(0);
        blueBar.setOpacity(1);
        if (healthyBeing.blueBarPercent() == 0)
            blueBar.setOpacity(0);
        text.setText(healthyBeing.getHealthDigit());
    }

    private void fixSizeFormat() {
        format.setFitWidth(getWidth());
        format.setFitHeight(getHeight());
    }

    public double getColorBarMaxWidth() {
        return this.format.getFitWidth()
                - this.format.getFitWidth() * 0.017751;
    }

    public void fixFormat(double x, double y)
    {
        format.setX(x);
        format.setY(y);
        greenBar.setX(format.getX() + format.getFitWidth() * 0.017751);
        greenBar.setY(format.getY());
        blueBar.setX(format.getX() + format.getFitWidth() * 0.017751);
        blueBar.setY(format.getY());
        text.setX(format.getX() + format.getFitWidth() / 2 -
                text.getLayoutBounds().getWidth() / 2);
        text.setY(format.getY());
    }

    public static double getWidth()
    {
        return 80;
    }
    public static double getHeight()
    {
        return getWidth() / 7.191489;
    }
}
