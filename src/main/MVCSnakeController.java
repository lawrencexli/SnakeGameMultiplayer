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

public class MVCSnakeController {

    protected boolean dataWrite;

    private ArrayList<Circle> itemListPositions;
    private ArrayList<Circle> snakeListPositions;
    private ArrayList<Node> scrapNodes;

    /** MVC Snake View */
    private TempView theView;
    /** MVC Snake Model */
    private MVCSnakeModel theModel;

    public MVCSnakeController(TempView view) {
        //this.theView = new MVCSnakeView();
        //this.theModel = new MVCSnakeModel(this);

        this.theView = view;
        this.theModel = new MVCSnakeModel(this);

        this.itemListPositions = this.theModel.getItemListPositions();
        this.snakeListPositions = this.theModel.getSnakeListPositions();
        this.scrapNodes = this.theModel.getScrapNodes();

        this.dataWrite = false;
    }

    public synchronized ArrayList<Circle> getItemListPositions() {
        return itemListPositions;
    }

    public synchronized ArrayList<Circle> getSnakeListPositions() {
        return snakeListPositions;
    }

    public synchronized ArrayList<Node> getTrash() {
        return scrapNodes;
    }

    public void run()
    {
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

    public synchronized void updateView()
    {
        Platform.runLater(()->this.theView.updateView());
    }

    public void sendDirection(String turn_left, boolean b)
    {
        this.theModel.sendDirection(turn_left,b);
    }
}
