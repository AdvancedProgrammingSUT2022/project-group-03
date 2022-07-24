package com.example.demo.view;

import com.example.demo.HelloApplication;
import com.example.demo.controller.LoginController;
import com.example.demo.controller.NetworkController;
import com.example.demo.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class ScoreboardControllerFx implements Initializable {
    public AnchorPane pane;
    public ImageView background;
    private final ObservableList<Row> rows = FXCollections.observableArrayList();
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->background.setFitWidth(StageController.getScene().getWidth()));
        Platform.runLater(() ->background.setFitHeight(StageController.getScene().getHeight()));
        StageController.getScene().getStylesheets().add(HelloApplication.getResource("style.css"));
        initBoard();
    }
    public void back(MouseEvent mouseEvent) {
        StageController.getScene().getStylesheets().remove(HelloApplication.getResource("style.css"));
        NetworkController.send("menu exit");
        StageController.sceneChanger("mainMenu.fxml");
    }
    private void initBoard(){
        ScrollPane scrollPane = new ScrollPane();
        AnchorPane anchorPane  = new AnchorPane();
        scrollPane.setContent(anchorPane);
        ArrayList<User> arrayList = getUsers();
        TableView<Row> table = new TableView();
        TableColumn<Row,String> lastOnlineCol = new TableColumn<Row,String>("Last Online");
        TableColumn<Row,String> lastWinCol = new TableColumn<Row,String>("Last Win");
        TableColumn<Row,Integer> ScoreCol = new TableColumn<Row,Integer>("Score");
        TableColumn<Row,String> nicknameCol = new TableColumn<Row,String>("Nickname");
        TableColumn<Row,ImageView> avatarCol = new TableColumn<Row,ImageView>("Avatar");
        TableColumn<Row,Integer> numberCol = new TableColumn<Row,Integer>("Rank");
        table.getColumns().addAll(numberCol,avatarCol,nicknameCol,ScoreCol,lastWinCol,lastOnlineCol);
        for (int i = 0; i < arrayList.size(); i++) {
            addData(i+1,arrayList.get(i).getIcon(),arrayList.get(i).getNickname(),arrayList.get(i).getScore(),
                    arrayList.get(i).getLastWin(),arrayList.get(i).getLastOnline(),arrayList.get(i).getUsername());
        }
        final PseudoClass lowPriorityPseudoClass = PseudoClass.getPseudoClass("priority-low");
        table.setRowFactory(new Callback<TableView<Row>, TableRow<Row>>() {
            @Override
            public TableRow<Row> call(TableView<Row> personTableView) {
                return new TableRow<Row>() {
                    @Override
                    protected void updateItem(Row row, boolean b) {
                        super.updateItem(row, b);
                        boolean lowPriority = row != null && Objects.equals(row.username, LoginController.getLoggedUser().getUsername());
                        pseudoClassStateChanged(lowPriorityPseudoClass, lowPriority);
                    }
                };
            }
        });
        table.setPrefWidth(StageController.getStage().getWidth()*3/4);
        table.setPrefHeight(StageController.getStage().getHeight()*2/3);
        table.setLayoutX(100);
        table.setLayoutY(100);
        numberCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        avatarCol.setCellValueFactory(new PropertyValueFactory<>("avatar"));
        nicknameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        ScoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        lastWinCol.setCellValueFactory(new PropertyValueFactory<>("lastWin"));
        lastOnlineCol.setCellValueFactory(new PropertyValueFactory<>("lastOnline"));
        table.setItems(rows);
        anchorPane.getChildren().add(table);
        pane.getChildren().add(table);

    }
    private ArrayList<User> getUsers(){
        String json = NetworkController.getResponse();
        ArrayList<User> list = new Gson().fromJson(json,new TypeToken<ArrayList<User>>(){}.getType());
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getScore() > o2.getScore()) return -1;
                else if (o1.getScore() < o2.getScore()) return 1;
                else {
                    int x = 0;
                    if(o1.getLastWinDate() != null && o2.getLastWinDate() != null)
                     x= o1.getLastWinDate().compareTo(o2.getLastWinDate());
                    if (x!= 0) return x;
                    else {
                        return o1.getNickname().compareTo(o2.getNickname());
                    }
                }
            }
        });
        return list;
    }
    private void addData(int number,UserIcon icon,String nickname,int score,String lastWin,String lastOnline,String username){
        Image image = AssetsController.getUserAvatarImages().get(0);
        for (int i = 0; i < UserIcon.getVALUES().size(); i++) {
            if(UserIcon.getVALUES().get(i) == icon && icon != UserIcon.CUSTOM)
                image = AssetsController.getUserAvatarImages().get(i);
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        rows.add(new Row(number,imageView,nickname,score,lastWin,lastOnline,username));
    }
    public class Row{
        private final Integer rank;
        private ImageView avatar;
        private final String nickname;
        private final String username;
        private final Integer score;
        private final String lastWin;
        private final String lastOnline;

        Row(Integer rank, ImageView avatar, String nickname, Integer score, String lastWin, String lastOnline,String username) {
            this.rank = rank;
            this.avatar = avatar;
            this.nickname = nickname;
            this.score = score;
            this.lastWin = lastWin;
            this.lastOnline = lastOnline;
            this.username = username;
        }

        public Integer getRank() {
            return rank;
        }

        public ImageView getAvatar() {
            return avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public Integer getScore() {
            return score;
        }

        public String getLastWin() {
            return lastWin;
        }

        public String getLastOnline() {
            return lastOnline;
        }

        public String getUsername() {
            return username;
        }
    }

}
