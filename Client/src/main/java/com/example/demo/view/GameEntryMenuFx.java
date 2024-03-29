package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.controller.NetworkController;
import com.example.demo.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    public AnchorPane pane;
    public ScrollPane gameInvitationScrollBar;
    public Button creatGame;
    int mapX = 60, mapY = 90;
    int autoSave = 0;
    int autoSaveNumbers = 5;
    public ToggleButton autoMapToggle;
    public Button moreMapXButton, lessMapYButton;
    public Text mapXY, numberOfPlayersDetail, mapDetails, numberOfPlayersTest;
    public TextField invitationId;
    public SplitMenuButton autoSaveOrNot;
    int numberOfPlayers = 1;
    public ArrayList<User> users = new ArrayList<>();
    private Timeline twoKilo;
    @FXML
    Button startGameButton, sendInvitationButton;
    private boolean autoSaveIsEnabled;
    private boolean autoSaveAtChangingOnMap;
    private AnchorPane addedUsersAnchorPane;

    @FXML
    public void startGame() {
        switch (Integer.parseInt(NetworkController.send("start " + mapX + " " + mapY))) {
            case 0 -> StageController.errorMaker("start game", "started", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("start game", "wait for owner", Alert.AlertType.INFORMATION);
            case 2 -> StageController.errorMaker("start game", "invite more players", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void loadGame(String savingName, boolean isManual) {
        SavingHandler.autoSaveIsEnabled = autoSaveIsEnabled;
        SavingHandler.autoSaveAtRenderingMap = autoSaveAtChangingOnMap;
        SavingHandler.numberOfAutoSaving = autoSaveNumbers;
        if (!isManual)
            SavingHandler.autoSaveIsEnabled = true;
        StageController.sceneChanger("game.fxml");
    }

    @FXML
    public void sendInvitation() {
        switch (Integer.parseInt(NetworkController.send("invite " + invitationId.getText() + ";"))) {
            case 0 -> StageController.errorMaker("invitation", "invite sent", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("invitation", "no user with this id", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("invitation", "only one invite, wait for response", Alert.AlertType.ERROR);
        }


    }

    @FXML
    public void createGame() {
        if(0==Integer.parseInt(NetworkController.send("create game"))){
            StageController.errorMaker("game","successful", Alert.AlertType.INFORMATION);
        }else {
            StageController.errorMaker("game","something went wrong", Alert.AlertType.ERROR);
        }

    }


    private void updateNumberOfUsersText() {
        numberOfPlayers = users.size();
        if(numberOfPlayers==0)
            numberOfPlayers=1;
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
        Platform.runLater(this::runLaterPlease);
        twoKilo = new Timeline(
                new KeyFrame(Duration.millis(10000), event -> {
                    LoginController.setLoggedUser(new Gson().fromJson(NetworkController.send("update"),User.class));
                    String string = NetworkController.getResponse(true);
                    if(string.startsWith("start")){
                        twoKilo.stop();
                        SavingHandler.load();
                        StageController.sceneChanger("game.fxml");
                    }
                    else {
                        ArrayList<User> usersUpdate = new Gson().fromJson(string, new TypeToken<List<User>>() {
                        }.getType());
                        System.out.println("before: " + users.size());
                        if (usersUpdate != null) users = usersUpdate;
                        System.out.println("after: " + users.size());
                        updateNumberOfUsersText();
                        setAddedUsersAnchorPane();
                        initializeInvite();
                    }
                })
        );
        twoKilo.setCycleCount(Animation.INDEFINITE);
        twoKilo.play();
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

        numberOfPlayersDetail.setFont(font);
        numberOfPlayersDetail.setX(StageController.getStage().getWidth() * 0.91 - numberOfPlayersDetail.getLayoutBounds().getWidth() / 2);
        numberOfPlayersDetail.setY(StageController.getStage().getHeight() * 0.05);
        numberOfPlayersTest.setText(String.valueOf(numberOfPlayers));
        numberOfPlayersTest.setFont(font);
        numberOfPlayersTest.setX(StageController.getStage().getWidth() * 0.91 - numberOfPlayersTest.getLayoutBounds().getWidth() / 2);
        numberOfPlayersTest.setY(StageController.getStage().getHeight() * (0.05 + 0.05));


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

        creatGame.setLayoutX(StageController.getStage().getWidth() * 0.91 - creatGame.getWidth() / 2);
        creatGame.setLayoutY(StageController.getStage().getHeight() * 0.75);
        creatGame.setTooltip(new Tooltip("adds the username you type"));

        MenuItem[] menuItems = {new MenuItem("off"), new MenuItem("each turn"), new MenuItem("each change in map")};
        autoSaveOrNot.getItems().addAll(menuItems);
        autoSaveOrNot.setLayoutX(StageController.getStage().getWidth() * 0.91 - autoSaveOrNot.getWidth() / 2);
        autoSaveOrNot.setLayoutY(StageController.getStage().getHeight() * 0.38);

        for (int i = 0; i < menuItems.length; i++) {
            int finalI = i;
            menuItems[i].setOnAction((e) -> setAutoSave(finalI));
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
        titleManual.setFont(new Font(20*StageController.getStage().getWidth() / 1920));
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
        addedUsersScrollPane.setLayoutY(creatGame.getLayoutY() + creatGame.getHeight() * 1.2);
        addedUsersAnchorPane = new AnchorPane();

        addedUsersScrollPane.setContent(addedUsersAnchorPane);
        setAddedUsersAnchorPane();
        pane.getChildren().add(addedUsersScrollPane);
        initializeInvite();

    }

    private void initializeInvite()
    {
        AnchorPane gameInvitationPane = new AnchorPane();
        gameInvitationScrollBar.setContent(gameInvitationPane);
        gameInvitationScrollBar.setPrefSize(StageController.getScene().getWidth()/1920*250, StageController.getScene().getHeight() * 0.40);
        gameInvitationScrollBar.setLayoutY(StageController.getScene().getHeight() * 0.5);
        gameInvitationScrollBar.setLayoutX(StageController.getScene().getWidth() * 0.05 + 500);
        Panels.addText("Game Invitations:",5,20,20,null,gameInvitationPane);
        for (int i = 0; i < LoginController.getLoggedUser().getInvites().size(); i++) {
            Button button1 = Panels.addButton("Accept",
                    StageController.getScene().getWidth()/1920*150,50 + i*70,80,30,gameInvitationPane);
            User user = User.userFromArray(LoginController.getLoggedUser().getInvites().get(i),new Gson().fromJson(NetworkController.send("getUserList"),
                    new TypeToken<List<User>>() {}.getType()),false);
            button1.setOnAction(actionEvent -> acceptInvite(user));
            button1 = Panels.addButton("Ignore",StageController.getScene().getWidth()/1920*150,80+i*70,80,30,gameInvitationPane);
            button1.setOnAction(actionEvent -> declineInvite(user));
            Panels.addText(user.getNickname(),5,80+i*70,20,null,gameInvitationPane);
        }
    }
    private void acceptInvite(User user)
    {
        switch (NetworkController.send("accept invite " + user.getUsername() + ";")) {
            case "0" -> {
                StageController.errorMaker("Invite accepted!", "You've joined the game now. wait until the host starts the game", Alert.AlertType.INFORMATION);
                for (Node child : pane.getChildren()) {
                    child.setDisable(true);
                }
            }
            case "1" -> StageController.errorMaker("invite", "there is no game", Alert.AlertType.ERROR);
            case "2" -> StageController.errorMaker("invite", "something went wrong", Alert.AlertType.ERROR);
        }
        initializeInvite();
    }

    private void declineInvite(User user)
    {
        if(!NetworkController.send("decline invite "+ user.getUsername()+";").equals("0"))
            StageController.errorMaker("invite","something went wrong", Alert.AlertType.INFORMATION);
        initializeInvite();
    }

    private void setAddedUsersAnchorPane() {
        addedUsersAnchorPane.getChildren().clear();
        Text text = new Text("Current Players:");
        text.setLayoutY(10);
        addedUsersAnchorPane.getChildren().add(text);
        for (int i = 0; i < users.size(); i++) {
            Text text1 = new Text(users.get(i).getNickname());
            text1.setLayoutY((i + 1) * 15 + 15);
            addedUsersAnchorPane.getChildren().add(text1);
        }
        if(users.size()==0)
        {
            Text text1 = new Text(LoginController.getLoggedUser().getNickname());
            text1.setLayoutY(30);
            addedUsersAnchorPane.getChildren().add(text1);
        }
    }

    public void back() {
        NetworkController.send("menu exit");
        twoKilo.stop();
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
