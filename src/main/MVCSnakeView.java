/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/21/20
 * Time: 11:11 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeView
 *
 * Description:
 * MVC snake view
 * ****************************************
 */
package
        main;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import static main.GameAsset.MyRotate.LEFT;
import static main.GameAsset.MyRotate.RIGHT;

/** The MVC snake view class */
public class MVCSnakeView{

    /** Main display window for the snake game */
    private Pane root;

    /** The MVC Snake Model */
    //private MVCSnakeModel theModel;

    /** Fixed width of the window */
    private final int WIDTH = 800;
    /** Fixed height of the window */
    private final int HEIGHT = 600;

    /** Random number generator */
    private Random randomNumGen;

    /** A list containing all items currently in the pane */
    private ArrayList<GameAsset> listOfItems;
    /** A list of all walls currently in the pane */
    private ArrayList<Rectangle> listOfWalls;

    /** The player character */
    private GameAsset player;
    /** Trigger for a right turn */
    private boolean turnRight;
    /** Trigger for a left turn */
    private boolean turnLeft;

    /**
     * A trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     */
    private ArrayList<GameAsset> inactiveFoodNodes;

    /**
     * initializes lists of items and the pane to a certain size
     *
     * @author Christopher Asbrock
     */
    public MVCSnakeView() {
        //this.theModel = new MVCSnakeModel(); //Needs snake model
        this.root = new Pane();
        this.root.setStyle("-fx-background-color: dark;");
        this.root.setPrefSize(WIDTH, HEIGHT);

        this.randomNumGen = new Random();

        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();

        makeSnake();
        setUpWalls();
    }

    /**
     * Creates and adds a snake (a object including a list of SnakeTail)
     */
    private void makeSnake() {
        this.player = new Snake();
        this.player.setVelocity(1,0);
        SnakeUtil.addToGame(this.root, this.player, this.WIDTH/4.0, this.HEIGHT/2.0);
        this.turnLeft = false;
        this.turnRight = false;
    }

    /**
     * creates and adds a wall to the pane and list of walls
     * @param width - the width of the wall
     * @param height - the height of the wall
     * @param posX - the x position to place the wall
     * @param posY - the y position to place the wall
     */
    private void makeWall(double width, double height, double posX, double posY) {
        Rectangle wall = new Rectangle(width, height, Color.DARKRED);
        listOfWalls.add(wall);
        SnakeUtil.addToGame(this.root, wall, posX ,posY);
    }

    /**
     * sets up 4 rectangles around the parameter of the pane to act as walls
     */
    private void setUpWalls() {
        makeWall(30, this.root.getPrefHeight(),0 ,0);
        makeWall(30, root.getPrefHeight(),root.getPrefWidth() - 30 ,0);
        makeWall(root.getPrefWidth(), 30,0 ,0);
        makeWall(root.getPrefWidth(), 30,0 ,root.getPrefHeight() - 30);
    }

    /**
     * UpdateView when called by the controller will use the corresponding list of coordinates in
     * controller to update the size of this classes SnakeTails list to the same size,
     * then update the positioning of each tail piece based on the presented data.
     */
     private void updateView() {
         //TODO - complete updateView
         if (this.turnRight) {

         }

         if (this.turnLeft) {

         }
     }

    /**
     * the main driver that updates the screen 60 times a second.
     * food actions will only happen when player is alive
     *
     * @author Christopher Asbrock
     */
    private void updateDriver() {
        if (player != null) {
            foodPlacer();
            itemCleanUp();
            handlePlayer();
        }
        this.listOfItems.forEach(GameAsset::updateAsset);
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer() {
        if (this.turnRight)
            this.player.rotate(LEFT);
        else if (this.turnLeft)
            this.player.rotate(RIGHT);

        for (GameAsset item : this.listOfItems)
            if (this.player.checkForCollision(item)) {
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
    private void updatePlayer() {
        if (this.player.isNoLongerActive()) {
            this.root.getChildren().removeAll(((Snake) this.player).getSnakeTails());
            ((Snake) this.player).getSnakeTails().clear();
            this.root.getChildren().removeAll(this.player);
            this.player = null;
        }
        else {
            this.player.updateAsset();
        }
    }

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     *
     * @author Christopher Asbrock
     */
    private void foodPlacer() {
        if (this.listOfItems.size() < 30) {
            int randomInt = randomNumGen.nextInt(2000);
            if (randomInt < 25) {
                Item newItem;
                switch (randomInt) {
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
                        60 + (this.randomNumGen.nextInt(WIDTH- 150)),
                        60 + (this.randomNumGen.nextInt(HEIGHT- 150)));
            }
        }
    }

    /**
     * Will remove any inactive items from the list
     *
     * @author Christopher Asbrock
     */
    private void itemCleanUp() {
        listOfItems.removeAll(inactiveFoodNodes);
        inactiveFoodNodes.clear();
    }

    /**
     * When colliding with an Item this determines the instance type and adds or removes pieces of the snake
     * accordingly
     *
     * @author Christopher Asbrock
     *
     * @param item - the idem being collided with
     */
    private void handleItemCollision(GameAsset item) {
        if (item instanceof Potion) {
            for (int i = 0; i < ((Potion) item).getPotionLength(); i++)
                this.root.getChildren().add(((Snake) this.player).addTail());
        } else if (item instanceof Poison) {
            for (int i = 0; i < ((Poison) item).getPoisonLength(); i++)
                this.root.getChildren().removeAll(((Snake) player).removeTail());
        } else if (item instanceof Food){
            for (int i = 0; i < ((Food) item).getFoodLength(); i++)
                this.root.getChildren().add(((Snake) this.player).addTail());
        }
    }

    /**
     * revamps the stages button inputs to activate on both the up and down stroke to trigger a boolean
     * as true on down and false on up
     *
     * @author Christopher Asbrock
     *
     * @param stage - the stage to apply input to
     */
    private void userInputButtonPress(Stage stage) {
        stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT)
                this.turnLeft = true;
            if (event.getCode() == KeyCode.RIGHT)
                this.turnRight = true;
            if (event.getCode() == KeyCode.UP)
                System.out.println("up");
        });
        stage.getScene().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT)
                this.turnLeft = false;
            if (event.getCode() == KeyCode.RIGHT)
                this.turnRight = false;
            if (event.getCode() == KeyCode.UP)
                System.out.println("up");
        });
    }
}
