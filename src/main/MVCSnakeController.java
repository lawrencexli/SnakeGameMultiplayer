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
    protected boolean gameGoing;

    private ArrayList<Circle> itemListPositions;
    private ArrayList<ArrayList<Circle>> snakeListPositions;
    private ArrayList<Node> scrapNodes;

    /** MVC Snake View */
    private MVCSnakeView theView;
    /** MVC Snake Model */
    private MVCSnakeModel theModel;

    public MVCSnakeController(MVCSnakeView view)
    {
        this.theView = view;
        this.theModel = new MVCSnakeModel();
        this.gameGoing = false;

        this.itemListPositions = this.theModel.getItemListPositions();
        this.snakeListPositions = this.theModel.getSnakeListPositions();
        this.scrapNodes = this.theModel.getScrapNodes();

        this.dataWrite = false;
    }

    public void setHost(String port)
    {
        System.out.println("start host");
        this.theModel.createNetwork(Integer.parseInt(port));
        long time = System.currentTimeMillis();

        while (System.currentTimeMillis() - time < 5000)
        {

        }

        this.setJoin("localhost", port);
    }

    public void setJoin(String host, String port)
    {
        System.out.println("start join");
        this.theModel.modelInit(this,host, port);
        this.gameGoing = true;
        this.getTrash().addAll(this.theView.startMenu.getChildren());
        this.getTrash().add(this.theView.startMenu);
        new Thread(()-> this.run()).start();
    }

    public synchronized ArrayList<Circle> getItemListPositions() {
        return itemListPositions;
    }

    public synchronized ArrayList<ArrayList<Circle>> getSnakeListPositions() {
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
        }

        System.out.println("Controller ShutDown");
    }

    public void updateView()
    {
        Platform.runLater(()->this.theView.updateView());
    }

    public void sendDirection(String turn_left, boolean b)
    {
        this.theModel.sendDirection(turn_left,b);
    }
}
