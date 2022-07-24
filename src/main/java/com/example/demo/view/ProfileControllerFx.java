package com.example.demo.view;
import com.example.demo.HelloApplication;
import com.example.demo.controller.LoginController;
import com.example.demo.controller.NetworkController;
import com.example.demo.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProfileControllerFx implements Initializable {
    private static final double SIZE_RATIO = StageController.getScene().getWidth()/1536;
    @FXML private  ImageView background;
    @FXML private  AnchorPane pane;
    @FXML private  TextField nickname;
    private final HashMap<Rectangle,UserIcon> icons = new HashMap<>();
    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;
    private String avatarPath;
    @FXML
    private Button profile;
    public void initialize(URL location, ResourceBundle resources) {
        changeProfile(profile,200, LoginController.getLoggedUser().getAvatar());
        initializeElements();
        initializeIcons();
        Platform.runLater(() ->background.setFitWidth(StageController.getScene().getWidth()));
        Platform.runLater(() ->background.setFitHeight(StageController.getScene().getHeight()));

    }
    public static void changeProfile(Button profile,int size,String address){
        Image image = new Image(address);
        BackgroundImage b = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(size,size,false,false,false,false));
        Background background = new Background(b);
        profile.setBackground( background);
    }


    public void changeUserData(ActionEvent mouseEvent) {
        switch (LoginController.changeData(oldPassword.getText(),newPassword.getText(),nickname.getText(),avatarPath)){
            case 0:
                LoginController.saveUser();
                StageController.errorMaker("Successful","changed successfully", Alert.AlertType.INFORMATION);
                break;
            case 1:
                StageController.errorMaker("Input not valid","wrong password", Alert.AlertType.ERROR);
                break;
            case 2:
                StageController.errorMaker("Input not valid","enter new password", Alert.AlertType.ERROR);
                break;
            case 3:
                StageController.errorMaker("Input not valid","not a valid password", Alert.AlertType.ERROR);
                break;
            case 4:
                StageController.errorMaker("Input not valid","choose another nickname", Alert.AlertType.ERROR);
                break;
        }
        User.saveData();
        changeProfile(profile,(int)(200*SIZE_RATIO),LoginController.getLoggedUser().getAvatar());
    }


    public void delete(MouseEvent mouseEvent) {
        User.deleteUser(LoginController.getLoggedUser());
        StageController.sceneChanger("loginMenu.fxml");
    }

    public void back(MouseEvent mouseEvent) {
        NetworkController.send("menu exit");
        StageController.sceneChanger("mainMenu.fxml");
    }

    public void exit(MouseEvent mouseEvent) {
        StageController.sceneChanger("loginMenu.fxml");
    }

    private void initializeElements(){
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
            public void handle(ActionEvent e)
            {
                File file = avatarFile.showOpenDialog(StageController.getStage());
                if (file != null) {
                    label.setText(file.getAbsolutePath()
                            + "  selected");
                    try {
                        avatarPath =file.toURI().toURL().toExternalForm();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }


            }
        });

        for (Node child : pane.getChildren()) {
            if(child != background) {
                child.setLayoutX(child.getLayoutX() * SIZE_RATIO);
                child.setLayoutY(child.getLayoutY() * SIZE_RATIO);
            }
        }



    }
    private void initializeIcons(){
        int j = 0,i;
        System.out.println(StageController.getStage().getWidth());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(390 * SIZE_RATIO, 450* SIZE_RATIO);
        scrollPane.setLayoutX(25* SIZE_RATIO);
        scrollPane.setLayoutY(250* SIZE_RATIO);
        AnchorPane paneScroll = new AnchorPane();
        scrollPane.setContent(paneScroll);
        pane.getChildren().add(scrollPane);
        for ( i = 0; 3*i <UserIcon.ICON_NUMBER ; i++) {
            for ( j = 0; j < 3 && 3*i + j < UserIcon.ICON_NUMBER;j++){
                Rectangle rectangle = new Rectangle();
                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        LoginController.getLoggedUser().setIcon(icons.get(mouseEvent.getSource()));
                        changeProfile(profile, (int) (200* SIZE_RATIO),LoginController.getLoggedUser().getAvatar());
                        LoginController.saveUser();
                    }
                });
                rectangle.setWidth(100* SIZE_RATIO);
                rectangle.setHeight(100* SIZE_RATIO);
                rectangle.setLayoutX(5* SIZE_RATIO + 110 * j* SIZE_RATIO);
                rectangle.setLayoutY(5* SIZE_RATIO + 110 * i* SIZE_RATIO);
                rectangle.setFill(new ImagePattern(AssetsController.getUserAvatarImages().get(3*i + j)));
                paneScroll.getChildren().add(rectangle);
                icons.put(rectangle,UserIcon.getVALUES().get(3*i + j));

            }
        }

    }
}
