package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Civilization;
import com.example.demo.model.resources.ResourcesTypes;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Diplomacy implements Initializable {
    public Pane upperMapPane;
    public ImageView background;
    private Civilization opponent = null;
    private ScrollPane knownCivilizationsScrollPane = new ScrollPane();
    private ScrollPane myResourcesScrollPane = new ScrollPane();
    private ScrollPane myOffersScrollPane = new ScrollPane();
    private ScrollPane opponentsOffersScrollPane = new ScrollPane();
    private ScrollPane opponentsResourcesScrollPane = new ScrollPane();
    private ScrollPane requestsScrollPane = new ScrollPane();
    ArrayList<Pair<ResourcesTypes, Integer>> myResources = new ArrayList<>();
    ArrayList<Pair<ResourcesTypes, Integer>> opponentsResources = new ArrayList<>();
    ArrayList<Pair<ResourcesTypes, Integer>> myOffers = new ArrayList<>();
    ArrayList<Pair<ResourcesTypes, Integer>> opponentsOffers = new ArrayList<>();
    private Node[] stayingButtons = new Node[20];
    private Text condition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Panels.setBackground(background, "treeNoLine");
        Platform.runLater(this::runLater);
    }

    private void runLater() {

        stayingButtons[0] = new Button("Discussion");
        upperMapPane.getChildren().add(stayingButtons[0]);
        stayingButtons[0].setLayoutX(StageController.getStage().getWidth() * 0.95 - 10);
        stayingButtons[0].setLayoutY(StageController.getStage().getHeight() * 0.95 - 10);
        stayingButtons[0].setOnMouseClicked(event -> {
            ChatController.setInGame(true);
            StageController.sceneChanger("chat.fxml");
        });

        //////


        for (Civilization civilization : GameController.getCivilizations()) {
            if (civilization != GameController.getCivilizations().get(GameController.getPlayerTurn()))
                GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().add(new Pair<>(civilization, 0));
        }
        for (int i = 0; i < ResourcesTypes.VALUES.size(); i++) {
            GameController.getCivilizations().get(GameController.getPlayerTurn()).getResourcesAmount().put(ResourcesTypes.VALUES.get(i), i + 5);
        }



        ArrayList<Pair<ResourcesTypes, Integer>> finalResources = new ArrayList<>();
        GameController.getCurrentCivilization().getResourcesAmount().forEach((k, v) -> {
            int value = v;
            if (value > 0)
                finalResources.add(new Pair<>(k, value));

        });


        GameController.getCurrentCivilization().getTradeRequests().add(new TradeRequest(finalResources,opponentsOffers,GameController.getCivilizations().get(1),GameController.getCurrentCivilization()));
        GameController.getCurrentCivilization().getTradeRequests().add(new TradeRequest(finalResources,opponentsOffers,GameController.getCivilizations().get(1),GameController.getCurrentCivilization()));
        GameController.getCurrentCivilization().getFriendshipRequests().add(GameController.getCivilizations().get(1));
        //////
        stayingButtons[1] = Panels.setBackButton(upperMapPane, StageController.getStage().getWidth() - 40);
        AnchorPane knownCivilizationsAnchorPane = new AnchorPane();
        for (int i = 0; i < GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().size(); i++) {
            Text text = Panels.addText(GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().get(i).getKey().getUser().getNickname(),
                    10, (i) * 20 + 5, 17, null, null);
            Label label = Panels.textToLabel(text, knownCivilizationsAnchorPane);
            label.setCursor(Cursor.HAND);
            int finalI = i;
            label.setOnMouseClicked(event -> {
                opponent = GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().get(finalI).getKey();
                setOpponentOn();
            });
        }

        stayingButtons[2] = Panels.addText("Requests: ", 10, 25, 25, Color.WHITE, upperMapPane);

        ScrollPane requestsScrollPane = new ScrollPane();
        stayingButtons[3] = requestsScrollPane;
        updateRequests();
        requestsScrollPane.setLayoutX(10);
        requestsScrollPane.setLayoutY(70);
        requestsScrollPane.setPrefWidth(300);
        requestsScrollPane.setPrefHeight(300);
        upperMapPane.getChildren().add(requestsScrollPane);

        knownCivilizationsScrollPane.setContent(knownCivilizationsAnchorPane);
        knownCivilizationsScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.90);
        knownCivilizationsScrollPane.setLayoutY(StageController.getStage().getHeight() * 2 / 7);
        knownCivilizationsScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        knownCivilizationsScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.09);
        upperMapPane.getChildren().add(knownCivilizationsScrollPane);


        myResourcesScrollPane = new ScrollPane();
        myResourcesScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.21);
        myResourcesScrollPane.setLayoutY(StageController.getStage().getHeight() * 2.2 / 7);
        myResourcesScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        myResourcesScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);

        myOffersScrollPane = new ScrollPane();
        myOffersScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.30);
        myOffersScrollPane.setLayoutY(StageController.getStage().getHeight() * 2.2 / 7);
        myOffersScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        myOffersScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);

        opponentsOffersScrollPane = new ScrollPane();
        opponentsOffersScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.65);
        opponentsOffersScrollPane.setLayoutY(StageController.getStage().getHeight() * 2.2 / 7);
        opponentsOffersScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        opponentsOffersScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);

        opponentsResourcesScrollPane = new ScrollPane();
        opponentsResourcesScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.74);
        opponentsResourcesScrollPane.setLayoutY(StageController.getStage().getHeight() * 2.2 / 7);
        opponentsResourcesScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.09);
        opponentsResourcesScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);

//        Panels.addText("You",StageController.getStage().getWidth()*0.73, StageController.getStage().getHeight()*2/7,25,upperMapPane);
    }


    private void updateRequests()
    {
        AnchorPane requestsAnchorPane = new AnchorPane();
        int lastI=0;
        for (int i = 0; i < GameController.getCurrentCivilization().getTradeRequests().size(); i++) {
            ScrollPane thisRequestScrollPane = new ScrollPane();
            AnchorPane thisRequestAnchorPane = new AnchorPane();
            thisRequestScrollPane.setContent(thisRequestAnchorPane);

            requestsAnchorPane.getChildren().add(thisRequestScrollPane);

            TradeRequest tradeReq = GameController.getCurrentCivilization().getTradeRequests().get(i);
            Panels.addText(tradeReq.getFrom().getUser().getNickname() + " Offers: ",0,
                    23 + StageController.getStage().getHeight()*0.22*i,
                    15,null,requestsAnchorPane);
            thisRequestScrollPane.setLayoutY(i*StageController.getStage().getHeight() * 0.22 + 30);
            thisRequestScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.05);
            thisRequestScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);
            for (int j = 0; j <GameController.getCurrentCivilization().getTradeRequests().get(i).getTheirOffers().size() ; j++) {
                addImageAndTextOfOffer(j,GameController.getCurrentCivilization().getTradeRequests().get(i).getTheirOffers().get(j),thisRequestAnchorPane,false);
            }



            thisRequestScrollPane = new ScrollPane();
            thisRequestAnchorPane = new AnchorPane();
            thisRequestScrollPane.setContent(thisRequestAnchorPane);

            requestsAnchorPane.getChildren().add(thisRequestScrollPane);

            Panels.addText("Your Offers: ",
                    StageController.getStage().getWidth() * 0.05,
                    23 + StageController.getStage().getHeight()*0.22*i,
                    15,null,requestsAnchorPane);


            thisRequestScrollPane.setLayoutY(i*StageController.getStage().getHeight() * 0.22 + 30);
            thisRequestScrollPane.setLayoutX(StageController.getStage().getWidth() * 0.05);
            thisRequestScrollPane.setPrefWidth(StageController.getStage().getWidth() * 0.05);
            thisRequestScrollPane.setPrefHeight(StageController.getStage().getHeight() * 0.20);
            for (int j = 0; j <GameController.getCurrentCivilization().getTradeRequests().get(i).getYourOffers().size() ; j++)
                addImageAndTextOfOffer(j,GameController.getCurrentCivilization().getTradeRequests().get(i).getYourOffers().get(j),thisRequestAnchorPane,false);

            Button accept = Panels.addButton("Accept",StageController.getStage().getWidth() * 0.105,
                    StageController.getStage().getHeight() * 0.23 *i +StageController.getStage().getHeight() * 0.05, 100, 45,requestsAnchorPane);
            int finalI = i;
            accept.setOnMouseClicked(event -> {
                TradeRequest tradeRequest = GameController.getCurrentCivilization().getTradeRequests().get(finalI);
                for (int i1 = tradeRequest.getTheirOffers().size()-1; i1 >=0; i1--) {
                    GameController.getCurrentCivilization().addResources(tradeRequest.getTheirOffers().get(i1).getKey(),
                            tradeRequest.getTheirOffers().get(i1).getValue());
                    tradeRequest.getFrom().removeResource(tradeRequest.getTheirOffers().get(i1).getKey(),
                            tradeRequest.getTheirOffers().get(i1).getValue());

                }
                for (int i1 = tradeRequest.getYourOffers().size()-1; i1 >=0; i1--) {
                    tradeRequest.getFrom().addResources(tradeRequest.getTheirOffers().get(i1).getKey(),
                            tradeRequest.getTheirOffers().get(i1).getValue());
                    GameController.getCurrentCivilization().removeResource(tradeRequest.getTheirOffers().get(i1).getKey(),
                            tradeRequest.getTheirOffers().get(i1).getValue());
                }
                GameController.getCurrentCivilization().getTradeRequests().remove(finalI);
                updateRequests();
            });

            Button decline = Panels.addButton("Decline",StageController.getStage().getWidth() * 0.105,
                    StageController.getStage().getHeight() * 0.23 *i +StageController.getStage().getHeight() * 0.15, 100, 45,requestsAnchorPane);
            decline.setOnMouseClicked(event -> {
                GameController.getCurrentCivilization().getTradeRequests().remove(finalI);
                updateRequests();
            });
            lastI =i;
        }

        for (int i = 0; i < GameController.getCurrentCivilization().getFriendshipRequests().size(); i++) {
            Panels.addText("Peace Request: " +
                            GameController.getCurrentCivilization().getFriendshipRequests().get(i).getUser().getNickname(),0,
                    StageController.getStage().getHeight() * 0.23 *(lastI+1) + StageController.getStage().getHeight()*0.05*i + 35,
                    15,null,requestsAnchorPane);




            Button accept = Panels.addButton("Accept",StageController.getStage().getWidth() * 0.105,
                    StageController.getStage().getHeight() * 0.23 *(lastI+1) + StageController.getStage().getHeight()*0.05*i, 100, 30,requestsAnchorPane);
            int finalI = i;

            accept.setOnMouseClicked(event ->
            {
                if(GameController.getCurrentCivilization().knownCivilizationsContains(GameController.getCurrentCivilization().getFriendshipRequests().get(finalI))){
                    for (Pair<Civilization, Integer> knownCivilization : GameController.getCurrentCivilization().getKnownCivilizations()) {
                        if(knownCivilization.getKey()==GameController.getCurrentCivilization().getFriendshipRequests().get(finalI))
                        {
                            GameController.getCurrentCivilization().getKnownCivilizations().remove(knownCivilization);
                            break;
                        }
                    }
                }
                GameController.getCurrentCivilization().getKnownCivilizations()
                        .add(new Pair<>(GameController.getCurrentCivilization().getFriendshipRequests().get(finalI),1));
                GameController.getCurrentCivilization().getFriendshipRequests().remove(finalI);
                updateRequests();
            });

            Button decline = Panels.addButton("Decline",StageController.getStage().getWidth() * 0.105,
                    StageController.getStage().getHeight() * 0.23 *(lastI+1) + StageController.getStage().getHeight()*0.05*i +
                            StageController.getStage().getHeight()*0.03, 100, 30,requestsAnchorPane);

            decline.setOnMouseClicked(event -> {
                GameController.getCurrentCivilization().getFriendshipRequests().remove(finalI);
                updateRequests();
            });
        }
        ((ScrollPane)stayingButtons[3]).setContent(requestsAnchorPane);
    }
    private boolean shouldButtonStay(Node node) {
        if (node == knownCivilizationsScrollPane ||
                node == background)
            return false;
        for (Node stayingButton : stayingButtons) {
            if (node == stayingButton)
                return false;
        }
        return true;
    }

    private String getConditionString(int value) {
        String string = "neutral";
        if (value == 1)
            string = "peace";
        else if (value == -1)
            string = "war";
        return string;
    }

    private void setOpponentOn() {
        if (opponent != null) {
            upperMapPane.getChildren().removeIf(this::shouldButtonStay);
            Panels.addText(opponent.getUser().getNickname(),
                    StageController.getStage().getWidth() * 0.73,
                    StageController.getStage().getHeight() * 2 / 7,
                    25, javafx.scene.paint.Color.WHITE, upperMapPane);
            int value = -2;
            for (Pair<Civilization, Integer> knownCivilization : GameController.getCurrentCivilization().getKnownCivilizations()) {
                if (knownCivilization.getKey() == opponent) {
                    value = knownCivilization.getValue();
                    break;
                }
            }

            String string = "neutral";
            if (value == 1)
                string = "peace";
            else if (value == -1)
                string = "war";
            condition = Panels.addText(string, StageController.getStage().getWidth() * 0.48,
                    StageController.getStage().getHeight() * 2 / 7,
                    25, Color.WHITE, upperMapPane);
            Panels.addText("You", StageController.getStage().getWidth() * 0.23,
                    StageController.getStage().getHeight() * 2 / 7,
                    25, javafx.scene.paint.Color.WHITE, upperMapPane);
            Button button = Panels.addButton("Declare war", StageController.getStage().getWidth() * 0.47,
                    StageController.getStage().getHeight() * 2.1 / 7,
                    130, 20, upperMapPane);
            button.setOnMouseClicked(event -> {
                for (Pair<Civilization, Integer> knownCivilization : GameController.getCurrentCivilization().getKnownCivilizations()) {
                    if (knownCivilization.getKey() == opponent) {
                        if (knownCivilization.getValue() == -1) {
                            StageController.errorMaker("Your enemy twice, eh?", "You guys are already enemies, dumbA-", Alert.AlertType.ERROR);
                        } else {
                            GameController.getCurrentCivilization().getKnownCivilizations().remove(knownCivilization);
                            GameController.getCurrentCivilization().getKnownCivilizations()
                                    .add(new Pair<>(opponent, -1));
                            StageController.errorMaker("Done", "You guys are enemies now", Alert.AlertType.INFORMATION);
                            condition.setText(getConditionString(-1));
                        }
                        break;
                    }


                }
            });
            button = Panels.addButton("Send Piss Request", StageController.getStage().getWidth() * 0.47,
                    StageController.getStage().getHeight() * 2.4 / 7,
                    130, 20, upperMapPane);
            button.setOnMouseClicked(event -> {
                opponent.getFriendshipRequests().add(GameController.getCurrentCivilization());
            });

            button = Panels.addButton("Send Trade Request", StageController.getStage().getWidth() * 0.47,
                    StageController.getStage().getHeight() * 2.7 / 7,
                    130, 20, upperMapPane);
            button.setOnMouseClicked(event -> {
                opponent.getTradeRequests().add(new TradeRequest(myOffers, opponentsOffers, GameController.getCurrentCivilization(), opponent));
            });
            updateMyResources(true);
            updateMyResources(false);
        }
    }

    private void updateMyOffers(boolean isMine) {
        ScrollPane scrollPane = opponentsOffersScrollPane;
        ArrayList<Pair<ResourcesTypes, Integer>> offers = opponentsOffers;
        if (isMine) {
            scrollPane = myOffersScrollPane;
            offers = myOffers;
        }

        AnchorPane anchorPane = new AnchorPane();
        scrollPane.setContent(anchorPane);
        for (int i = 0; i < offers.size(); i++) {
            Label label = addImageAndTextOfOffer(i, offers.get(i), anchorPane,true);
            int finalI = i;
            ArrayList<Pair<ResourcesTypes, Integer>> finalOffers = offers;
            label.setOnMouseClicked(event -> {
                int value = finalOffers.get(finalI).getValue() - 1;
                ResourcesTypes key = finalOffers.get(finalI).getKey();
                finalOffers.remove(finalI);
                if (value > 0)
                    finalOffers.add(new Pair<>(key, value));
                updateMyResources(isMine);
                updateMyOffers(isMine);
            });
        }
    }

    private void updateMyResources(boolean isMine) {
        ScrollPane offersScrollPane = opponentsOffersScrollPane;
        ScrollPane resourcesScrollPane = opponentsResourcesScrollPane;
        ArrayList<Pair<ResourcesTypes, Integer>> offers = opponentsOffers;
        ArrayList<Pair<ResourcesTypes, Integer>> resources = opponentsResources;
        if (isMine) {
            offersScrollPane = myOffersScrollPane;
            resourcesScrollPane = myResourcesScrollPane;
            offers = myOffers;
            resources = myResources;
        }

        resources.clear();
        ArrayList<Pair<ResourcesTypes, Integer>> finalResources = resources;
        ArrayList<Pair<ResourcesTypes, Integer>> finalOffers1 = offers;
        Civilization civilization = opponent;
        if (isMine)
            civilization = GameController.getCurrentCivilization();
        civilization.getResourcesAmount().forEach((k, v) -> {
            int value = v;
            for (Pair<ResourcesTypes, Integer> myOffer : finalOffers1) {
                if (myOffer.getKey() == k) {
                    value -= myOffer.getValue();
                    break;
                }
            }
            if (value > 0)
                finalResources.add(new Pair<>(k, value));

        });
        AnchorPane myResourcesAnchorPane = new AnchorPane();
        resourcesScrollPane.setContent(myResourcesAnchorPane);
        if (!upperMapPane.getChildren().contains(resourcesScrollPane))
            upperMapPane.getChildren().add(resourcesScrollPane);
        if (!upperMapPane.getChildren().contains(offersScrollPane))
            upperMapPane.getChildren().add(offersScrollPane);

        for (int i = 0; i < resources.size(); i++) {
            Label label = addImageAndTextOfOffer(i, resources.get(i), myResourcesAnchorPane,true);
            int finalI = i;
            ArrayList<Pair<ResourcesTypes, Integer>> finalResources1 = resources;
            ArrayList<Pair<ResourcesTypes, Integer>> finalOffers = offers;
            label.setOnMouseClicked(event -> {
                int value = 0;
                for (int i1 = 0; i1 < finalOffers.size(); i1++) {
                    if (finalOffers.get(i1).getKey() == finalResources1.get(finalI).getKey()) {
                        value = finalOffers.get(i1).getValue();
                        finalOffers.remove(i1);
                        break;
                    }
                }
                value++;
                finalOffers.add(new Pair<>(finalResources1.get(finalI).getKey(), value));
                updateMyOffers(isMine);
                updateMyResources(isMine);
            });
        }
    }

    private Label addImageAndTextOfOffer(int i, Pair<ResourcesTypes, Integer> pair, Pane pane, boolean handCursor) {
        ImageView imageView = new ImageView(ImageLoader.get(pair.getKey().toString()));
        imageView.setX(5);
        imageView.setY(25 * i + 5);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        Label label = Panels.imageViewToLabel(imageView, pane);
        label.setLayoutX(5);
        label.setLayoutY(25 * i + 5);
        label.setTooltip(new Tooltip(pair.getKey().toString()));
        if (handCursor) {
            label.setCursor(Cursor.HAND);
            imageView.setCursor(Cursor.HAND);
        }
        if (pair.getValue() != 0) {
            Panels.addText(": " + pair.getValue(),
                    25, (i + 1) * 25 - 3, 17, null, pane);
        }
        return label;
    }
}
