package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Map;
import com.example.demo.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameEntryMenuFx implements Initializable {
    public Text numberOfAutoSaveDetail;

    public Text numberOfAutoSaveText;
    public Button lessAutoSavesButton;
    public Button moreAutoSavesButton;
    public ImageView background;
    public ScrollPane savesListScrollBar;
    public ScrollPane autoSavesListScrollBar;
    public Text titleManual;
    public Button back;
    public TextField addPlayerId;
    public Button addPlayerButton;
    public AnchorPane pane;
    int mapX = 60, mapY = 90;
    int autoSave = 0;
    int autoSaveNumbers = 5;
    public ToggleButton autoMapToggle;
    public Button moreMapXButton, lessMapYButton, morePlayersButton, lessPlayersButton;
    public Text mapXY, numberOfPlayersDetail, mapDetails, numberOfPlayersTest;
    public TextField invitationId;
    public SplitMenuButton autoSaveOrNot;
    int numberOfPlayers = 1;
    public ArrayList<User> users = new ArrayList<>();
    @FXML
    Button startGameButton, sendInvitationButton;
    private boolean autoSaveIsEnabled;
    private boolean autoSaveAtChangingOnMap;
    private AnchorPane addedUsersAnchorPane;

    @FXML
    public void startGame() {
        if(numberOfPlayers<2)
        {
            StageController.errorMaker("can't start the game", "the number of players is too short", Alert.AlertType.ERROR);
            return;
        }
        Map.setX(mapX);
        Map.setY(mapY);
        SavingHandler.autoSaveIsEnabled = autoSaveIsEnabled;
        SavingHandler.autoSaveAtRenderingMap = autoSaveAtChangingOnMap;
        SavingHandler.numberOfAutoSaving = autoSaveNumbers;
        if(users.size()<numberOfPlayers)
            for (int i = 0; i < numberOfPlayers - users.size(); i++)
                users.add(new User("bot" + i, "bot" + i, "bot" + i,false));
        GameController.startGame(users);
        StageController.sceneChanger("game.fxml");
    }

    @FXML
    private void loadGame(String savingName, boolean isManual) {
        SavingHandler.autoSaveIsEnabled = autoSaveIsEnabled;
        SavingHandler.autoSaveAtRenderingMap = autoSaveAtChangingOnMap;
        SavingHandler.numberOfAutoSaving = autoSaveNumbers;

        SavingHandler.load(savingName, isManual);
        StageController.sceneChanger("game.fxml");
    }

    @FXML
    public void sendInvitation() {

    }

    @FXML
    public void addPlayer() {
        User user = User.findUser(addPlayerId.getText(), false);
        addPlayerId.setText("");
        boolean firstBool = user == null, secondBool = users.contains(user);
        if (firstBool ||
                secondBool) {
            if (firstBool)
                StageController.errorMaker("You can't add this player", "No user with this Id exists", Alert.AlertType.ERROR);
            else
                StageController.errorMaker("You can't add this player", "The selected user is already in your game", Alert.AlertType.ERROR);
            return;
        }
        StageController.errorMaker("Done", "The selected user added to game successfully", Alert.AlertType.INFORMATION);
        users.add(user);
        if (users.size() > numberOfPlayers) {
            numberOfPlayers = users.size();
            updateNumberOfUsersText();
        }
        setAddedUsersAnchorPane();

    }

    @FXML
    public void morePlayers() {
        numberOfPlayers++;
        updateNumberOfUsersText();
    }

    @FXML
    public void lessPlayers() {
        if (numberOfPlayers < 2)
            return;
        numberOfPlayers--;
        updateNumberOfUsersText();
    }

    private void updateNumberOfUsersText() {

        numberOfPlayersTest.setText(String.valueOf(numberOfPlayers));
        numberOfPlayersTest.setX(StageController.getStage().getWidth() * 0.91 - numberOfPlayersTest.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void moreMapX() {
        mapX += 2;
        mapY += 3;
        mapXY.setText("X: " + mapX + " Y: " + mapY);
        mapXY.setX(StageController.getStage().getWidth() * 0.91 - mapXY.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void lessMapY() {
        if (mapX < 20)
            return;
        mapX -= 2;
        mapY -= 3;
        mapXY.setText("X: " + mapX + " Y: " + mapY);
        mapXY.setX(StageController.getStage().getWidth() * 0.91 - mapXY.getLayoutBounds().getWidth() / 2);
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
        numberOfAutoSaveText.setX(StageController.getStage().getWidth() * 0.91 - numberOfAutoSaveText.getLayoutBounds().getWidth() / 2);
    }

    @FXML
    public void lessAutoSave() {
        if (autoSaveNumbers < 2)
            return;
        autoSaveNumbers--;
        numberOfAutoSaveText.setText(String.valueOf(autoSaveNumbers));
        numberOfAutoSaveText.setX(StageController.getStage().getWidth() * 0.91 - numberOfAutoSaveText.getLayoutBounds().getWidth() / 2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOfPlayers = 0;
        users = new ArrayList<>();
        users.add(LoginController.getLoggedUser());
        Platform.runLater(this::runLaterPlease);
//        startGameButton.setPrefWidth();
//        sendInvitationButton.setLayoutX(StageController.getStage().getWidth() - sendInvitationButton.getWidth()*1.5);

    }

    private void runLaterPlease() {
        Font font = new Font(30 * StageController.getStage().getWidth() / 1920);

        back.setLayoutX(StageController.getStage().getWidth() * 0.02);
        back.setLayoutY(StageController.getStage().getHeight() - back.getHeight() * 1.5);

        autoMapToggle.fire();
        mapDetails.setOpacity(0.5);
        mapXY.setOpacity(0.5);
        moreMapXButton.setDisable(true);
        lessMapYButton.setDisable(true);


        numberOfPlayers = 1;
        setWithMoreLess(numberOfPlayersDetail,
                font, 0.91,
                numberOfPlayersTest, lessPlayersButton,
                morePlayersButton, String.valueOf(numberOfPlayers),
                "Increases the number of players",
                "Increases the number of players", 0.05);

        autoMapToggle.setLayoutX(StageController.getStage().getWidth() * 0.91 - autoMapToggle.getWidth() / 2);
        autoMapToggle.setLayoutY(StageController.getStage().getHeight() * 0.18);
        autoMapToggle.setTooltip(new Tooltip("Set/onset Auto-generate-map"));

        setWithMoreLess(mapDetails, font, 0.91, mapXY,
                lessMapYButton, moreMapXButton, "X: " + mapX + " Y: " + mapY,
                "Increases the size of map",
                "Decreases the size of map", 0.25);

        invitationId.setLayoutX(StageController.getStage().getWidth() * 0.91 - invitationId.getWidth() / 2);
        invitationId.setLayoutY(StageController.getStage().getHeight() * 0.60);
        sendInvitationButton.setLayoutX(StageController.getStage().getWidth() * 0.91 - sendInvitationButton.getWidth() / 2);
        sendInvitationButton.setLayoutY(StageController.getStage().getHeight() * 0.63);
        sendInvitationButton.setTooltip(new Tooltip("sends invitation to the username you type"));

        addPlayerId.setLayoutX(StageController.getStage().getWidth() * 0.91 - addPlayerId.getWidth() / 2);
        addPlayerId.setLayoutY(StageController.getStage().getHeight() * 0.72);
        addPlayerButton.setLayoutX(StageController.getStage().getWidth() * 0.91 - addPlayerButton.getWidth() / 2);
        addPlayerButton.setLayoutY(StageController.getStage().getHeight() * 0.75);
        addPlayerButton.setTooltip(new Tooltip("adds the username you type"));

        MenuItem[] menuItems = {new MenuItem("off"), new MenuItem("each turn"), new MenuItem("each change in map")};
        autoSaveOrNot.getItems().addAll(menuItems);
        autoSaveOrNot.setLayoutX(StageController.getStage().getWidth() * 0.91 - autoSaveOrNot.getWidth() / 2);
        autoSaveOrNot.setLayoutY(StageController.getStage().getHeight() * 0.38);

        for (int i = 0; i < menuItems.length; i++) {
            int finalI = i;
            menuItems[i].setOnAction((e) -> {
                setAutoSave(finalI);
            });
        }

        setWithMoreLess(numberOfAutoSaveDetail, font, 0.91, numberOfAutoSaveText,
                lessAutoSavesButton, moreAutoSavesButton, String.valueOf(autoSaveNumbers),
                "Increases the number of autoSaves", "Decreases the number of autoSaves", 0.45);

        startGameButton.setLayoutX(StageController.getScene().getWidth() - startGameButton.getWidth() * 1.5);
        startGameButton.setLayoutY(StageController.getScene().getHeight() - startGameButton.getHeight() * 1.5);
        startGameButton.setTooltip(new Tooltip("This button starts the game, obviously."));
        VBox savingList = new VBox();
        VBox autoSavingList = new VBox();

        savesListScrollBar.setPrefSize(180, StageController.getScene().getWidth() * 0.25);
        savesListScrollBar.setContent(savingList);
        savesListScrollBar.setLayoutY(StageController.getScene().getHeight() * 0.5);
        savesListScrollBar.setLayoutX(StageController.getScene().getWidth() * 0.05);

        autoSavesListScrollBar.setPrefSize(180, StageController.getScene().getWidth() * 0.25);
        autoSavesListScrollBar.setContent(autoSavingList);
        autoSavesListScrollBar.setLayoutY(StageController.getScene().getHeight() * 0.5);
        autoSavesListScrollBar.setLayoutX(StageController.getScene().getWidth() * 0.05 + 250);

        titleManual.setText("load a manual save     or    load an auto save.");
        titleManual.setFont(font);
        titleManual.setX(StageController.getScene().getWidth() * 0.05);
        titleManual.setY(StageController.getStage().getHeight() * 0.48);


        for (String savingName : SavingHandler.getManualSaves()) {
            Button button = new Button(savingName);
            button.setOnAction(actionEvent -> loadGame(savingName, true));
            button.setMinWidth(180);
            button.setMinHeight(30);
            savingList.getChildren().add(button);
        }
        Button button = new Button("delete manual savings");
        button.setMinWidth(180);
        button.setMinHeight(30);
        button.setOnAction(actionEvent -> {
            SavingHandler.deleteAllManuals();
            savingList.getChildren().clear();
        });
        savingList.getChildren().add(button);

        for (String savingName : SavingHandler.getAutoSaves()) {
            Button button2 = new Button(savingName);
            button2.setOnAction(actionEvent -> loadGame(savingName, false));
            button2.setMinWidth(180);
            button2.setMinHeight(30);
            autoSavingList.getChildren().add(button2);
        }
        Button button2 = new Button("delete auto savings");
        button2.setMinWidth(180);
        button2.setMinHeight(30);
        button2.setOnAction(actionEvent -> {
            SavingHandler.deleteAllAutos();
            autoSavingList.getChildren().clear();
        });
        autoSavingList.getChildren().add(button2);


        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());

        ScrollPane addedUsersScrollPane = new ScrollPane();
        addedUsersScrollPane.setLayoutX(addPlayerId.getLayoutX());
        addedUsersScrollPane.setLayoutY(addPlayerButton.getLayoutY() + addPlayerButton.getHeight()*1.2);
        addedUsersAnchorPane = new AnchorPane();
        addedUsersAnchorPane.setPrefWidth(addPlayerId.getWidth());

        addedUsersScrollPane.setContent(addedUsersAnchorPane);
        setAddedUsersAnchorPane();
        pane.getChildren().add(addedUsersScrollPane);


    }

    private void setAddedUsersAnchorPane()
    {
        addedUsersAnchorPane.getChildren().clear();
        Text text = new Text("Current Players:");
        text.setLayoutY(10);
        addedUsersAnchorPane.getChildren().add(text);
        System.out.println(users.size());
        for (int i = 0; i < users.size(); i++) {
            Text text1 = new Text(users.get(i).getNickname());
            text1.setLayoutY((i+1)*15 + 15);
            addedUsersAnchorPane.getChildren().add(text1);
        }
    }

    public void back(MouseEvent mouseEvent) {
        StageController.sceneChanger("mainMenu.fxml");
    }

    private void setAutoSave(int i) {
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
        if (i == 0) {
            autoSaveIsEnabled = false;
        } else if (i == 1) {
            autoSaveIsEnabled = true;
            autoSaveAtChangingOnMap = false;
        } else {
            autoSaveIsEnabled = true;
            autoSaveAtChangingOnMap = true;
        }
    }

    private void setWithMoreLess(Text textDetails, Font font, double percent, Text text, Button less,
                                 Button more, String textString, String increaseString, String decreaseString, double yPercent) {

        textDetails.setFont(font);
        textDetails.setX(StageController.getStage().getWidth() * percent - textDetails.getLayoutBounds().getWidth() / 2);
        textDetails.setY(StageController.getStage().getHeight() * yPercent);

        text.setText(textString);
        text.setFont(font);
        text.setX(StageController.getStage().getWidth() * percent - text.getLayoutBounds().getWidth() / 2);
        text.setY(StageController.getStage().getHeight() * (yPercent + 0.05));

        more.setLayoutX(StageController.getStage().getWidth() * percent + more.getWidth() * 0.1);
        more.setLayoutY(text.getY() + text.getLayoutBounds().getHeight() / 2);
        more.setTooltip(new Tooltip(increaseString));

        less.setLayoutX(StageController.getStage().getWidth() * percent - 1.1 * less.getWidth());
        less.setLayoutY(text.getY() + text.getLayoutBounds().getHeight() / 2);
        less.setTooltip(new Tooltip(decreaseString));
    }
}
