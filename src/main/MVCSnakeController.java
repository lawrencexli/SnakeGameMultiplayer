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

import java.io.IOException;
import java.util.ArrayList;

public class MVCSnakeController {

    protected boolean dataWrite;
    protected boolean gameGoing;

    /**a reference to the models item list*/
    private final ArrayList<Circle> ITEM_POSITIONING;

    /**a reference to the models list of snakes and its list of parts*/
    private final ArrayList<ArrayList<Circle>> SNAKE_PARTS_POSITIONING;

    /**a reference to the scrap array that gets cleared after each update*/
    private final ArrayList<Node> SCRAP_NODES;

    public MVCSnakeView getVIEW()
    {
        return VIEW;
    }

    /** MVC Snake View */
    private final MVCSnakeView VIEW;

    /** MVC Snake Model */
    private final MVCSnakeModel MODEL;

    public MVCSnakeController(MVCSnakeView view)
    {
        this.VIEW = view;
        this.MODEL = new MVCSnakeModel(this);
        this.gameGoing = false;

        this.ITEM_POSITIONING = this.MODEL.getItemListPositions();
        this.SNAKE_PARTS_POSITIONING = this.MODEL.getSnakeListPositions();
        this.SCRAP_NODES = this.MODEL.getScrapNodes();

        this.dataWrite = false;
    }

    public void setHost(String port, String players, String width, String height)
    {
        try
        {
            this.MODEL.createNetwork(port, players, width, height);
            long time = System.currentTimeMillis();

            while (System.currentTimeMillis() - time < 5000)
            {
                //give this a moment to start up the network, or it'll will just fly into the connection that isn't there
            }

            this.setJoin("localhost", port);
        }
        catch (NumberFormatException e)
        {
            this.displayError(e.getMessage());
        }
    }

    public void displayError(String message)
    {
        this.VIEW.getDisplayMessage().setText(message);
    }

    public void setJoin(String host, String port)
    {
        this.MODEL.modelInit(host, port);
    }

    public synchronized ArrayList<Circle> getITEM_POSITIONING() {
        return ITEM_POSITIONING;
    }

    public synchronized ArrayList<ArrayList<Circle>> getSNAKE_PARTS_POSITIONING() {
        return SNAKE_PARTS_POSITIONING;
    }

    public synchronized ArrayList<Node> getTrash() {
        return SCRAP_NODES;
    }

    public void run()
    {
        this.MODEL.runListener();
    }

    public void updateView()
    {
        Platform.runLater(()->this.VIEW.updateView());
    }

    public void sendDirection(String turn_left, boolean b)
    {
        this.MODEL.sendDirection(turn_left,b);
    }
}
