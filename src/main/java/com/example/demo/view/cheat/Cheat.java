package com.example.demo.view.cheat;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.view.GameControllerFX;
import com.example.demo.view.SavingHandler;
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
    private final GameControllerFX controller;

    public Cheat(Pane root, HBox cheatBar, GameControllerFX gameControllerFX) {
        controller = gameControllerFX;
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
        if (cheatCommand.getText().matches("^next turn \\d+$")) {
            int turn = Integer.parseInt(cheatCommand.getText().substring(10));
            for (int i = 0; i < turn * GameController.getCivilizations().size(); i++)
                GameController.nextTurn();
        }
        else if(cheatCommand.getText().matches("^increase gold \\d+$"))
        {
            int gold = Integer.parseInt(cheatCommand.getText().substring(14));
            GameController.getCivilizations().get(GameController.getPlayerTurn()).increaseGold(gold);
        }
        controller.renderMap();
        upperMapPane.getChildren().clear();
        //save the game:
        if (SavingHandler.autoSaveIsEnabled && !SavingHandler.autoSaveAtRenderingMap)
            SavingHandler.save(false);
    }

}
