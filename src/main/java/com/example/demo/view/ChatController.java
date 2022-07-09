package com.example.demo.view;

import com.example.demo.controller.LoginController;
import com.example.demo.model.Chat;
import com.example.demo.model.Message;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.text.DateFormatSymbols;
import java.util.*;

public class ChatController {

    @FXML
    private HBox commandBar;
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

        //command bar
        commandBar.getChildren().clear();
        Button deleteForMe = new Button("Delete for me");
        Button deleteForAll = new Button("Delete for all");
        Button edit = new Button("Edit");
        deleteForAll.setVisible(false);
        deleteForMe.setVisible(false);
        edit.setVisible(false);
        commandBar.getChildren().addAll(deleteForAll, deleteForMe, edit);
        deleteForAll.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Delete selected Message(s)?", ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Delete for all");
            alert.initOwner(StageController.getStage());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType.equals(ButtonType.OK)) {
                    for (Message message : currentChat.getAllMessages())
                        if (message.isSelected()) {
                            message.setContent("This message was deleted.");
                            message.toggleSelected();
                        }
                    updateSavedMessages();
                    loadChats(currentChat.getName());
                }
                commandBarShowHide();
            });
        });

        deleteForMe.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Delete selected Message(s)?", ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Delete for me");
            alert.initOwner(StageController.getStage());
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType.equals(ButtonType.OK)) {
                    currentChat.getAllMessages().removeIf(Message::isSelected);
                    updateSavedMessages();
                    loadChats(currentChat.getName());
                }
                commandBarShowHide();
            });
        });

        edit.setOnAction(event -> {
            Message selectedMessage = null;
            int counter = 0;
            for (Message message : currentChat.getAllMessages())
                if (message.isSelected()) {
                    selectedMessage = message;
                    counter++;
                    if (counter == 2)
                        break;
                }
            if (counter == 1) {
                messageField.setText(selectedMessage.getContent());
                sendButton.setText("Edit");
                Message finalSelectedMessage = selectedMessage;
                sendButton.setOnAction(event1 -> editMessage(finalSelectedMessage));
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Cannot edit multiple messages at once.", ButtonType.OK);
                alert.initOwner(StageController.getStage());
                alert.show();
            }
        });


        //load chats
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

    private void editMessage(Message message) {
        message.setContent(messageField.getText());
        loadChats(currentChat.getName());
        updateSavedMessages();
        message.setSelected(false);
        messageField.setText("");
        sendButton.setText("Send");
        sendButton.setOnAction(event1 -> sendMessage());
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

        //unselect all
        for (Chat chat : chats)
            if (chat.getName().equals(chatName))
                for (Message message : chat.getAllMessages())
                    message.setSelected(false);

        loadChats(chatName);

        commandBarShowHide();
        sendButton.setText("Send");
        sendButton.setOnAction(event1 -> sendMessage());
        messageField.setText("");

        scroll.vvalueProperty().bind(chatVBox.heightProperty());
        messageField.requestFocus();
    }

    private void loadChats(String chatName) {
        chatVBox.getChildren().clear();
        for (Chat chat : chats) {
            if (chat.getName().equals(chatName)) {
                currentChat = chat;
                for (Message message : chat.getAllMessages()) {
                    showMessage(message);
                }
                break;
            }
        }
    }

    public void sendMessage() {
        String content = messageField.getText();
        if (content.equals("") || content.matches("^\\s+$"))
            return;
        Message message = new Message(LoginController.getLoggedUser().getUsername(), content);
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
        Calendar calendar = message.getCalendar();
        String date = calendar.getTime().toString();// + calendar.get(Calendar.DAY_OF_MONTH) + " " + new DateFormatSymbols().getShortMonths()[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        date = date.substring(0, date.length() - 10);
        Text title = new Text(message.getSender());
        Text msg = new Text(message.getContent() + "\n_________________\n" + date);
        ImageView avatar = new ImageView(new Image(LoginController.getLoggedUser().getAvatar()));
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);

        Label senderName = getLabel(title, 30, message);
        Label messageLabel = getLabel(msg, 20, message);

        VBox totalMessage = new VBox(senderName, messageLabel);
        totalMessage.setOnMouseClicked(mouseEvent -> {
            message.toggleSelected();
            updateSavedMessages();
            loadChats(currentChat.getName());
            commandBarShowHide();
        });
        chatVBox.getChildren().add(new HBox(avatar, totalMessage));
    }

    private void commandBarShowHide() {
        boolean weHaveSelectedMessages = false;
        for (Message message : currentChat.getAllMessages()) {
            if (message.isSelected()) {
                weHaveSelectedMessages = true;
                break;
            }
        }
        for (int i = 0; i < 3; i++)
            commandBar.getChildren().get(i).setVisible(weHaveSelectedMessages);
    }

    private Label getLabel(Text title, int fontSize, Message message) {
        Label label = new Label();
        label.setStyle("-fx-text-fill: white");
        if (message.isSelected())
            label.setBackground(new Background(new BackgroundFill(Color.rgb(46, 112, 164), null, null)));
        else
            label.setBackground(new Background(new BackgroundFill(Color.rgb(24, 37, 51), null, null)));
        label.setText(title.getText());
        label.setWrapText(true);
        label.setMaxWidth(800);
        label.setPadding(new Insets(10));
        label.setFont(new Font("Arial", fontSize));
        return label;
    }

    public void newChat() {
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
        link.setOnAction(event1 -> createRoom());
        mainSection.getChildren().addAll(text, field, error, startChat, link);

        field.setOnKeyReleased(event2 -> {
            error.setText("");
            if (event2.getCode().toString().equals("ENTER"))
                startPrivateChat(field, error);
        });

        field.requestFocus();
    }

    private void createRoom() {
        mainSection.getChildren().clear();
        Text title1 = new Text("Enter the room name:");
        title1.setStyle("-fx-font-size: 30;-fx-fill: white");

        TextField nameField = new TextField();
        nameField.setMaxWidth(600);
        nameField.setPromptText("Enter a name");

        Text title2 = new Text("Who would you like to add?");
        title2.setStyle("-fx-font-size: 30;-fx-fill: white");

        TextField userField = new TextField();
        userField.setMaxWidth(600);
        userField.setPromptText("Enter a username");

        Set<String> usersSet = new HashSet<>();
        Button add = new Button("Add user");
        Text users = new Text("Added users: ");
        users.setStyle("-fx-font-size: 15;-fx-fill: white;");

        Text error = new Text();
        error.setStyle("-fx-font-size: 20;-fx-fill: white;");

        add.setOnAction(event -> {
            if (userField.getText().isEmpty())
                error.setText("Enter a username.");
            else {
                if (usersSet.add(userField.getText()))
                    users.setText(users.getText() + userField.getText() + ", ");
                userField.setText("");
            }
        });

        Button startChat = new Button("Create room");
        startChat.setOnAction(event1 -> startRoomChat(nameField, usersSet, error));
        mainSection.getChildren().addAll(title1, nameField, title2, userField, error, add, startChat, users);

        nameField.setOnKeyReleased(event2 -> {
            error.setText("");
            if (event2.getCode().toString().equals("ENTER"))
                startRoomChat(nameField, usersSet, error);
        });

        userField.setOnKeyReleased(event2 -> {
            error.setText("");
            if (event2.getCode().toString().equals("ENTER")) {
                if (userField.getText().isEmpty())
                    error.setText("Enter a username.");
                else {
                    if (usersSet.add(userField.getText()))
                        users.setText(users.getText() + userField.getText() + ", ");
                    userField.setText("");
                }
            }
        });

        nameField.requestFocus();
    }

    private void startRoomChat(TextField nameField, Set<String> usersSet, Text error) {
        if (nameField.getText().equals(""))
            error.setText("Enter a name for the room.");
        else if (usersSet.isEmpty())
            error.setText("Add at list one user to the room.");
            //TODO: username validation = error.setText("No User exists with this username.");
        else {
            Chat chat = new Chat("room: " + nameField.getText());
            chat.addUser(LoginController.getLoggedUser());
            //TODO: add the users to chat: chat.addUser( ? );
            chats.add(chat);
            showUsersBar();
            updateSavedMessages();
            startChatting(chat.getName());
        }
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
            updateSavedMessages();
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
            if (sendButton.getText().equals("Send"))
                sendMessage();
    }
}
