package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import network.SnakeTail;
import network.SnakeUtil;

import java.util.ArrayList;

public class TempView extends Application
{
    private Pane root;

    private ArrayList<network.SnakeTail> snakeTails;
    private ArrayList<Item> items;

    private MVCSnakeController controller;

    public void init()
    {
        root = new Pane();
        root.setPrefSize(800, 600);

        snakeTails = new ArrayList<>();
        items = new ArrayList<>();
        controller = new MVCSnakeController(this);
        new Thread(()-> this.controller.run()).start();
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(this.root));
        this.userInputButtonPress(primaryStage);
        primaryStage.show();
    }

    public void updateView()
    {
        while (!this.controller.getTrash().isEmpty())
            this.root.getChildren().remove(this.controller.getTrash().remove(0));

        for (Circle part : this.controller.getSnakeListPositions())
            if (!this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : this.controller.getItemListPositions())
            if (!this.root.getChildren().contains(part))
                this.root.getChildren().add(part);
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
