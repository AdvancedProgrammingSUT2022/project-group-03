package com.example.demo.view.model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Cheat {

    public Cheat(Pane root, HBox cheatBar) {
        init(root, cheatBar);
    }

    private void init(Pane root, HBox cheatBar) {
        root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
            final KeyCombination combination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

            @Override
            public void handle(KeyEvent keyEvent) {
                if (combination.match(keyEvent)) {
                    //check if a cheat panel opened before
                    if (!cheatBar.getChildren().isEmpty()) {
                        cheatBar.getChildren().clear();
                        return;
                    }
                    TextField cheatCommand = new TextField();
                    cheatCommand.setStyle("-fx-font-weight: bold; pref-width: 500; -fx-font-size: 17;");
                    //an eventHandler for pressing Enter in TextField:
                    cheatCommand.setOnKeyReleased(keyEvent1 -> {
                        if (keyEvent1.getCode().getName().equals("Enter")) {
                            runCheatCommand(cheatBar, cheatCommand);
                        }
                    });
                    Button button = new Button("Run");
                    button.setOnAction(actionEvent -> runCheatCommand(cheatBar, cheatCommand));

                    HBox cheatPanel = new HBox(cheatCommand, button);
                    cheatPanel.setStyle("-fx-spacing: 5; -fx-background-color: white; -fx-padding: 10; -fx-opacity: 0.9;");
                    cheatBar.getChildren().add(cheatPanel);

                    cheatCommand.requestFocus();
                    keyEvent.consume();
                }
            }
        });
    }

    private void runCheatCommand(Pane upperMapPane, TextField cheatCommand) {
        System.out.println(cheatCommand.getText());
        upperMapPane.getChildren().clear();
    }

}
