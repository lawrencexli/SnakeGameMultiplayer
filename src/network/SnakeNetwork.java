package network;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * the main view for the user,
 *
 * Shows a game of snake happening in real time,
 * updates movements 60 times a second
 * player can change the angle of the snake by using their arrow keys
 *
 * @author Christopher Asbrock
 */
public class SnakeNetwork
{
    private Socket socket;
    private ServerSocket server;

    private Scanner networkIn;
    private PrintStream networkOut;

    private boolean gameIsOn = false;

    /**fixed width of the window*/
    private final int WIDTH = 800;
    /**fixed height of the window*/
    private final int HEIGHT = 600;

    /**random number generator*/
    private Random randomizer;

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;
    /**a list of all walls currently in the pane*/
    private ArrayList<Rectangle> listOfWalls;

    /**the player character*/
    private GameAsset player;
    /**trigger for a right turn*/
    private boolean turnRight;
    /**trigger for a left turn*/
    private boolean turnLeft;

    /**Length of snake added for potion*/
    private final int POTION_LENGTH = 50;
    /**Length of snake added for food*/
    private final int FOOD_LENGTH = 5;
    /**Length of snake deleted for poison*/
    private final int POISON_LENGTH = 100;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    /**
     * initializes lists of items and the pane to a certain size
     *
     * @author Christopher Asbrock
     */
    public void init()
    {
        this.randomizer = new Random();

        this.listOfItems = new ArrayList<>();
        this.listOfWalls = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();

        this.player = new Snake();
        this.player.setVelocity(1,0);
        ((Snake) this.player).addTail();
        SnakeUtil.addToGame(this.player, this.WIDTH/4.0, this.HEIGHT/2.0);

        this.turnLeft = true;
        this.turnRight = false;

        new Thread(this::setUpNetworkConnection).start();

        setUpWalls(Color.DARKRED);
    }

    private void setUpNetworkConnection()
    {
        try
        {
            server = new ServerSocket(1111);
            socket = server.accept();

            networkIn = new Scanner(socket.getInputStream());
            networkOut = new PrintStream(socket.getOutputStream());

            new Thread(this::networkListener).start();
            this.gameIsOn = true;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void networkListener()
    {
        while (this.gameIsOn)
        {
            String input = this.networkIn.nextLine();

            String protocol = input.split(" ")[0];
            String message = input.substring(protocol.length() + 1);

            switch (protocol)
            {
                case "TURN_LEFT":
                    this.turnLeft = message.equalsIgnoreCase("true");
                    break;
                case "TURN_RIGHT":
                    this.turnRight = message.equalsIgnoreCase("true");
                    break;
            }
            //System.out.println(protocol);
            //System.out.println(message);
        }
    }

    /**
     * creates and adds a wall to the pane and list of walls
     *
     * @param width - the width of the wall
     * @param height - the height of the wall
     * @param posX - the x position to place the wall
     * @param posY - the y position to place the wall
     * @param color - the color of the wall
     */
    private void makeWall(double width, double height, double posX, double posY, Color color)
    {
        Rectangle wall = new Rectangle(width, height, color);
        listOfWalls.add(wall);
        SnakeUtil.addToGame(wall, posX ,posY);
    }

    /**
     * sets up 4 rectangles around the parameter of the pane to act as walls
     */
    private void setUpWalls(Color color)
    {
        makeWall(30, HEIGHT,0 ,0, color);
        makeWall(30, HEIGHT,WIDTH - 30 ,0, color);
        makeWall(WIDTH, 30,0 ,0, color);
        makeWall(WIDTH, 30,0 ,HEIGHT - 30, color);
    }

    /**
     * the main driver that updates the screen 60 times a second.
     * food actions will only happen when player is alive
     *
     * @author Christopher Asbrock
     */
    private void updateDriver()
    {
        if (gameIsOn)
        {
            if (player != null)
            {
                foodPlacer();
                itemCleanUp();
                handlePlayer();
            }

            sendNetworkInfo();

        }
    }

    private void sendNetworkInfo()
    {
        String allInfo = "";

        for (GameAsset item : this.listOfItems)
        {
            allInfo += item.getTranslateX() + "," + item.getTranslateY() + ",";
            if (item instanceof Poison)
                allInfo += "2;";
            else if (item instanceof Potion)
                allInfo += "1;";
            else
                allInfo += "0;";
        }

        allInfo += "%";

        if (this.player != null)
            for (SnakeTail tail : ((Snake) this.player).getSnakeTails())
                allInfo += tail.getTranslateX() + "," + tail.getTranslateY() + "," + tail.getRotate() + ";";
        else
            allInfo += null;

        //System.out.println(allInfo);
       // this.networkOut.println("DATA " + allInfo);
        pushNetwork(allInfo);
    }

    private synchronized void pushNetwork(String info)
    {
        this.networkOut.println("DATA " + info);
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer()
    {
        if (this.turnRight)
            this.player.rotate(1);
        else if (this.turnLeft)
            this.player.rotate(-1);

        for (GameAsset item : this.listOfItems)
            if (this.player.checkForCollision(item))
            {
                handleItemCollision(item);
                this.inactiveFoodNodes.add(item);
            }

        for (Rectangle wall : this.listOfWalls) {
            if (this.player.checkForCollision(wall)) {
                System.out.println("HitWall");
                player.deactivate();
            }
        }

        int i = 0;
        for (SnakeTail tail : ((Snake) this.player).getSnakeTails()) {
            if (i++ < 100)
                continue;       // First several SnakeTails always collide with the head

            if (this.player.checkForCollision(tail)) {
                System.out.printf("Collided with tail number %d\n", tail.id);
                player.deactivate();
            }
        }

        updatePlayer();
    }

    /**
     * updates the player based on whether they are active or not,
     * if they are it will update them and their pieces,
     * if not it will remove them from the game
     */
    private void updatePlayer()
    {
        if (this.player.isNoLongerActive())
        {
            ((Snake) this.player).getSnakeTails().clear();
            this.player = null;
        }
        else
        {
            this.player.updateAsset();
        }
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
        if (item instanceof Potion)
            for (int i = 0; i < POTION_LENGTH; i++)
                ((Snake) this.player).addTail();
        else if (item instanceof Poison)
            for (int i = 0; i < POISON_LENGTH; i++)
                ((Snake) player).removeTail();
        else
            for (int i = 0; i < FOOD_LENGTH; i++)
                ((Snake) this.player).addTail();

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

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     *
     * @author Christopher Asbrock
     */
    private void foodPlacer()
    {
        if (this.listOfItems.size() < 130)
        {
            int randomInt = randomizer.nextInt(2000);
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

                this.listOfItems.add(newItem);
                SnakeUtil.addToGame(newItem,
                        60 + (this.randomizer.nextInt(WIDTH- 150)),
                        60 + (this.randomizer.nextInt(HEIGHT- 150)));
            }
        }
    }

    /**
     * Inherited start method to set up and start the scene
     *
     * @author Christopher Asbrock
     *
     */
    public void start()
    {
        Thread timer = new Thread(this::tickTimer);
        timer.start();
        while (true)
        {
            if (player.isNoLongerActive())
                break;
        }
    }

    private void tickTimer()
    {
        long now = System.currentTimeMillis();

        System.out.println("start");
        while (player != null && !player.isNoLongerActive())
        {

            if ((System.currentTimeMillis() - now) > 20) {
                now = System.currentTimeMillis();
                //System.out.println("tick");

                updateDriver();
            }
        }

        System.out.println("GameOver");
    }

    public static void main(String[] args)
    {
        SnakeNetwork test = new SnakeNetwork();
        test.init();
        test.start();
    }
}

