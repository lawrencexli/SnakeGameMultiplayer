package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

/**
 * the main view for the user,
 *
 * Shows a game of snake happening in real time,
 * updates movements 60 times a second
 * player can change the angle of the snake by using their arrow keys
 *
 * @author Christopher Asbrock
 */
public class SnakePane extends Application
{
    /**Main game javafx window*/
    private Pane root;

    /**fixed width of the window*/
    private final int WIDTH = 800;
    /**fixed height of the window*/
    private final int HEIGHT = 600;

    private Random randamizer;

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;
    /**a list of all walls currently in the pane*/
    private ArrayList<GameAsset> listOfWalls;


    /**
     * initializes lists of items and the pane to a certain size
     *
     * @author Christopher Asbrock
     */
    @Override
    public void init()
    {
        this.root = new Pane();
        this.root.setPrefSize(WIDTH, HEIGHT);

        this.randamizer = new Random();

        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
    }

    /**
     * the main driver that updates the screen 60 times a second
     *
     * @author Christopher Asbrock
     */
    private void updateDriver()
    {
        if (randamizer.nextInt(30) < 1)
        {
            Food newFood = new Food(15,15,15, Color.RED);
            this.listOfItems.add(newFood);
            SnakeUtil.addToGame(root, newFood, this.randamizer.nextInt(WIDTH), this.randamizer.nextInt(HEIGHT));
        }
        this.listOfItems.forEach(GameAsset::updateAsset);
        this.listOfWalls.forEach(GameAsset::updateAsset);
    }

    /**
     * Inherited start method to set up and start the scene
     *
     * @author Christopher Asbrock
     *
     * @param primaryStage - the primary stage
     */
    @Override
    public void start(Stage primaryStage)
    {
        AnimationTimer ticks = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                updateDriver();
            }
        };

        ticks.start();


        primaryStage.setScene(new Scene(this.root));
        this.userInputButtonPress(primaryStage, (event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                System.out.println("left");
            if (event.getCode() == KeyCode.RIGHT)
                System.out.println("right");
            if (event.getCode() == KeyCode.UP)
                System.out.println("up");
        }));

        primaryStage.show();
    }

    /**
     * revamps the stages button inputs to activate on both the up and down stroke to trigger a boolean
     * as true on down and false on up
     *
     * @author Christopher Asbrock
     *
     * @param stage - the stage to apply input to
     * @param event - the event to lambda in
     */
    private void userInputButtonPress(Stage stage, EventHandler<? super KeyEvent> event )
    {
        stage.getScene().setOnKeyPressed(event);
        stage.getScene().setOnKeyReleased(event);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
