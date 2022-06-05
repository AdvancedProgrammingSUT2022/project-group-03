package com.example.demo.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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

        numberOfPlayersDetail.setFont(font);
        numberOfPlayersDetail.setX(StageController.getStage().getWidth() * 0.92 - numberOfPlayersDetail.getLayoutBounds().getWidth() / 2);
        numberOfPlayersDetail.setY(StageController.getStage().getHeight() * 0.05);

        numberOfPlayersTest.setText(String.valueOf(numberOfPlayers));
        numberOfPlayersTest.setFont(font);
        numberOfPlayersTest.setX(StageController.getStage().getWidth() * 0.92 - numberOfPlayersTest.getLayoutBounds().getWidth() / 2);
        numberOfPlayersTest.setY(StageController.getStage().getHeight() * 0.1);

        morePlayersButton.setLayoutX(StageController.getStage().getWidth() * 0.92 + morePlayersButton.getWidth() * 0.1);
        morePlayersButton.setLayoutY(numberOfPlayersTest.getY() + numberOfPlayersTest.getLayoutBounds().getHeight() / 2);
        morePlayersButton.setTooltip(new Tooltip("Increases the number of players"));

        lessPlayersButton.setLayoutX(StageController.getStage().getWidth() * 0.92 - 1.1 * lessPlayersButton.getWidth());
        lessPlayersButton.setLayoutY(numberOfPlayersTest.getY() + numberOfPlayersTest.getLayoutBounds().getHeight() / 2);
        lessPlayersButton.setTooltip(new Tooltip("Decreases the number of players"));


        autoMapToggle.setLayoutX(StageController.getStage().getWidth() * 0.80 - autoMapToggle.getWidth() / 2);
        autoMapToggle.setLayoutY(StageController.getStage().getHeight() * 0.1);
        autoMapToggle.setTooltip(new Tooltip("Set/onset Auto-generate-map"));

        mapDetails.setFont(font);
        mapDetails.setX(StageController.getStage().getWidth() * 0.68 - mapDetails.getLayoutBounds().getWidth() / 2);
        mapDetails.setY(StageController.getStage().getHeight() * 0.05);

        mapXY.setFont(font);
        mapXY.setText("X: " + mapX + " Y: " + mapY);
        mapXY.setX(StageController.getStage().getWidth() * 0.68 - mapXY.getLayoutBounds().getWidth() / 2);
        mapXY.setY(StageController.getStage().getHeight() * 0.1);

        moreMapXButton.setLayoutX(StageController.getStage().getWidth() * 0.68 + moreMapXButton.getWidth() * 0.1);
        moreMapXButton.setLayoutY(mapXY.getY() + mapXY.getLayoutBounds().getHeight() / 2);
        moreMapXButton.setTooltip(new Tooltip("Increases the size of map"));

        lessMapYButton.setLayoutX(StageController.getStage().getWidth() * 0.68 - lessMapYButton.getWidth() * 1.1);
        lessMapYButton.setLayoutY(mapXY.getY() + mapXY.getLayoutBounds().getHeight() / 2);
        lessMapYButton.setTooltip(new Tooltip("Decreases the size of map"));


        invitationId.setLayoutX(StageController.getStage().getWidth() * 0.56 - invitationId.getWidth() / 2);
        invitationId.setLayoutY(StageController.getStage().getHeight() * 0.05);
        sendInvitationButton.setLayoutX(StageController.getStage().getWidth() * 0.56 - sendInvitationButton.getWidth() / 2);
        sendInvitationButton.setLayoutY(StageController.getStage().getHeight() * 0.1);
        sendInvitationButton.setTooltip(new Tooltip("sends invitation to the username you type"));

        MenuItem[] menuItems = {new MenuItem("off"), new MenuItem("each round"), new MenuItem("each city occupation")};
        autoSaveOrNot.getItems().addAll(menuItems);

        for (int i = 0; i < menuItems.length; i++) {
            int finalI = i;
            menuItems[i].setOnAction((e) -> {
               setAutoSave(finalI);
            });
        }

        numberOfAutoSaveDetail.setFont(font);
        numberOfAutoSaveDetail.setX(StageController.getStage().getWidth() * 0.41 - numberOfAutoSaveDetail.getLayoutBounds().getWidth() / 2);
        numberOfAutoSaveDetail.setY(StageController.getStage().getHeight() * 0.05);

        numberOfAutoSaveText.setText(String.valueOf(autoSaveNumbers));
        numberOfAutoSaveText.setFont(font);
        numberOfAutoSaveText.setX(StageController.getStage().getWidth() * 0.41 - numberOfAutoSaveText.getLayoutBounds().getWidth() / 2);
        numberOfAutoSaveText.setY(StageController.getStage().getHeight() * 0.1);

        moreAutoSavesButton.setLayoutX(StageController.getStage().getWidth() * 0.41 + moreAutoSavesButton.getWidth() * 0.1);
        moreAutoSavesButton.setLayoutY(numberOfAutoSaveText.getY() + numberOfAutoSaveText.getLayoutBounds().getHeight() / 2);
        moreAutoSavesButton.setTooltip(new Tooltip("Increases the number of autoSaves"));

        lessAutoSavesButton.setLayoutX(StageController.getStage().getWidth() * 0.41 - 1.1 * lessAutoSavesButton.getWidth());
        lessAutoSavesButton.setLayoutY(numberOfAutoSaveText.getY() + numberOfAutoSaveText.getLayoutBounds().getHeight() / 2);
        lessAutoSavesButton.setTooltip(new Tooltip("Decreases the number of autoSaves"));

        startGameButton.setLayoutX(StageController.getScene().getWidth() - startGameButton.getWidth() * 1.5);
        startGameButton.setLayoutY(StageController.getScene().getHeight() - startGameButton.getHeight() * 1.5);
        startGameButton.setTooltip(new Tooltip("This button starts the game, obviously."));

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
}
