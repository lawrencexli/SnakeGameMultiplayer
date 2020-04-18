package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    /**random number generator*/
    private Random randomizer;

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;
    /**a list of all walls currently in the pane*/
    private ArrayList<Rectangle> listOfWalls;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<Node> inactiveNodes;

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

        this.randomizer = new Random();

        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
        this.inactiveNodes = new ArrayList<>();

        setUpWalls(Color.DARKRED);
    }

    /**
     * sets up 4 rectangles around the parameter of the pane to act as walls
     */
    private void setUpWalls(Color color)
    {
        makeWall(30, this.root.getPrefHeight(),0 ,0, color);
        makeWall(30, root.getPrefHeight(),root.getPrefWidth() - 30 ,0, color);
        makeWall(root.getPrefWidth(), 30,0 ,0, color);
        makeWall(root.getPrefWidth(), 30,0 ,root.getPrefHeight() - 30, color);

    }

    /**
     * creates and adds a wall to the pane and list of walls
     *
     * @param width - the wide of the wall
     * @param height - the height of the wall
     * @param posX - the x position to place the wall
     * @param posY - the y position to place the wall
     * @param color - the color of the wall
     */
    private void makeWall(double width, double height, double posX, double posY, Color color)
    {
        Rectangle wall = new Rectangle(width, height, color);
        listOfWalls.add(wall);
        SnakeUtil.addToGame(this.root, wall, posX ,posY);
    }

    /**
     * the main driver that updates the screen 60 times a second
     *
     * @author Christopher Asbrock
     */
    private void updateDriver()
    {
        foodPlacer();
        updateAllAssets();
        listOfItems.forEach(GameAsset::updateAsset);
    }

    /**
     * Will remove any inactive items from the list
     */
    private void updateAllAssets()
    {
        listOfItems.removeAll(inactiveNodes);
        inactiveNodes.clear();
    }

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     */
    private void foodPlacer()
    {
        if (randomizer.nextInt(100) < 1)
        {
            Food newFood = new Food(15, Color.RED);
            this.listOfItems.add(newFood);
            SnakeUtil.addToGame(root, newFood,
                    60 + (this.randomizer.nextInt(WIDTH- 150)),
                    60 + (this.randomizer.nextInt(HEIGHT- 150)));
        }
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
