package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.model.Chat;
import com.example.demo.model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
    private VBox chatSection;
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
    }

    public void startChatting(String chatName) {
        mainSection.getChildren().clear();
        messageField.setDisable(false);
        sendButton.setDisable(false);
        mainSection.getChildren().clear();
        chatSection = new VBox();
        chatSection.setAlignment(Pos.CENTER);
        chatSection.setSpacing(15);
        scroll = new ScrollPane(chatSection);
        mainSection.getChildren().add(scroll);

        //load chats:
        for (Chat chat : chats) {
            if (chat.getName().equals(chatName))
                for (Message message : chat.getAllMessages())
                    showMessage(message);
        }
    }

    public void gotoPublicChat(ActionEvent event) {
        currentChat = chats.get(0);
        startChatting("publicChat");
    }

    public void sendMessage() {
        String content = messageField.getText();
        if (content.equals("") || content.matches("^\\s+$"))
            return;
        Message message = new Message(LoginController.getLoggedUser().getUsername(), new Date(), content);
        currentChat.sendMessage(message);
        showMessage(message);
        messageField.setText("");
        scroll.vvalueProperty().bind(chatSection.heightProperty());
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
        Text msg = new Text(message.getContent());
        msg.setStyle("-fx-font-size: 20");
        msg.setWrappingWidth(750);
        Label label = new Label();
        label.setBackground(new Background(new BackgroundFill(Color.rgb(212, 250, 180), null, null)));
        label.setText(msg.getText());
        label.setWrapText(true);
        label.setPrefWidth(800);
        label.setPadding(new Insets(10));
        label.setFont(new Font("Arial", 25));
        chatSection.getChildren().add(label);
    }

    public void newChat(ActionEvent event) {
        messageField.setDisable(true);
        sendButton.setDisable(true);
        mainSection.setAlignment(Pos.CENTER);
        mainSection.getChildren().clear();
        Text text = new Text("Enter a username to start chatting:");
        text.setStyle("-fx-font-size: 30");
        TextField field = new TextField();
        field.setMaxWidth(600);
        field.setPromptText("Enter a username");
        Text error = new Text();
        error.setStyle("-fx-font-size: 20");
        Button startChat = new Button("Start messaging");
        startChat.setOnAction(event1 -> startPrivateChat(field, error));
        Hyperlink link = new Hyperlink("You can create a chat room with multiple users.");
        mainSection.getChildren().addAll(text, field, error, startChat, link);

        field.setOnKeyReleased(event2 -> {
            error.setText("");
            if (event2.getCode().toString().equals("ENTER"))
                startPrivateChat(field, error);
        });

    }

    private void startPrivateChat(TextField usernameField, Text error) {
        if (usernameField.getText().equals(""))
            error.setText("Enter a username.");
        else
            //TODO: username validation
            error.setText("No User exists with this username.");
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
