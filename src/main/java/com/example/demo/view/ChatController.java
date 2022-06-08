package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.model.Chat;
import com.example.demo.model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;

public class ChatController {

    @FXML
    private Button sendButton;
    @FXML
    private TextField messageField;
    @FXML
    private VBox usersBar;
    @FXML
    private VBox mainSection;

    private VBox chatVBox;
    private ScrollPane scroll;
    private List<Chat> chats = new ArrayList<>();
    private Chat currentChat;


    public void initialize() {
        try {
            FileInputStream fileInputStream = new FileInputStream("dataBase/chats.dat");
            ObjectInputStream objectStream = new ObjectInputStream(fileInputStream);
            chats = (List<Chat>) objectStream.readObject();
            fileInputStream.close();
            objectStream.close();
        } catch (Exception e) {
            System.out.println("Can not load chats :(");
            System.out.println("Error : " + Arrays.toString(e.getStackTrace()));
        }
        if (chats.size() == 0) {
            Chat publicChat = new Chat("publicChat");
            chats.add(publicChat);
        }

        showUsersBar();
    }


    private void showUsersBar() {
        usersBar.getChildren().clear();
        for (Chat chat : chats) {
            Button button = new Button(chat.getName());
            button.setOnAction(event -> startChatting(chat.getName()));
            usersBar.getChildren().add(button);
        }
    }

    public void startChatting(String chatName) {
        mainSection.getChildren().clear();
        messageField.setDisable(false);
        sendButton.setDisable(false);
        mainSection.getChildren().clear();
        chatVBox = new VBox();
        chatVBox.setAlignment(Pos.BOTTOM_CENTER);
        chatVBox.setSpacing(15);
        chatVBox.setStyle("-fx-background-color: #0e1621");
        chatVBox.setFillWidth(true);
        scroll = new ScrollPane(chatVBox);
        scroll.setStyle("-fx-background: #0e1621; -fx-border-color: #0e1621; -fx-padding: 10");
        mainSection.getChildren().add(scroll);

        loadChats(chatName);

        scroll.vvalueProperty().bind(chatVBox.heightProperty());
        messageField.requestFocus();
    }

    private void loadChats(String chatName) {
        chatVBox.getChildren().clear();
        for (Chat chat : chats) {
            if (chat.getName().equals(chatName)) {
                currentChat = chat;
                for (Message message : chat.getAllMessages())
                    showMessage(message);
            }
        }
    }

    public void sendMessage() {
        String content = messageField.getText();
        if (content.equals("") || content.matches("^\\s+$"))
            return;
        Message message = new Message(LoginController.getLoggedUser().getUsername(), new Date(), content);
        currentChat.sendMessage(message);
        showMessage(message);
        messageField.setText("");
        scroll.vvalueProperty().bind(chatVBox.heightProperty());
        updateSavedMessages();
    }

    private void updateSavedMessages() {
        try {
            FileOutputStream fileStream = new FileOutputStream("dataBase/chats.dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(chats);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving chats : ");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private void showMessage(Message message) {
        Text title = new Text(message.getSender());
        Text msg = new Text(message.getContent());
        ImageView avatar = new ImageView(new Image(LoginController.getLoggedUser().getAvatar()));
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);

        Label senderName = getLabel(title, 30);
        Label messageLabel = getLabel(msg, 20);

        VBox totalMessage = new VBox(senderName, messageLabel);
        totalMessage.setOnMouseClicked(mouseEvent -> {
            editMessage(message);
        });
        chatVBox.getChildren().add(new HBox(avatar, totalMessage));
    }

    private void editMessage(Message message) {
        message.setContent("This message was deleted.");
        loadChats(currentChat.getName());
        updateSavedMessages();
    }


    private Label getLabel(Text title, int fontSize) {
        Label label2 = new Label();
        label2.setStyle("-fx-text-fill: white");
        label2.setBackground(new Background(new BackgroundFill(Color.rgb(24, 37, 51), null, null)));
        label2.setText(title.getText());
        label2.setWrapText(true);
        label2.setMaxWidth(800);
        label2.setPadding(new Insets(10));
        label2.setFont(new Font("Arial", fontSize));
        return label2;
    }

    public void newChat(ActionEvent event) {
        messageField.setDisable(true);
        sendButton.setDisable(true);
        mainSection.setAlignment(Pos.CENTER);
        mainSection.getChildren().clear();
        Text text = new Text("Enter a username to start chatting:");
        text.setStyle("-fx-font-size: 30;-fx-fill: white");
        TextField field = new TextField();
        field.setMaxWidth(600);
        field.setPromptText("Enter a username");
        Text error = new Text();
        error.setStyle("-fx-font-size: 20;-fx-fill: white;");
        Button startChat = new Button("Start messaging");
        startChat.setOnAction(event1 -> startPrivateChat(field, error));
        Hyperlink link = new Hyperlink("You can create a chat room with multiple users.");
        mainSection.getChildren().addAll(text, field, error, startChat, link);

        field.setOnKeyReleased(event2 -> {
            error.setText("");
            if (event2.getCode().toString().equals("ENTER"))
                startPrivateChat(field, error);
        });

        field.requestFocus();
    }

    private void startPrivateChat(TextField usernameField, Text error) {
        if (usernameField.getText().equals(""))
            error.setText("Enter a username.");
            //TODO: username validation = error.setText("No User exists with this username.");
        else {
            Chat chat = new Chat(usernameField.getText());
            chat.addUser(LoginController.getLoggedUser());
            //TODO: add the second user to chat: chat.addUser( ? );
            chats.add(chat);
            showUsersBar();
            startChatting(chat.getName());
        }

    }


    public void settings(ActionEvent event) {
    }

    public void back() {
        StageController.sceneChanger("mainMenu.fxml");
    }


    public void checkEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER"))
            sendMessage();
    }
}
