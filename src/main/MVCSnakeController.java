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

    protected String menuMessage;
    protected final int WIDTH = 800;
    protected final int HEIGHT = 600;
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

    public int getNumOfPlayers()
    {
        return this.MODEL.playerCount;
    }

    public void setHost(String port, String players, String width, String height)
    {
        try
        {
            this.MODEL.createNetwork(port, players, width, height);
            long time = System.currentTimeMillis();

            while (System.currentTimeMillis() - time < 3000)
            {
                /*
                    give this a moment to start up the network, or it'll will just fly into the connection
                     isn't there

                     much rather use the network as a model itself for the host, but little low on time, so
                     maybe later
                 */
            }

            //after the network is set up, locally join it
            this.setJoin("localhost", port);
        }
        catch (NumberFormatException e)
        {
            this.displayMessage(e.getMessage());
        }
    }

    public void displayMessage(String message)
    {
        this.menuMessage = message;
        this.updateView();
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

    public void updateView()
    {
        Platform.runLater(this.VIEW::updateView);
    }

    public void leftTurn(boolean b)
    {
        this.MODEL.sendDirection(this.MODEL.TURN_LEFT, b);
    }

    public void rightTurn(boolean b)
    {
        this.MODEL.sendDirection(this.MODEL.TURN_RIGHT, b);
    }
}
