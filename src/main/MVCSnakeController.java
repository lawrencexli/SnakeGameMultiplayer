/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/25/20
 * Time: 10:34 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeController
 *
 * Description:
 *
 * ****************************************
 */
package
        main;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import static main.GameAsset.MyRotate.LEFT;
import static main.GameAsset.MyRotate.RIGHT;

public class MVCSnakeController {



    private ArrayList<Positioning> itemListPositions;
    private ArrayList<Positioning> snakeListPositions;

    /**trigger for a right turn*/
    private boolean turnRight;
    /**trigger for a left turn*/
    private boolean turnLeft;

    /**random number generator*/
    private Random randomNumGen;

    /** MVC Snake View */
    private MVCSnakeView theView;
    /** MVC Snake Model */
    private MVCSnakeModel theModel;

    public MVCSnakeController() {
        //this.theView = new MVCSnakeView();
        //this.theModel = new MVCSnakeModel(this);

        this.itemListPositions = new ArrayList<>();
        this.snakeListPositions = new ArrayList<>();

        this.turnLeft = false;
        this.turnRight = false;
    }

    public ArrayList<Positioning> getItemListPositions() {
        return itemListPositions;
    }

    public ArrayList<Positioning> getSnakeListPositions() {
        return snakeListPositions;
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer()
    {
        if (this.turnRight)
            this.theView.getPlayer().rotate(LEFT);
        else if (this.turnLeft)
            this.theView.getPlayer().rotate(RIGHT);


        int i = 0;
        for (SnakeTail tail : ((Snake) this.theView.getPlayer()).getSnakeTails()) {
            if (i++ < 100)
                continue;       // First several SnakeTails always collide with the head

            if (this.theView.getPlayer().checkForCollision(tail)) {
                System.out.printf("Collided with tail number %d\n", tail.id);
                theView.getPlayer().deactivate();
            }
        }
        updatePlayer();
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

    }

    /**
     * updates the player based on whether they are active or not,
     * if they are it will update them and their pieces,
     * if not it will remove them from the game
     */
    private void updatePlayer()
    {
        if (this.theView.getPlayer().isNoLongerActive())
        {
            this.theView.getRoot().getChildren().removeAll(((Snake) this.theView.getPlayer()).getSnakeTails());
            ((Snake) this.theView.getPlayer()).getSnakeTails().clear();
            this.theView.getRoot().getChildren().removeAll(this.theView.getPlayer());
            this.theView.setPlayerNull();
        }
        else
        {
            this.theView.getPlayer().updateAsset();
        }
    }

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     *
     * @author Christopher Asbrock
     */
    private void foodPlacer()
    {

    }



    /**
     * the main driver that updates the screen 60 times a second.
     * food actions will only happen when player is alive
     *
     * @author Christopher Asbrock
     */
    private void updateDriver()
    {
        if (theView.getPlayer() != null)
        {
            foodPlacer();

            handlePlayer();
        }

        //this.listOfItems.forEach(GameAsset::updateAsset);
    }

    public void run()
    {
        this.theModel = new MVCSnakeModel(this);
        this.theModel.runListener();

        while (this.theModel.gameRunning)
        {
            //needs to keep running so the thread can function properly
        }

        System.out.println("Controller ShutDown");
    }

    public static void main(String [] args)
    {
        MVCSnakeController controller = new MVCSnakeController();
        controller.run();
    }
}
