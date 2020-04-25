/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/25/20
 * Time: 10:01 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeModel
 *
 * Description:
 *
 * ****************************************
 */
package
        main;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MVCSnakeModel {

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;
    /**a list of all walls currently in the pane*/
    private ArrayList<Rectangle> listOfWalls;

    /**Length of snake added for potion*/
    private int potionLength = 50;
    /**Length of snake added for food*/
    private int foodLength = 5;
    /**Length of snake deleted for poison*/
    private int poisonLength = 100;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    public MVCSnakeModel() {
        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();
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

    public ArrayList<GameAsset> getListOfItems() {
        return listOfItems;
    }

    public ArrayList<Rectangle> getListOfWalls() {
        return listOfWalls;
    }

    public int getPotionLength() {
        return potionLength;
    }

    public int getFoodLength() {
        return foodLength;
    }

    public int getPoisonLength() {
        return poisonLength;
    }

    public ArrayList<GameAsset> getInactiveFoodNodes() {
        return inactiveFoodNodes;
    }
}
