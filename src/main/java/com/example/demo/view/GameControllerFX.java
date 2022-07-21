package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.cheat.Cheat;
import com.example.demo.view.model.GraphicTile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class GameControllerFX {
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
    private ImageView technologyTreeIcon;
    private Label technologyTreeLabel;
    private ImageView selectResearchIcon;
    private Label selectResearchLabel;
    private ImageView unitsPanelIcon;
    private Label unitsPanelLabel;
    private double startX;
    private double startY;
    private final CityPanel cityPanel = new CityPanel(this);
    private AnchorPane unitsPanelPane;
    private static boolean hasStarted = false;


    public void initialize() {
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


        root.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode().getName().equals("S"))
                SavingHandler.save(true);
        });

        System.out.println("Game started...");
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
                case 7 -> {
                    cityPanel.turnEveryButtonOff();
                    infoText.setText(InfoController.cityBanner(cityPanel.getOpenedPanelCity()));
                }
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
            buttons[i].setOnMouseClicked(event -> getCityPanel().cityPanel(GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().get(finalI), false));
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

    private void addInfoButtons() {
        technologyTreeLabel = new Label();

        technologyTreeIcon = new ImageView(ImageLoader.get("techIconOff"));
        technologyTreeLabel.setOnMouseEntered(event -> {
            technologyTreeIcon.setImage(ImageLoader.get("techIconOn"));
            technologyTreeLabel.setGraphic(technologyTreeIcon);
        });
        technologyTreeLabel.setOnMouseExited(event -> {
            technologyTreeIcon.setImage(ImageLoader.get("techIconOff"));
            technologyTreeLabel.setGraphic(technologyTreeIcon);
        });
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
        selectResearchLabel = new Label();
        selectResearchIcon = new ImageView(ImageLoader.get("chooseTechIconOff"));
        selectResearchLabel.setOnMouseEntered(event -> {
            selectResearchIcon.setImage(ImageLoader.get("chooseTechIconOn"));
            selectResearchLabel.setGraphic(selectResearchIcon);
        });
        selectResearchLabel.setOnMouseExited(event -> {
            selectResearchIcon.setImage(ImageLoader.get("chooseTechIconOff"));
            selectResearchLabel.setGraphic(selectResearchIcon);
        });
        selectResearchLabel.setOnMouseClicked(event -> {
            if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0) {
                StageController.errorMaker("You cannot enter the select research panel yet", "You must have atLeast one city to enter the select research panel", Alert.AlertType.ERROR);
            } else {
                try {
                    enterSelectResearchPanel();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        unitsPanelLabel = new Label();
        unitsPanelIcon = new ImageView(ImageLoader.get("unitsPanelIconOff"));
        unitsPanelLabel.setOnMouseEntered(event -> {
            unitsPanelIcon.setImage(ImageLoader.get("unitsPanelIconOn"));
            unitsPanelLabel.setGraphic(unitsPanelIcon);
        });
        unitsPanelLabel.setOnMouseExited(event -> {
            unitsPanelIcon.setImage(ImageLoader.get("unitsPanelIconOff"));
            unitsPanelLabel.setGraphic(unitsPanelIcon);
        });
        unitsPanelLabel.setOnMouseClicked(event -> {
            StageController.sceneChanger("unitsPanel.fxml");
        });


        technologyTreeLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        technologyTreeLabel.setGraphic(technologyTreeIcon);
        technologyTreeLabel.setTooltip(new Tooltip("Technology Tree"));
        infoBar.getChildren().add(technologyTreeLabel);
        selectResearchLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        selectResearchLabel.setGraphic(selectResearchIcon);
        selectResearchLabel.setTooltip(new Tooltip("Select Research"));
        infoBar.getChildren().add(selectResearchLabel);
        unitsPanelLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        unitsPanelLabel.setGraphic(unitsPanelIcon);
        unitsPanelLabel.setTooltip(new Tooltip("units Panel"));
        infoBar.getChildren().add(unitsPanelLabel);
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
        selectingTile = mode;
    }


    public void renderMap() {
        mapPane.getChildren().clear();
        Map map = GameController.getMap();
        graphicMap = new GraphicTile[map.getX()][map.getY()];
        Tile[][] tiles = map.getTiles();
        for (int j = 0; j < map.getY(); j++)
            for (int i = 0; i < map.getX(); i++)
                graphicMap[i][j] = new GraphicTile(tiles[i][j], mapPane, leftPanel, this);
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
        Map.setX(60);
        Map.setY(90);
        GameController.startGame(users);
    }


    public static GraphicTile[][] getGraphicMap() {
        return graphicMap;
    }


    public void nextTurn() {
        GameController.setUnfinishedTasks();
        if (!GameController.nextTurnIfYouCan()) {
            switch (GameController.getUnfinishedTasks().get(0).getTaskTypes()) {
                case UNIT -> StageController.errorMaker("Unit Error", "A unit needs order.", Alert.AlertType.ERROR);
                case CITY_PRODUCTION -> StageController.errorMaker("City Error", "Set your city to produce a unit.", Alert.AlertType.ERROR);
                case TECHNOLOGY_PROJECT -> StageController.errorMaker("Technology not selected", "Please select a technology to researching about it.", Alert.AlertType.ERROR);
                case CITY_DESTINY -> StageController.errorMaker("City destiny error", "Please decide whether to destroy the captured city or not.", Alert.AlertType.ERROR);
            }
        } else {
            renderMap();
            StageController.errorMaker("Next turn", "Successfully passed this turn.", Alert.AlertType.INFORMATION);
            //save the game:
            if (SavingHandler.autoSaveIsEnabled && !SavingHandler.autoSaveAtRenderingMap)
                SavingHandler.save(false);
        }
    }

    public void findUnit(ActionEvent actionEvent) {
        if (GameController.getUnfinishedTasks().isEmpty()) {
            StageController.errorMaker("All is done", "Click next turn.", Alert.AlertType.INFORMATION);
            return;
        }
        Tile tile = GameController.getUnfinishedTasks().get(0).getTile();
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
        Button pause = new Button("Pause");
        autoSave.setOnAction(actionEvent1 -> {
            if (SavingHandler.autoSaveIsEnabled) {
                SavingHandler.autoSaveIsEnabled = false;
                autoSave.setText("Enable auto save");
            } else {
                SavingHandler.autoSaveIsEnabled = true;
                autoSave.setText("Disable auto save");
            }
        });
        pause.setOnAction(actionEvent -> StageController.sceneChanger("mainMenu.fxml"));
        menuPanel.getChildren().addAll(autoSave, pause);
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
}
