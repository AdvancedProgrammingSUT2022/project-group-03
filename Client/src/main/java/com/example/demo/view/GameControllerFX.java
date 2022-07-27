package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.Music;
import com.example.demo.controller.NetworkController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.cheat.Cheat;
import com.example.demo.view.model.GraphicTile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.ArrayList;

public class GameControllerFX {
    public boolean myTurn = false;
    public HBox infoBar;
    public Button infoButton;
    public Button researchesButton;
    public Button economicsButton;
    public Button demographicsButton;
    public Button militaryButton;
    public Button notificationsButton;
    public ScrollPane infoTab;
    public Text infoText;
    public int infoTabNumber = -1;
    public Button cityButton;
    public ScrollPane cityPage;
    public Text cityText;
    public VBox rightPanelVBox;
    public VBox menuPanel;
    private MapMoveController mapMoveController;
    private boolean selectingTile;
    public final Text publicText = new Text("");
    public final ImageView cross = new ImageView(ImageLoader.get("cross"));
    public Button nextButton;
    @FXML
    private HBox cheatBar;
    @FXML
    private VBox leftPanel;
    @FXML
    private HBox statusBar;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Pane upperMapPane;
    private double startX;
    private double startY;
    private Timeline update;
    private CityPanel cityPanel;
    private AnchorPane unitsPanelPane;
    private static boolean hasStarted = false;

    public void initialize() {
        cityPanel = new CityPanel(this, leftPanel);
        if (!hasStarted)
            Music.play("game");
//        if (!hasStarted)
//            startAFakeGame();
        new Cheat(root, cheatBar, this);
//        GameController.getMap().getX()*
        StatusBarController.init(statusBar);
        addInfoButtons();
        renderMap();
        upperMapPane.setTranslateX(300);
        upperMapPane.setTranslateY(300);
        mapMoveController = new MapMoveController(root, upperMapPane, -2222222, 222222, -222222, 222222, true, true);
        hasStarted = true;

        update = new Timeline(
                new KeyFrame(Duration.millis(5000), event -> {
                    String string =NetworkController.send("update");
                    if(string.startsWith("end")){
                        SavingHandler.load();
                        renderMap();
                        String winner =  NetworkController.getResponse(true);
                        for (Civilization civilization : GameController.getCivilizations()) {
                            if(civilization.getUser().getUsername().equals(winner)){
                                GameController.setWinnerSend(civilization);
                            }
                        }
                        update.stop();
                        StageController.sceneChanger("gameEnd");


                    }else if(string.startsWith("your turn")){
                        if(!myTurn) {
                            StageController.errorMaker("turn", "your turn", Alert.AlertType.INFORMATION);
                            myTurn = true;
                        }else {
                            myTurn = false;
                        }
                    }
                    SavingHandler.load();
                    renderMap();


                }
                )
        );

        root.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode().getName().equals("S"))
                SavingHandler.save(true);
        });

        findUnit();
    }

    void eachInfoButtonsClicked(int number) {
        if (infoTabNumber == number) {
            infoTab.setOpacity(0);
            if (CityPanel.getCityPanelPane() != null)
                CityPanel.getCityPanelPane().setOpacity(0);
            infoTabNumber = -1;
            cityPanel.disableCityPanel();
        } else {
            infoText.setDisable(false);
            infoTab.setContent(infoText);
            switch (number) {
                case 0 -> infoText.setText(InfoController.infoResearches());
                case 2 -> cityButtonClicked();
                case 3 -> infoText.setText(InfoController.infoEconomic());
                case 4 -> infoText.setText(InfoController.infoDemographic());
                case 5 -> infoText.setText(InfoController.printMilitaryOverview());
                case 6 -> infoText.setText(InfoController.infoNotifications(10));
                case 7 -> infoText.setText(InfoController.cityBanner(cityPanel.getOpenedPanelCity()));
            }
            infoTab.setOpacity(1);
            infoTabNumber = number;
        }
    }

    private void cityButtonClicked() {
        infoText.setDisable(true);
        AnchorPane anchorPane = new AnchorPane();
        Text[] texts = new Text[GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size()];
        Button[] buttons = new Button[texts.length];
        for (int i = 0; i < GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size(); i++) {
            String string = InfoController.printCity(i);
            texts[i] = new Text(string);
            texts[i].setLayoutY((i + 1) * 45);
            anchorPane.getChildren().add(texts[i]);
            buttons[i] = new Button("open");
            buttons[i].setLayoutX(texts[i].getLayoutBounds().getMinX() + texts[i].getLayoutBounds().getWidth() + 10);
            buttons[i].setLayoutY((i + 0.5) * 45);
            int finalI = i;
            buttons[i].setOnMouseClicked(event -> getCityPanel().cityClicked(GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().get(finalI)));
            anchorPane.getChildren().add(buttons[i]);
        }

        infoTab.setContent(anchorPane);
    }


    private void infoButtonClicked(MouseEvent mouseEvent) {
        if (infoBar.getOpacity() == 1) {
            infoBar.setOpacity(0);
            infoTab.setOpacity(0);
            researchesButton.setDisable(true);
            economicsButton.setDisable(true);
            demographicsButton.setDisable(true);
            militaryButton.setDisable(true);
            notificationsButton.setDisable(true);
        } else {
            infoBar.setOpacity(1);
            researchesButton.setDisable(false);
            economicsButton.setDisable(false);
            demographicsButton.setDisable(false);
            militaryButton.setDisable(false);
            notificationsButton.setDisable(false);
        }
    }

    private void enterTechTree() throws IOException {
        StageController.sceneChanger("technologyTree.fxml");
    }


    private void enterSelectResearchPanel() throws IOException {
        StageController.sceneChanger("chooseTechnologyMenu.fxml");
    }

    private void panelLabelsInit(Label label, String string) {
        ImageView imageView = new ImageView(ImageLoader.get(string + "Off"));
        label.setGraphic(imageView);
        label.setOnMouseEntered(event -> {
            imageView.setImage(ImageLoader.get(string + "On"));
            label.setGraphic(imageView);
        });
        label.setOnMouseExited(event -> {
            imageView.setImage(ImageLoader.get(string + "Off"));
            label.setGraphic(imageView);
        });
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(imageView);
        infoBar.getChildren().add(label);
    }

    private void addInfoButtons() {
        Label technologyTreeLabel = new Label();
        panelLabelsInit(technologyTreeLabel, "techIcon");
        technologyTreeLabel.setOnMouseClicked(event -> {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0) {
                StageController.errorMaker("You cannot enter the technology tree yet", "You must have atLeast one city to enter the technology tree", Alert.AlertType.ERROR);
            } else {
                try {
                    enterTechTree();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Label selectResearchLabel = new Label();
        panelLabelsInit(selectResearchLabel, "chooseTechIcon");
        selectResearchLabel.setOnMouseClicked(event -> {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0) {
                StageController.errorMaker("You cannot enter the select research panel yet",
                    "You must have atLeast one city to enter the select research panel", Alert.AlertType.ERROR);
            } else {
                try {
                    enterSelectResearchPanel();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        Label unitsPanelLabel = new Label();
        panelLabelsInit(unitsPanelLabel, "unitsPanelIcon");
        unitsPanelLabel.setOnMouseClicked(event -> StageController.sceneChanger("unitsPanel.fxml"));

        Label diplomacyLabel = new Label();
        panelLabelsInit(diplomacyLabel, "diplomacyIcon");
        diplomacyLabel.setOnMouseClicked(event -> {
            if (GameController.getCurrentCivilization().getKnownCivilizations().size() == 0)
                StageController.errorMaker("You cannot enter the Diplomacy panel yet",
                    "You must know atLeast one other civilization to enter the Diplomacy panel", Alert.AlertType.ERROR);
            else
                StageController.sceneChanger("diplomacy.fxml");
        });
        diplomacyLabel.setTooltip(new Tooltip("Diplomacy Panel"));


        technologyTreeLabel.setTooltip(new Tooltip("Technology Tree"));
        selectResearchLabel.setTooltip(new Tooltip("Select Research"));
        unitsPanelLabel.setTooltip(new Tooltip("units Panel"));
        infoButton.setOnMouseClicked(this::infoButtonClicked);
        researchesButton.setOnMouseClicked(event -> eachInfoButtonsClicked(0));
        cityButton.setOnMouseClicked(event -> eachInfoButtonsClicked(2));
        economicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(3));
        demographicsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(4));
        militaryButton.setOnMouseClicked(event -> eachInfoButtonsClicked(5));
        notificationsButton.setOnMouseClicked(event -> eachInfoButtonsClicked(6));
    }

    private static GraphicTile[][] graphicMap;

    public boolean getSelectingTile() {
        return selectingTile;
    }

    public void setSelectingTile(boolean mode) {
        if (!mode)
            mapPane.getChildren().remove(cross);
        selectingTile = mode;
    }



    public void renderMap() {
        mapPane.getChildren().clear();
        Map map = GameController.getMap();
        graphicMap = new GraphicTile[map.getStaticX()][map.getStaticY()];
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getStaticY(); j++)
            for (int i = 0; i < map.getStaticX(); i++) {
                graphicMap[i][j] = new GraphicTile(tiles[i][j], mapPane, leftPanel, this);
            }
        cityPage.setViewOrder(-2);
        mapPane.getChildren().add(cityPage);
        cityPage.setOnKeyPressed(keyEvent -> {
            if ((keyEvent.getCode().getName().equals("q") || keyEvent.getCode().getName().equals("Q")) && cityPage.getLayoutX() != -2000 && cityPage.getLayoutY() != -2000) {
                cityPage.setLayoutX(-2000);
                cityPage.setLayoutY(-2000);
            }
        });
        StatusBarController.update();
        //save the game:
        if (SavingHandler.autoSaveIsEnabled && SavingHandler.autoSaveAtRenderingMap)
            SavingHandler.save(false);
    }

    /*
     * This methode is only for testing
     */
    private void startAFakeGame() {
        //start a fake game
        User user = new User("Sayyed", "ali", "Tayyeb", false);
        User user2 = new User("Sayyed2", "ali", "Tayyeb", false);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        LoginController.loginUser("u", "pap");
        Map.setStaticX(60);
        Map.setStaticY(90);
        GameController.startGame(users);
    }


    public static GraphicTile[][] getGraphicMap() {
        return graphicMap;
    }


    public void nextTurn() {
        if (!Boolean.parseBoolean(NetworkController.send("nextTurn"))) {
            switch (GameController.getUnfinishedTasks().get(0).getTaskTypes()) {
                case UNIT -> StageController.errorMaker("Unit Error", "A unit needs order.", Alert.AlertType.ERROR);
                case CITY_PRODUCTION -> StageController.errorMaker("City Error", "Set your city to produce something.", Alert.AlertType.ERROR);
                case TECHNOLOGY_PROJECT -> StageController.errorMaker("Technology not selected", "Please select a technology to researching about it.", Alert.AlertType.ERROR);
                case CITY_DESTINY -> StageController.errorMaker("City destiny error", "Please decide whether to destroy the captured city or not.", Alert.AlertType.ERROR);
            }
        } else {
            GameController.setSelectedCity(null);
            GameController.setSelectedUnit(null);
            GameController.setSelectedTile(null);
            setSelectingTile(false);
            leftPanel.getChildren().clear();
            renderMap();
            findUnit();
            notify("Next turn", "Successfully passed this turn.");
            //save the game:
            if (SavingHandler.autoSaveIsEnabled && !SavingHandler.autoSaveAtRenderingMap)
                SavingHandler.save(false);
        }
    }

    public void findUnit() {
        if (GameController.getUnfinishedTasks().isEmpty()) {
            if (GameController.getCurrentCivilization().getCities().size() == 0)
                return;
            Tile tile = GameController.getCurrentCivilization().getCities().get(0).getMainTile();
            MapMoveController.showTile(graphicMap[tile.getX()][tile.getY()]);
            notify("All is done", "Now you should be able to click on the next turn without any problems");
            return;
        }
        Tile tile = GameController.getUnfinishedTasks().get(0).getTile();
        if (tile != null)
            MapMoveController.showTile(graphicMap[tile.getX()][tile.getY()]);
    }

    public static void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.setTitle(title);
        alert.initOwner(StageController.getStage());
        alert.showAndWait();
    }


    public ScrollPane getCityPage() {
        return cityPage;
    }

    public CityPanel getCityPanel() {
        return cityPanel;
    }

    public AnchorPane getMapPane() {
        return mapPane;
    }

    public VBox getRightPanelVBox() {
        return rightPanelVBox;
    }

    public ScrollPane getInfoTab() {
        return infoTab;
    }

    public void menu() {
        if (!menuPanel.getChildren().isEmpty()) {
            menuPanel.getChildren().clear();
            return;
        }
        Button autoSave;
        if (SavingHandler.autoSaveIsEnabled)
            autoSave = new Button("Disable auto save");
        else
            autoSave = new Button("enable auto save");
        Button pause = new Button("Pause game");
        Button music;
        if (Music.isEnabled())
            music = new Button("Mute music");
        else
            music = new Button("Play music");
        autoSave.setOnAction(actionEvent1 -> {
            if (SavingHandler.autoSaveIsEnabled) {
                SavingHandler.autoSaveIsEnabled = false;
                autoSave.setText("Enable auto save");
            } else {
                SavingHandler.autoSaveIsEnabled = true;
                SavingHandler.autoSaveAtRenderingMap = true;
                autoSave.setText("Disable auto save");
            }
        });

        music.setOnAction(actionEvent -> {
            if (Music.isEnabled()) {
                Music.setEnabled(false);
                music.setText("Play music");
            } else {
                Music.setEnabled(true);
                Music.play("game");
                music.setText("Mute music");
            }
        });

        pause.setOnAction(actionEvent -> {
            StageController.sceneChanger("mainMenu.fxml");
            Music.play("menu");
        });

        menuPanel.setStyle("-fx-prefWidth: 300;");
        menuPanel.getChildren().addAll(autoSave, music, pause);
    }

    public MapMoveController getMapMoveController() {
        return mapMoveController;
    }

    static GraphicTile tileToGraphicTile(Tile tile) {
        for (GraphicTile[] graphicTiles : graphicMap) {
            for (int j = 0; j < graphicMap[0].length; j++) {
                if (graphicTiles[j].getTile().getX() == tile.getX() &&
                    graphicTiles[j].getTile().getY() == tile.getY())
                    return graphicTiles[j];
            }
        }
        return null;
    }

    private void notify(String title, String message) {
        Notifications notifications = Notifications.create().hideAfter(Duration.seconds(5)).text(message).title(title);
        notifications.show();
    }
}
