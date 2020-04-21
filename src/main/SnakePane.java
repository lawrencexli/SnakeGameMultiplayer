package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import static main.GameAsset.MyRotate.LEFT;
import static main.GameAsset.MyRotate.RIGHT;

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

    /**the player character*/
    private GameAsset player;
    /**trigger for a right turn*/
    private boolean turnRight;
    /**trigger for a left turn*/
    private boolean turnLeft;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

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
        this.inactiveFoodNodes = new ArrayList<>();

        this.player = new Snake();
        this.player.setVelocity(0,0);
        SnakeUtil.addToGame(this.root, this.player, this.WIDTH/2.0, this.HEIGHT/2.0);

        this.turnLeft = false;
        this.turnRight = false;

        setUpWalls(Color.DARKRED);
    }

    /**
     * creates and adds a wall to the pane and list of walls
     *
     * @param width - the width of the wall
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
     * the main driver that updates the screen 60 times a second.
     * food actions will only happen when player is alive
     *
     * @author Christopher Asbrock
     */
    private void updateDriver()
    {
        if (player != null)
        {
            foodPlacer();
            itemCleanUp();
            handlePlayer();
        }

        //this.listOfItems.forEach(GameAsset::updateAsset);
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer()
    {
        if (this.turnRight)
            this.player.rotate(LEFT);
        else if (this.turnLeft)
            this.player.rotate(RIGHT);

        for (GameAsset item : this.listOfItems)
            if (this.player.checkForCollision(item))
            {
                handleItemCollision(item);
                this.root.getChildren().removeAll(item);
                this.inactiveFoodNodes.add(item);
            }

        for (Rectangle wall : this.listOfWalls) {
            if (this.player.checkForCollision(wall)) {
                player.deactivate();
            }
        }

        int i = 0;
        for (SnakeTail tail : ((Snake) this.player).getSnakeTails()) {
            if (i++ < 100)
                continue;       // First several SnakeTails always collide with the head

            if (this.player.checkForCollision(tail)) {
                System.out.printf("Collided with tail number %d\n", tail.id);
                player.deactivate();
            }
        }

        updatePlayer();
    }

    /**
     * updates the player based on whether they are active or not,
     * if they are it will update them and their pieces,
     * if not it will remove them from the game
     */
    private void updatePlayer()
    {
        if (this.player.isNoLongerActive())
        {
            this.root.getChildren().removeAll(((Snake) this.player).getSnakeTails());
            ((Snake) this.player).getSnakeTails().clear();
            this.root.getChildren().removeAll(this.player);
            this.player = null;
        }
        else
        {
            this.player.updateAsset();
        }
    }

    /**
     * when colliding with an Item this determines the instance type and adds or removes pieces of the snake
     * accordingly
     *
     * @author Christopher Asbrock
     *
     * @param item - the idem being collided with
     */
    private void handleItemCollision(GameAsset item)
    {
        if (item instanceof Potion)
            for (int i = 0; i < 50; i++)
                this.root.getChildren().add(((Snake) this.player).addTail());
        else if (item instanceof Poison)
            for (int i = 0; i < 100; i++)
                this.root.getChildren().removeAll(((Snake) player).removeTail());
        else
            for (int i = 0; i < 5; i++)
                this.root.getChildren().add(((Snake) this.player).addTail());
    }

    /**
     * Will remove any inactive items from the list
     *
     * @author Christopher Asbrock
     */
    private void itemCleanUp()
    {
        listOfItems.removeAll(inactiveFoodNodes);
        inactiveFoodNodes.clear();
    }

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     *
     * @author Christopher Asbrock
     */
    private void foodPlacer()
    {
        if (this.listOfItems.size() < 30)
        {
            int randomInt = randomizer.nextInt(2000);
            if (randomInt < 20)
            {
                Item newItem;
                switch (randomInt)
                {
                    case 1:
                        newItem = new Potion(12, Color.GOLD);
                        break;
                    case 2:
                        newItem = new Poison(11, Color.GREEN);
                        break;
                    default:
                        newItem = new Food(10, Color.BLUE);
                }

                this.listOfItems.add(newItem);
                SnakeUtil.addToGame(root, newItem,
                        60 + (this.randomizer.nextInt(WIDTH- 150)),
                        60 + (this.randomizer.nextInt(HEIGHT- 150)));
            }
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
        this.userInputButtonPress(primaryStage);

        primaryStage.show();
    }

    /**
     * revamps the stages button inputs to activate on both the up and down stroke to trigger a boolean
     * as true on down and false on up
     *
     * @author Christopher Asbrock
     *
     * @param stage - the stage to apply input to
     */
    private void userInputButtonPress(Stage stage)
    {
        stage.getScene().setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.turnLeft = true;
            if (event.getCode() == KeyCode.RIGHT)
                this.turnRight = true;
            if (event.getCode() == KeyCode.UP)
                System.out.println("up");
        });
        stage.getScene().setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.turnLeft = false;
            if (event.getCode() == KeyCode.RIGHT)
                this.turnRight = false;
            if (event.getCode() == KeyCode.UP)
                System.out.println("up");
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
