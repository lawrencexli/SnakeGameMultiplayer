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

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/** The MVC snake view class */
public class MVCSnakeView{

    /** Main display window for the snake game */
    private Pane root;

//    /** The MVC Snake Model */
//    private MVCSnakeModel theModel;

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
        this.root = new Pane();
        this.root.setStyle("-fx-background-color: dark;");
        this.root.setPrefSize(WIDTH, HEIGHT);

        //this.theModel = new MVCSnakeModel();
        this.randomNumGen = new Random();

        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();

        this.player = new Snake();
        this.player.setVelocity(1,0);
        SnakeUtil.addToGame(this.root, this.player, this.WIDTH/4.0, this.HEIGHT/2.0);

        this.turnLeft = false;
        this.turnRight = false;

        setUpWalls();
    }

    /**
     * creates and adds a wall to the pane and list of walls
     *  @param width - the width of the wall
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
     * when colliding with an Item this determines the instance type and adds or removes pieces of the snake
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
}
