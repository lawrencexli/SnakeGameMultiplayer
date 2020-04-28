package main;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MVCSnakeView extends Application
{
    private Pane root;

    private ArrayList<SnakeTail> snakeTails;
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

    public synchronized void updateView()
    {
        this.controller.dataWrite = true;

        ArrayList<Circle> tempOne = this.controller.getSnakeListPositions();
        ArrayList<Circle> tempTwo = this.controller.getItemListPositions();
        ArrayList<Node> tempThree = this.controller.getTrash();

        while (!tempThree.isEmpty())
            this.root.getChildren().remove(tempThree.remove(0));

        for (Circle part : tempOne)
            if (part != null && !this.root.getChildren().contains(part))
                this.root.getChildren().add(part);

        for (Circle part : tempTwo)
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
