package main;

import javafx.application.Application;
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

    protected GridPane startMenu;
    private Button clientButton;
    private Button hostButton;
    private TextField hostText;
    private TextField portText;
    private TextField hostPortField;
    private Label hostPrompt;
    private Label portPrompt;
    private Label hostPortLabel;

    private MVCSnakeController controller;

    public void init()
    {
        root = new Pane();
        root.setPrefSize(800, 600);
        controller = new MVCSnakeController(this);

        this.startMenu = new GridPane();
        this.startMenu.setPrefSize(400,300);
        this.hostPrompt = new Label();
        this.hostPrompt.setText("HOST:");
        this.portPrompt = new Label();
        this.portPrompt.setText("PORT:");
        this.hostText = new TextField();
        this.portText = new TextField();
        this.hostPortField = new TextField();
        this.hostPortLabel = new Label();
        this.hostPortLabel.setText("PORT:");

        this.clientButton = new Button();
        this.clientButton.setText("JOIN");
        this.hostButton = new Button();
        this.hostButton.setText("HOST");

        this.startMenu.add(this.hostPrompt, 1,1);
        this.startMenu.add(this.hostText, 2,1);
        this.startMenu.add(this.portPrompt, 1,2);
        this.startMenu.add(this.portText,2,2);
        this.startMenu.add(this.clientButton,2,3);
        this.startMenu.add(this.hostPortLabel, 1,4);
        this.startMenu.add(this.hostPortField, 2,4);
        this.startMenu.add(this.hostButton,2,5);

        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(800/2);
        this.startMenu.setTranslateY(600/2);
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
            {
                this.controller.setHost(this.hostPortField.getText());
            });

            this.clientButton.setOnAction((event) ->
            {
                this.controller.setJoin(this.hostText.getText(), this.portText.getText());
            });
        }
        primaryStage.show();
    }

    public synchronized void updateView()
    {
        this.controller.dataWrite = true;

        int i = 0;
        ArrayList<Circle> tempOne = this.controller.getSnakeListPositions().get(i++);
        ArrayList<Circle> tempTwos = this.controller.getSnakeListPositions().get(i++);
        ArrayList<Circle> tempThrees = this.controller.getSnakeListPositions().get(i++);
        ArrayList<Circle> tempFours = this.controller.getSnakeListPositions().get(i);
        ArrayList<Circle> tempTwo = this.controller.getItemListPositions();
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
