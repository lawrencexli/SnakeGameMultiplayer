package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MVCSnakeView extends Application
{
    private Pane root;

    public Pane getRoot()
    {
        return root;
    }

    protected GridPane startMenu;
    private Button clientButton;
    private Button hostButton;
    private TextField hostText;
    private TextField portText;
    private TextField hostPortField;
    private TextField numOfPlayerField;
    private TextField setHeightField;
    private TextField setWidthField;

    public TextField getDisplayMessage()
    {
        return displayMessage;
    }

    private TextField displayMessage;
    private MVCSnakeController controller;

    public void init()
    {
        root = new Pane();
        root.setPrefSize(800, 600);
        controller = new MVCSnakeController(this);

        this.startMenu = new GridPane();
        this.startMenu.setPrefSize(400,300);
        //this.startMenu.setGridLinesVisible(true);
        this.startMenu.setAlignment(Pos.CENTER);
        this.startMenu.setHgap(10);
        this.startMenu.setVgap(10);
        this.hostText = new TextField();
        this.portText = new TextField();
        this.hostPortField = new TextField();

        this.numOfPlayerField = new TextField();
        this.numOfPlayerField.setText("1");
        this.setHeightField = new TextField();
        this.setHeightField.setText("800");
        this.setWidthField = new TextField();
        this.setWidthField.setText("600");

        this.clientButton = new Button();
        this.clientButton.setText("JOIN");
        this.hostButton = new Button();
        this.hostButton.setText("HOST");

        this.displayMessage = new TextField();
        this.displayMessage.setMouseTransparent(true);
        this.displayMessage.setStyle("-fx-background-color: #c8c8c8; -fx-text-fill: red");
        this.displayMessage.setAlignment(Pos.CENTER);

        this.startMenu.add(new Label("JOIN GAME"), 2, 1,3,1);
        this.startMenu.add(new Label("HOST:"), 1,2);
        this.startMenu.add(this.hostText, 2,2);

        this.startMenu.add(new Label("PORT:"), 1,3);
        this.startMenu.add(this.portText,2,3);


        this.startMenu.add(this.clientButton,3,2,1,2);

        this.startMenu.add(new Label("HOST GAME"), 2, 6,3,1);
        this.startMenu.add(new Label("PORT:"), 1,7);
        this.startMenu.add(this.hostPortField, 2,7);
        this.startMenu.add(this.hostButton,3,7,1,4);
        this.startMenu.add(new Label("PLAYERS:"), 1,8);
        this.startMenu.add(this.numOfPlayerField,2,8);
        this.startMenu.add(new Label("PANE HEIGHT:"), 1,9);
        this.startMenu.add(this.setHeightField,2,9);
        this.startMenu.add(new Label("PANE WIDTH:"), 1,10);
        this.startMenu.add(this.setWidthField,2,10);

        this.startMenu.add(this.displayMessage, 1, 13,4,1);


        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(800/4);
        this.startMenu.setTranslateY(600/4);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(this.root));
        if (this.controller.gameGoing)
            this.userInputButtonPress(primaryStage);
        else
        {
            this.hostButton.setOnAction((event) ->
                    this.controller.setHost(this.hostPortField.getText(),
                            this.numOfPlayerField.getText(),
                            this.setWidthField.getText(),
                            this.setHeightField.getText()));

            this.clientButton.setOnAction((event) ->
                    this.controller.setJoin(this.hostText.getText(), this.portText.getText()));
        }
        primaryStage.show();
    }

    public synchronized void updateView()
    {
        this.controller.dataWrite = true;

        int i = 0;
        ArrayList<Circle> tempOne = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
        ArrayList<Circle> tempTwos = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
        ArrayList<Circle> tempThrees = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
        ArrayList<Circle> tempFours = this.controller.getSNAKE_PARTS_POSITIONING().get(i);
        ArrayList<Circle> tempTwo = this.controller.getITEM_POSITIONING();
        ArrayList<Node> tempThree = this.controller.getTrash();

        while (!tempThree.isEmpty())
            this.root.getChildren().remove(tempThree.remove(0));

        for (Circle part : tempOne)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : tempTwos)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : tempThrees)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : tempTwo)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : tempFours)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        this.controller.dataWrite = false;
    }

    private void userInputButtonPress(Stage stage)
    {
        stage.getScene().setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.sendDirection("TURN_LEFT", true);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.sendDirection("TURN_RIGHT", true);
        });

        stage.getScene().setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.sendDirection("TURN_LEFT", false);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.sendDirection("TURN_RIGHT", false);
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
