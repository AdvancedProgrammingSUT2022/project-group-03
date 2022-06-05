package com.example.demo.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameEntryMenuFx implements Initializable {

    public Text numberOfAutoSaveDetail;
    public Text numberOfAutoSaveText;
    public Button lessAutoSavesButton;
    public Button moreAutoSavesButton;
    public ImageView background;
    public ScrollPane savesListScrollBar;
    public Text selectedSaveNumber;
    int mapX = 60, mapY = 90;
    int autoSave = 0;
    int autoSaveNumbers = 5;
    public ToggleButton autoMapToggle;
    public Button moreMapXButton, lessMapYButton, morePlayersButton, lessPlayersButton;
    public Text mapXY, numberOfPlayersDetail, mapDetails, numberOfPlayersTest;
    public TextField invitationId;
    public SplitMenuButton autoSaveOrNot;
    int numberOfPlayers = 1;
    @FXML
    Button startGameButton, sendInvitationButton;

    @FXML
    public void startGame() {

    }

    @FXML
    public void sendInvitation() {

    }

    @FXML
    public void morePlayers() {
        numberOfPlayers++;
        numberOfPlayersTest.setText(String.valueOf(numberOfPlayers));
        numberOfPlayersTest.setX(StageController.getStage().getWidth() * 0.92 - numberOfPlayersTest.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void lessPlayers() {
        if (numberOfPlayers < 2)
            return;
        numberOfPlayers--;
        numberOfPlayersTest.setText(String.valueOf(numberOfPlayers));
        numberOfPlayersTest.setX(StageController.getStage().getWidth() * 0.92 - numberOfPlayersTest.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void moreMapX() {
        mapX += 2;
        mapY += 3;
        mapXY.setText("X: " + mapX + " Y: " + mapY);
        mapXY.setX(StageController.getStage().getWidth() * 0.68 - mapXY.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void lessMapY() {
        if (mapX < 10)
            return;
        mapX -= 2;
        mapY -= 3;
        mapXY.setText("X: " + mapX + " Y: " + mapY);
        mapXY.setX(StageController.getStage().getWidth() * 0.68 - mapXY.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void autoMap() {
        if (autoMapToggle.isSelected()) {
            mapDetails.setOpacity(0.5);
            mapXY.setOpacity(0.5);
            moreMapXButton.setDisable(true);
            lessMapYButton.setDisable(true);
            return;
        }
        mapDetails.setOpacity(1);
        mapXY.setOpacity(1);
        moreMapXButton.setDisable(false);
        lessMapYButton.setDisable(false);

    }

    @FXML
    public void moreAutoSave() {
        autoSaveNumbers++;
        numberOfAutoSaveText.setText(String.valueOf(autoSaveNumbers));
        numberOfAutoSaveText.setX(StageController.getStage().getWidth() * 0.41 - numberOfAutoSaveText.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void lessAutoSave() {
        if (autoSaveNumbers < 2)
            return;
        autoSaveNumbers--;
        numberOfAutoSaveText.setText(String.valueOf(autoSaveNumbers));
        numberOfAutoSaveText.setX(StageController.getStage().getWidth() * 0.41 - numberOfAutoSaveText.getLayoutBounds().getWidth() / 2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfPlayers = 0;
        Platform.runLater(this::runLaterPlease);
//        startGameButton.setPrefWidth();
//        sendInvitationButton.setLayoutX(StageController.getStage().getWidth() - sendInvitationButton.getWidth()*1.5);

    }

    private void runLaterPlease() {
        Font font = new Font(30 * StageController.getStage().getWidth() / 1920);

        autoMapToggle.fire();
        mapDetails.setOpacity(0.5);
        mapXY.setOpacity(0.5);
        moreMapXButton.setDisable(true);
        lessMapYButton.setDisable(true);


        setWithMoreLess(numberOfPlayersDetail,
                font, 0.91,
                numberOfPlayersTest, lessPlayersButton,
                morePlayersButton, String.valueOf(numberOfPlayers),
                "Increases the number of players",
                "Increases the number of players",0.05);

        autoMapToggle.setLayoutX(StageController.getStage().getWidth() * 0.91 - autoMapToggle.getWidth() / 2);
        autoMapToggle.setLayoutY(StageController.getStage().getHeight() * 0.18);
        autoMapToggle.setTooltip(new Tooltip("Set/onset Auto-generate-map"));

        setWithMoreLess(mapDetails, font, 0.91, mapXY,
                lessMapYButton, moreMapXButton, "X: " + mapX + " Y: " + mapY,
                "Increases the size of map",
                "Decreases the size of map",0.25);

        invitationId.setLayoutX(StageController.getStage().getWidth() * 0.91 - invitationId.getWidth() / 2);
        invitationId.setLayoutY(StageController.getStage().getHeight() * 0.60);
        sendInvitationButton.setLayoutX(StageController.getStage().getWidth() * 0.91 - sendInvitationButton.getWidth() / 2);
        sendInvitationButton.setLayoutY(StageController.getStage().getHeight() * 0.63);
        sendInvitationButton.setTooltip(new Tooltip("sends invitation to the username you type"));

        MenuItem[] menuItems = {new MenuItem("off"), new MenuItem("each round"), new MenuItem("each city occupation")};
        autoSaveOrNot.getItems().addAll(menuItems);
        autoSaveOrNot.setLayoutX(StageController.getStage().getWidth() * 0.91 - autoSaveOrNot.getWidth() / 2);
        autoSaveOrNot.setLayoutY(StageController.getStage().getHeight()*0.38);

        for (int i = 0; i < menuItems.length; i++) {
            int finalI = i;
            menuItems[i].setOnAction((e) -> {
                setAutoSave(finalI);
            });
        }

        setWithMoreLess(numberOfAutoSaveDetail, font, 0.91, numberOfAutoSaveText,
                lessAutoSavesButton, moreAutoSavesButton, String.valueOf(autoSaveNumbers),
                "Increases the number of autoSaves", "Decreases the number of autoSaves",0.45);

        startGameButton.setLayoutX(StageController.getScene().getWidth() - startGameButton.getWidth() * 1.5);
        startGameButton.setLayoutY(StageController.getScene().getHeight() - startGameButton.getHeight() * 1.5);
        startGameButton.setTooltip(new Tooltip("This button starts the game, obviously."));
        AnchorPane tempAnchorPane = new AnchorPane();
        savesListScrollBar.setPrefSize(StageController.getScene().getWidth()*0.06, StageController.getScene().getWidth()*0.1);
        savesListScrollBar.setContent(tempAnchorPane);
        savesListScrollBar.setLayoutY(StageController.getScene().getHeight()*0.5);
        selectedSaveNumber.setText("Selected Save: New Game");
        selectedSaveNumber.setFont(font);
        selectedSaveNumber.setX(StageController.getStage().getWidth()*0.11 - selectedSaveNumber.getLayoutBounds().getWidth()/2);
        savesListScrollBar.setLayoutX(StageController.getScene().getWidth()*0.11 - selectedSaveNumber.getLayoutBounds().getWidth()/2);
        selectedSaveNumber.setY(StageController.getStage().getHeight()*0.48);

        //hardcode save text
        Button[] saves = new Button[20];
        saves[0] = new Button();
        saves[0].setText("new Game");
        saves[0].setOnAction(actionEvent -> selectedSaveNumber.setText("Selected Save: New Game"));
        saves[0].setLayoutY(0);
        tempAnchorPane.getChildren().add(saves[0]);
        for (int i = 1; i < saves.length; i++) {
            saves[i] = new Button();
            saves[i].setText("Save" + i);
            saves[i].setLayoutY(i*StageController.getScene().getHeight()*0.03);
            int finalI = i;
            saves[i].setOnAction(actionEvent -> selectedSaveNumber.setText("Selected Save: save" + finalI));
            tempAnchorPane.getChildren().add(saves[i]);
        }
        //


//        savesListScrollBar.setOrientation(Orientation.VERTICAL);
//        savesListScrollBar.s

        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());
    }

    private EventHandler<ActionEvent> setAutoSave(int i) {
        System.out.println(i);
        autoSave = i;
        if (i == 0) {
            numberOfAutoSaveText.setOpacity(0.5);
            numberOfAutoSaveDetail.setOpacity(0.5);
            moreAutoSavesButton.setDisable(true);
            lessAutoSavesButton.setDisable(true);
        } else {
            numberOfAutoSaveText.setOpacity(1);
            numberOfAutoSaveDetail.setOpacity(1);
            moreAutoSavesButton.setDisable(false);
            lessAutoSavesButton.setDisable(false);
        }
        return null;
    }

    private void setWithMoreLess(Text textDetails, Font font, double percent, Text text, Button less,
                                 Button more, String textString, String increaseString, String decreaseString, double yPercent) {
        textDetails.setFont(font);
        textDetails.setX(StageController.getStage().getWidth() * percent - textDetails.getLayoutBounds().getWidth() / 2);
        textDetails.setY(StageController.getStage().getHeight() * yPercent);

        text.setText(textString);
        text.setFont(font);
        text.setX(StageController.getStage().getWidth() * percent - text.getLayoutBounds().getWidth() / 2);
        text.setY(StageController.getStage().getHeight() * (yPercent+0.05));

        more.setLayoutX(StageController.getStage().getWidth() * percent + more.getWidth() * 0.1);
        more.setLayoutY(text.getY() + text.getLayoutBounds().getHeight() / 2);
        more.setTooltip(new Tooltip(increaseString));

        less.setLayoutX(StageController.getStage().getWidth() * percent - 1.1 * less.getWidth());
        less.setLayoutY(text.getY() + text.getLayoutBounds().getHeight() / 2);
        less.setTooltip(new Tooltip(decreaseString));
    }
}
