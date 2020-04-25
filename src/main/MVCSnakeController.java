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

import java.util.Random;

import static main.GameAsset.MyRotate.LEFT;
import static main.GameAsset.MyRotate.RIGHT;

public class MVCSnakeController {

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
        this.theView = new MVCSnakeView();
        this.theModel = new MVCSnakeModel();
        this.turnLeft = false;
        this.turnRight = false;
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

        for (GameAsset item : this.theModel.getListOfItems())
            if (this.theView.getPlayer().checkForCollision(item))
            {
                handleItemCollision(item);
                this.theView.getRoot().getChildren().removeAll(item);
                this.theModel.getInactiveFoodNodes().add(item);
            }

        for (Rectangle wall : this.theModel.getListOfWalls()) {
            if (this.theView.getPlayer().checkForCollision(wall)) {
                theView.getPlayer().deactivate();
            }
        }

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
        if (item instanceof Potion) {
            for (int i = 0; i < theModel.getPotionLength(); i++)
                this.theView.getRoot().getChildren().add(((Snake) this.theView.getPlayer()).addTail());
        } else if (item instanceof Poison) {
            for (int i = 0; i < theModel.getPoisonLength(); i++)
                this.theView.getRoot().getChildren().removeAll(((Snake) theView.getPlayer()).removeTail());
        } else {
            for (int i = 0; i < theModel.getFoodLength(); i++)
                this.theView.getRoot().getChildren().add(((Snake) this.theView.getPlayer()).addTail());
        }
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
        if (this.theModel.getListOfItems().size() < 30)
        {
            int randomInt = randomNumGen.nextInt(2000);
            if (randomInt < 25)
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

                this.theModel.getListOfItems().add(newItem);
                SnakeUtil.addToGame(theView.getRoot(), newItem,
                        60 + (this.randomNumGen.nextInt(theView.getWIDTH() - 150)),
                        60 + (this.randomNumGen.nextInt(theView.getHEIGHT() - 150)));
            }
        }
    }

    /**
     * Will remove any inactive items from the list
     *
     * @author Christopher Asbrock
     */
    private void itemCleanUp()
    {
        theModel.getListOfItems().removeAll(theModel.getInactiveFoodNodes());
        theModel.getInactiveFoodNodes().clear();
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
            itemCleanUp();
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
}
