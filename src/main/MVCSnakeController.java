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



    private ArrayList<double[]> itemListPositions;
    private ArrayList<double[]> snakeListPositions;

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

    public ArrayList<double[]> getItemListPositions() {
        return itemListPositions;
    }

    public ArrayList<double[]> getSnakeListPositions() {
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

    public void run()
    {
        System.out.println("setting up model");
        this.theModel = new MVCSnakeModel(this);
        this.theModel.runListener();
    }

    public static void main(String [] args)
    {
        MVCSnakeController controller = new MVCSnakeController();
        controller.run();

        while (true)
        {
            //needs to keep running so the thread can function properly
        }
    }
}
