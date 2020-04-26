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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MVCSnakeController {



    private ArrayList<Circle> itemListPositions;
    private ArrayList<Circle> snakeListPositions;
    private ArrayList<Node> scrapNodes;

    /**trigger for a right turn*/
    private boolean turnRight;
    /**trigger for a left turn*/
    private boolean turnLeft;

    /**random number generator*/
    private Random randomNumGen;

    /** MVC Snake View */
    private TempView theView;
    /** MVC Snake Model */
    private MVCSnakeModel theModel;

    public MVCSnakeController(TempView view) {
        //this.theView = new MVCSnakeView();
        //this.theModel = new MVCSnakeModel(this);

        this.theView = view;
        this.itemListPositions = new ArrayList<>();
        this.snakeListPositions = new ArrayList<>();
        this.scrapNodes = new ArrayList<>();

        this.turnLeft = false;
        this.turnRight = false;
    }

    public ArrayList<Circle> getItemListPositions() {
        return itemListPositions;
    }

    public ArrayList<Circle> getSnakeListPositions() {
        return snakeListPositions;
    }

    public ArrayList<Node> getTrash() {
        return scrapNodes;
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
        MVCSnakeController controller = new MVCSnakeController(null);
        controller.run();
    }

    public void updateView()
    {
        Platform.runLater(()->this.theView.updateView());
    }

    public void resizeArrayList(int size, List<Circle> list)
    {
        if (list.size() < size)
            while (list.size() < size)
                list.add(null);
        else if (list.size() > size)
            while (list.size() > size)
                this.scrapNodes.add(list.remove(0));
    }

    public void sendDirection(String turn_left, boolean b)
    {
        this.theModel.sendDirection(turn_left,b);
    }
}
