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

    protected SnakeMenu startMenu;



    private MVCSnakeController controller;

    public void init()
    {
        root = new Pane();
        root.setPrefSize(800, 600);
        controller = new MVCSnakeController(this);

        this.startMenu = new SnakeMenu();

        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(800/4);
        this.startMenu.setTranslateY(600/4);
    }

    public TextField getDisplayMessage()
    {
        return this.startMenu.getDisplayMessage();
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(this.root));
        if (this.controller.gameGoing)
            this.userInputButtonPress(primaryStage);
        else
        {
            this.startMenu.onHostButtonClick((event) ->
                    this.startMenu.hostStartAction(this.controller));

            this.startMenu.onClientButtonClick((event) ->
                    this.startMenu.joinStartAction(this.controller));
        }
        primaryStage.show();
    }

    public void updateView()
    {
        if (this.controller.gameGoing)
        {
            this.controller.dataWrite = true;

            int i = 0;
            ArrayList<Circle> tempOne = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
            ArrayList<Circle> tempTwos = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
            ArrayList<Circle> tempThrees = this.controller.getSNAKE_PARTS_POSITIONING().get(i++);
            ArrayList<Circle> tempFours = this.controller.getSNAKE_PARTS_POSITIONING().get(i);
            ArrayList<Circle> tempTwo = this.controller.getITEM_POSITIONING();
            ArrayList<Node> tempThree = this.controller.getTrash();

            while (!this.controller.getTrash().isEmpty())
                this.root.getChildren().remove(this.controller.getTrash().remove(0));

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
        else
        {
            this.startMenu.getDisplayMessage().setText(this.controller.menuMessage);
        }
    }

    private void userInputButtonPress(Stage stage)
    {
        stage.getScene().setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.leftTurn(true);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.leftTurn(true);
        });

        stage.getScene().setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.rightTurn(false);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.rightTurn(false);
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
