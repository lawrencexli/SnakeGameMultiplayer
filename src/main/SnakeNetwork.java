package main;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Socket[] socket;
    private ServerSocket server;

    private Scanner [] networkIn;
    private PrintStream [] networkOut;

    private boolean gameIsOn = false;

    /**fixed width of the window*/
    private int WIDTH;
    /**fixed height of the window*/
    private int HEIGHT;

    /**random number generator*/
    private Random randomizer;

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;

    /**the player character*/
    private GameAsset[] player;
    /**trigger for a right turn*/
    private boolean[] turnRight;
    /**trigger for a left turn*/
    private boolean[] turnLeft;

    /**Length of snake added for potion*/
    private final int POTION_LENGTH = 50;
    /**Length of snake added for food*/
    private final int FOOD_LENGTH = 5;
    /**Length of snake deleted for poison*/
    private final int POISON_LENGTH = 100;

    private int numOfPlayer;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    public SnakeNetwork(int numberOfPlayers)
    {
        this.numOfPlayer = numberOfPlayers;
    }
    /**
     * initializes lists of items and the pane to a certain size
     *
     * @author Christopher Asbrock
     */
    public void init(int port, int players, int width, int height)
    {
        this.numOfPlayer = players;
        this.WIDTH = width;
        this.HEIGHT = height;

        this.randomizer = new Random();

        this.listOfItems = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();

        this.player = new GameAsset[this.numOfPlayer];
        this.socket = new Socket[this.numOfPlayer];
        this.networkIn = new Scanner[this.numOfPlayer];
        this.networkOut = new PrintStream[this.numOfPlayer];

        for (int i = 0; i < this.numOfPlayer; i++)
        {
            this.player[i] = new Snake();
            this.player[i].setVelocity(0, 0);
            this.player[i].setRotate(90 * i + 45);
            ((Snake) this.player[i]).addTail();

            SnakeUtil.addToGame(this.player[i],
                    WIDTH / 2.0 + 50 * Math.cos(Math.toRadians(90 * i + 45)),
                    HEIGHT / 2.0 + 50 * Math.sin(Math.toRadians(90 * i + 45)));
        }

        this.turnLeft = new boolean[this.numOfPlayer];
        Arrays.fill(this.turnLeft, true);
        this.turnRight = new boolean[this.numOfPlayer];

        try
        {
            server = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < this.player.length; i++)
            if (this.player[i] != null)
                this.setUpNetworkConnection(i);

        this.start();
    }

    private void setUpNetworkConnection(int player)
    {
        try
        {
            System.out.println("Waiting on Player " + (player + 1));
            socket[player] = server.accept();
            System.out.println("Player " + (player + 1) + " connected");

            networkIn[player] = new Scanner(socket[player].getInputStream());
            networkOut[player] = new PrintStream(socket[player].getOutputStream());

            new Thread(() -> networkListener(player)).start();
            if (player == this.numOfPlayer - 1)
                this.gameIsOn = true;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void networkListener(int player)
    {
        while (this.gameIsOn)
        {
            String input = this.networkIn[player].nextLine();
            System.out.println(player + " " + input);
            String protocol = input.split(" ")[0];
            String message = input.substring(protocol.length() + 1);

            switch (protocol)
            {
                case "TURN_LEFT":
                    this.turnLeft[player] = message.equalsIgnoreCase("true");
                    break;
                case "TURN_RIGHT":
                    this.turnRight[player] = message.equalsIgnoreCase("true");
                    break;
            }
            //System.out.println(protocol);
            //System.out.println(message);
        }
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
        StringBuilder allInfo = new StringBuilder();

        for (int i = 0; i < this.listOfItems.size(); i++)
        {
            allInfo.append(this.listOfItems.get(i).getTranslateX()).append(",").append(this.listOfItems.get(i).getTranslateY()).append(",");
            if (this.listOfItems.get(i) instanceof Poison)
                allInfo.append("2");
            else if (this.listOfItems.get(i) instanceof Potion)
                allInfo.append("1");
            else
                allInfo.append("0");

            allInfo.append((i != this.listOfItems.size() - 1) ? ";" : "");
        }

        for (GameAsset gameAsset : this.player)
        {
            allInfo.append("%");

            if (gameAsset != null)
                for (int j = 0; j < ((Snake) gameAsset).getSnakeTails().size(); j++)
                    allInfo.append(((Snake) gameAsset).getSnakeTails().get(j).getTranslateX()).append(",").append(((Snake) gameAsset).getSnakeTails().get(j).getTranslateY()).append(",").append(((Snake) gameAsset).getSnakeTails().get(j).getRotate()).append((((Snake) gameAsset).getSnakeTails().size() - 1 != j) ? ";" : "");
            else
                allInfo.append("null");
        }
        //System.out.println(allInfo);
       // this.networkOut.println("DATA " + allInfo);
       // System.out.println(allInfo);
        pushNetwork(allInfo.toString());
    }

    private void pushNetwork(String info)
    {
        for (int i = 0; i < this.player.length; i++)
            this.networkOut[i].println("DATA " + info);
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer()
    {
        for (int i = 0; i < this.player.length; i++)
        {
            if (this.player[i] != null)
            {
                if (this.turnRight[i])
                    this.player[i].rotate(1);
                else if (this.turnLeft[i])
                    this.player[i].rotate(-1);

                for (GameAsset item : this.listOfItems)
                    if (this.player[i] != null && this.player[i].checkForCollision(item)) {
                        handleItemCollision(item, (Snake) this.player[i]);
                        this.inactiveFoodNodes.add(item);
                    }


                assert this.player[i] != null;
                if (this.player[i].getTranslateX() < 30 || this.player[i].getTranslateX() > this.WIDTH - 60)
                {
                    System.out.println("PLayer" +
                            (i + 1) +
                            "Hit X Wall");
                    player[i].deactivate();
                }

                if (this.player[i].getTranslateY() < 30 || this.player[i].getTranslateY() > this.HEIGHT - 60)
                {
                    System.out.println("Player" +
                            (i+1) +
                            "Hit Y Wall");
                    player[i].deactivate();
                }

                int j = 0;
                for (SnakeTail tail : ((Snake) this.player[i]).getSnakeTails()) {
                    if (j++ < 100)
                        continue;       // First several SnakeTails always collide with the head

                    if (this.player[i].checkForCollision(tail)) {
                        System.out.printf("Collided with tail number %d\n", tail.id);
                        player[i].deactivate();
                    }
                }
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
        for (int i = 0; i < this.player.length; i++)
        {
            if (this.player[i] != null)
                if (this.player[i].isNoLongerActive())
                {
                    ((Snake) this.player[i]).getSnakeTails().clear();
                    this.player[i] = null;
                }
                else
                    this.player[i].updateAsset();
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
    private void handleItemCollision(GameAsset item, Snake currPlayer)
    {
        if (item instanceof Potion)
            for (int i = 0; i < POTION_LENGTH; i++)
                currPlayer.addTail();
        else if (item instanceof Poison)
            for (int i = 0; i < POISON_LENGTH; i++)
                currPlayer.removeTail();
        else
            for (int i = 0; i < FOOD_LENGTH; i++)
                currPlayer.addTail();

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

    public void start()
    {
        Thread timer = new Thread(this::tickTimer);
        timer.start();
        while (true)
        {
        }
    }

    private void tickTimer()
    {
        long now = System.currentTimeMillis();

        System.out.println("start");
        while (player != null)
        {
            if ((System.currentTimeMillis() - now) > 30) {
                now = System.currentTimeMillis();

                updateDriver();
            }
        }

        System.out.println("GameOver");
    }

    public static void main(String[] args)
    {
        SnakeNetwork test = new SnakeNetwork(4);
        test.init(1111,4,600,800);
        test.start();
    }
}

