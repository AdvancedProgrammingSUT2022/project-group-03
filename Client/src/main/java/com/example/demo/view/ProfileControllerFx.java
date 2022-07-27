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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ProfileControllerFx implements Initializable {
    private static final double SIZE_RATIO = StageController.getScene().getWidth() / 1536;
    public ScrollPane gameInvitationScrollBar;
    public AnchorPane invitePane;
    public TextField sendRequestTextField;
    public Button sendRequestButton;
    private ScrollPane friendsListScrollPane;
    Timeline twoKilo;
    @FXML
    private ImageView background;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField nickname;
    private final HashMap<Rectangle, UserIcon> icons = new HashMap<>();
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    private String avatarPath;
    @FXML
    private Button profile;

    public void initialize(URL location, ResourceBundle resources) {
        changeProfile(profile, 200, LoginController.getLoggedUser().getAvatar());
        initializeElements();
        initializeIcons();
        initializeInvite();
        Platform.runLater(this::runLater);
        twoKilo = new Timeline(new KeyFrame(Duration.millis(10000), event -> {
                    LoginController.setLoggedUser(new Gson().fromJson(NetworkController.send("update"), User.class));
                    initializeInvite();
        }));
        twoKilo.setCycleCount(Animation.INDEFINITE);
        twoKilo.play();

    }


    private void runLater() {
        background.setFitWidth(StageController.getScene().getWidth());
        background.setFitHeight(StageController.getScene().getHeight());

        sendRequestButton.setOnAction(actionEvent -> {
            User user = User.findUser(sendRequestTextField.getText(), false);
            if (user == null)
                StageController.errorMaker("Invalid username", "No user with this username exists", Alert.AlertType.ERROR);
            else {
                sendFriendShipRequest(user);
                sendRequestTextField.setText("");
            }
        });


        sendRequestTextField.setPromptText("Type and click on the username");
        sendRequestTextField.setLayoutX(StageController.getStage().getWidth() * 0.75);
        sendRequestTextField.setLayoutY(StageController.getStage().getHeight() * 1 / 4);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        Platform.runLater(() -> {
            sendRequestButton.setPrefWidth(sendRequestTextField.getWidth());
            sendRequestButton.setText("Send Request");
            sendRequestButton.setLayoutX(sendRequestTextField.getLayoutX());
            sendRequestButton.setLayoutY(sendRequestTextField.getLayoutY() + sendRequestTextField.getHeight() * 1.5);
            anchorPane.setLayoutX(sendRequestTextField.getLayoutX());
            anchorPane.setLayoutY(sendRequestTextField.getLayoutY() + sendRequestTextField.getHeight());
            anchorPane.setPrefWidth(sendRequestTextField.getWidth());
        });

        sendRequestTextField.setOnKeyTyped(keyEvent -> {

            String string = NetworkController.send("getUserList");
            ArrayList<User> usersUpdate = new Gson().fromJson(string, new TypeToken<List<User>>() {
            }.getType());
            anchorPane.getChildren().clear();
            if (sendRequestTextField.getText().length() != 0) {
                int i = 0;
                for (User listOfUser : usersUpdate) {
                    if (listOfUser.getUsername().toLowerCase().startsWith(sendRequestTextField.getText().toLowerCase()) &&
                            !listOfUser.getUsername().equals(LoginController.getLoggedUser().getUsername())) {
                        Text text = new Text(listOfUser.getUsername());
                        text.setY(20 + i*35);
                        text.setX(35);
                        text.setCursor(Cursor.HAND);
                        Image image = AssetsController.getUserAvatarImages().get(0);
                        for (int k = 0; k < UserIcon.getVALUES().size(); k++) {
                            if(UserIcon.getVALUES().get(k) == listOfUser.getIcon() && listOfUser.getIcon() != UserIcon.CUSTOM)
                                image = AssetsController.getUserAvatarImages().get(k);
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(30);
                        imageView.setFitHeight(30);
                        imageView.setY( i*35);
                        imageView.setX(2);
                        imageView.setCursor(Cursor.HAND);
                        text.setOnMouseClicked(event -> {
                            sendRequestTextField.setText(text.getText());
                            anchorPane.getChildren().clear();
                        });
                        imageView.setOnMouseClicked(event -> {
                            sendRequestTextField.setText(text.getText());
                            anchorPane.getChildren().clear();
                        });

                        i += 30;
                        anchorPane.getChildren().add(imageView);
                        anchorPane.getChildren().add(text);
                    }
                }
            }
        });
        pane.getChildren().add(anchorPane);
        friendsListScrollPane =new ScrollPane();
        friendsListScrollPane.setLayoutX(StageController.getStage().getWidth()*0.75);
        friendsListScrollPane.setLayoutY(StageController.getStage().getHeight()*0.50);
        friendsListScrollPane.setPrefWidth(StageController.getStage().getWidth()*0.07);
        friendsListScrollPane.setPrefHeight(StageController.getStage().getHeight()*0.25);
        pane.getChildren().add(friendsListScrollPane);

        updateFriendsList();

    }

    private void updateFriendsList()
    {
        AnchorPane friendsListAnchorPane = new AnchorPane();
        friendsListScrollPane.setContent(friendsListAnchorPane);
        Panels.addText("Your Friends:", 5, 20, 20, null, friendsListAnchorPane);

        for (int i = 0; i < LoginController.getLoggedUser().getFriends().size(); i++)
            Panels.addText(LoginController.getLoggedUser().getFriends().get(i).getNickname(), 5, 40 + i * 30, 20, null, friendsListAnchorPane);
    }

    private void sendFriendShipRequest(User user) {
        switch (Integer.parseInt(NetworkController.send("friend; " + user.getUsername()))) {
            case 0 -> StageController.errorMaker("friendship never ends", "request sent", Alert.AlertType.INFORMATION);
            case 1 -> StageController.errorMaker("friendship never ends", "you already sent a request", Alert.AlertType.INFORMATION);
            case 2 -> StageController.errorMaker("friendship never ends", "already a friend", Alert.AlertType.INFORMATION);
            case 3 -> StageController.errorMaker("friendship never ends", "something went wrong", Alert.AlertType.INFORMATION);
            case 4 -> StageController.errorMaker("friendship never ends", "new friend!", Alert.AlertType.INFORMATION);
            case 5 -> StageController.errorMaker("dumb ass", "you are the biggest enemy of yourself", Alert.AlertType.ERROR);
        }
    }

    public static void changeProfile(Button profile, int size, String address) {
        if (address == null)
            address = "file:/A:/civ/project-group-03/src/main/resources/com/example/demo/" + UserIcon.ALEXANDER.getImage();
        Image image = new Image(address);
        BackgroundImage b = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(size, size, false, false, false, false));
        Background background = new Background(b);
        profile.setBackground(background);
    }


    public void changeUserData() {
        switch (LoginController.changeData(oldPassword.getText(), newPassword.getText(), nickname.getText(), avatarPath)) {
            case 0 -> {
                LoginController.saveUser();
                StageController.errorMaker("Successful", "changed successfully", Alert.AlertType.INFORMATION);
            }
            case 1 -> StageController.errorMaker("Input not valid", "wrong password", Alert.AlertType.ERROR);
            case 2 -> StageController.errorMaker("Input not valid", "enter new password", Alert.AlertType.ERROR);
            case 3 -> StageController.errorMaker("Input not valid", "not a valid password", Alert.AlertType.ERROR);
            case 4 -> StageController.errorMaker("Input not valid", "choose another nickname", Alert.AlertType.ERROR);
        }
        User.saveData();
        changeProfile(profile, (int) (200 * SIZE_RATIO), LoginController.getLoggedUser().getAvatar());
    }


    public void delete() {
        User.deleteUser(LoginController.getLoggedUser());
        StageController.sceneChanger("loginMenu.fxml");
    }

    public void back() {
        twoKilo.stop();
        NetworkController.send("menu exit");
        StageController.sceneChanger("mainMenu.fxml");
    }

    public void exit() {
        StageController.sceneChanger("loginMenu.fxml");
    }

    private void initializeElements() {
        FileChooser avatarFile = new FileChooser();
        Button button = new Button("Choose custom avatar");
        button.setLayoutX(170);
        button.setLayoutY(750);
        Label label = new Label("no file selected");
        label.setLayoutX(150);
        label.setLayoutY(800);
        label.setStyle("-fx-font-size: 25;-fx-text-fill: white");
        pane.getChildren().add(button);
        pane.getChildren().add(label);
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                File file = avatarFile.showOpenDialog(StageController.getStage());
                if (file != null) {
                    label.setText(file.getAbsolutePath()
                            + "  selected");
                    try {
                        avatarPath = file.toURI().toURL().toExternalForm();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }


            }
        });

        for (Node child : pane.getChildren()) {
            if (child != background) {
                child.setLayoutX(child.getLayoutX() * SIZE_RATIO);
                child.setLayoutY(child.getLayoutY() * SIZE_RATIO);
            }
        }


    }

    private void initializeIcons() {
        int j = 0, i;
        System.out.println(StageController.getStage().getWidth());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(390 * SIZE_RATIO, 450 * SIZE_RATIO);
        scrollPane.setLayoutX(25 * SIZE_RATIO);
        scrollPane.setLayoutY(250 * SIZE_RATIO);
        AnchorPane paneScroll = new AnchorPane();
        scrollPane.setContent(paneScroll);
        pane.getChildren().add(scrollPane);
        for (i = 0; 3 * i < UserIcon.ICON_NUMBER; i++) {
            for (j = 0; j < 3 && 3 * i + j < UserIcon.ICON_NUMBER; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        LoginController.getLoggedUser().setIcon(icons.get(mouseEvent.getSource()));
                        changeProfile(profile, (int) (200 * SIZE_RATIO), LoginController.getLoggedUser().getAvatar());
                        LoginController.saveUser();
                    }
                });
                rectangle.setWidth(100 * SIZE_RATIO);
                rectangle.setHeight(100 * SIZE_RATIO);
                rectangle.setLayoutX(5 * SIZE_RATIO + 110 * j * SIZE_RATIO);
                rectangle.setLayoutY(5 * SIZE_RATIO + 110 * i * SIZE_RATIO);
                rectangle.setFill(new ImagePattern(AssetsController.getUserAvatarImages().get(3 * i + j)));
                paneScroll.getChildren().add(rectangle);
                icons.put(rectangle, UserIcon.getVALUES().get(3 * i + j));

            }
        }
        initializeInvite();


    }

    private void initializeInvite() {
        invitePane = new AnchorPane();
        gameInvitationScrollBar.setContent(invitePane);
        gameInvitationScrollBar.setPrefSize(StageController.getScene().getWidth() / 1920 * 250, StageController.getScene().getHeight() * 0.25);
        gameInvitationScrollBar.setLayoutY(StageController.getScene().getHeight() * 0.5);
        gameInvitationScrollBar.setLayoutX(StageController.getScene().getWidth() * 0.05 + 500);
        Panels.addText("FriendShip Requests:", 5, 20, 20, null, invitePane);
        ArrayList<User> requests = LoginController.getLoggedUser().getFriendsRequest();
        for (int i = 0; i < requests.size(); i++) {
            Button button1 = Panels.addButton("Accept",
                    StageController.getScene().getWidth() / 1920 * 150, 50 + i * 70, 80, 30, invitePane);
            User user = requests.get(i);
            button1.setOnAction(actionEvent -> acceptInvite(user));
            button1 = Panels.addButton("Decline", StageController.getScene().getWidth() / 1920 * 150, 80 + i * 70, 80, 30, invitePane);
            button1.setOnAction(actionEvent -> declineInvite(user));
            Panels.addText(user.getNickname(), 5, 80 + i * 70, 20, null, invitePane);
        }
    }

    private void acceptInvite(User user) {
        if (4 == Integer.parseInt(NetworkController.send("friend " + user.getUsername()))) {
            StageController.errorMaker("friendship never ends", "new friend!", Alert.AlertType.INFORMATION);
        } else {
            StageController.errorMaker("friendship never ends", "something went wrong", Alert.AlertType.ERROR);
        }
        initializeInvite();
    }

    private void declineInvite(User user) {
        if (Integer.parseInt(NetworkController.send("rm " + user.getUsername())) == 0)
            StageController.errorMaker("The friendship may ends", "nice!", Alert.AlertType.INFORMATION);
        else
            StageController.errorMaker("friendship never ends", "something went wrong", Alert.AlertType.ERROR);

        initializeInvite();
    }
}
