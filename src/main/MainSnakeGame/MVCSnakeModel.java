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
package main.MainSnakeGame;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import main.CommonInterfaces.GameCommonIndexes;
import main.CommonInterfaces.Protocol;
import main.Exception.SnakeException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MVCSnakeModel implements Protocol, GameCommonIndexes
{
    /***/
    private final MVCSnakeController CONTROLLER;

    /***/
    private Socket socket;
    /***/
    private Scanner networkIn;
    /***/
    private PrintStream networkOut;

    /***/
    protected int height;
    /***/
    protected int width;
    /***/
    protected int playerCount;

    private Circle snakeDefaultCircle = new Circle(SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE);

    /**an arraylist holding the positions of all active items*/
    private final ArrayList<Circle> itemListPositions;
    /**An array of the max players, that holds a reference to each of there ArrayList kept positions*/
    private final ArrayList<Circle>[] snakeListPositions;
    /**An array list for reference in the view that notes and nodes taken out of game for removal*/
    private final ArrayList<Node> scrapNodes;

    /***/
    protected boolean gameRunning;

    public MVCSnakeModel(MVCSnakeController controller)
    {
        this.CONTROLLER = controller;
        this.itemListPositions = new ArrayList<>();
        this.snakeListPositions = new ArrayList[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS;i++)
            this.snakeListPositions[i] = new ArrayList<>();

        this.scrapNodes = new ArrayList<>();
    }

    protected void modelInit(String host, String port)
    {
        try
        {
            this.gameRunning = true;

            this.CONTROLLER.displayMenuMessage("Waiting For More Players...");
            this.socket = new Socket(host, Integer.parseInt(port));
            System.out.println("Connected");

            this.networkIn = new Scanner(this.socket.getInputStream());
            this.networkOut = new PrintStream(this.socket.getOutputStream());

            this.startModel();
        }
        catch (IOException e)
        {
            this.CONTROLLER.displayMenuMessage(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            this.CONTROLLER.displayMenuMessage("Port, Players, Width, And Height Can Only Be Integers");
        }
    }

    private void listener()
    {
        try
        {
            while (true)
            {
                if (!this.networkIn.hasNextLine())
                    throw new SnakeException("Disconnected From Server");

                String input = this.networkIn.nextLine();
                String protocol = getProtocol(input);
                String message = getMessage(protocol, input);

                switch (protocol)
                {
                    case DATA:
                        this.updateSnake(message);
                        break;
                    case START_GAME:
                        this.CONTROLLER.gameGoing = true;
                        this.playerCount = Integer.parseInt(message);
                        break;
                    case MESSAGE:
                        this.CONTROLLER.displayGameMessage(message);
                        break;
                    case END_GAME:
                        throw new SnakeException(message);
                    default:
                        System.out.println("problem");
                }
            }
        }
        catch (SnakeException e)
        {
            this.CONTROLLER.displayGameMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        finally
        {
            this.close();
        }

    }

    private void close()
    {
        try
        {
            this.networkOut.close();
            this.networkIn.close();
            this.socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Model closed down");
    }

    private void updateSnake(String snakeInfo)
    {
        if (!this.CONTROLLER.dataWrite)
        {
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0 && !positions[ITEM].equals(""))
                setItemPositioning(positions[ITEM].split(";"));

            for (int i = 0; i < this.playerCount; i++)
                if (!(positions[SNAKE + i].equalsIgnoreCase("null")
                        || positions[SNAKE + i].equalsIgnoreCase("")))
                    setSnakePositioning(i, positions[SNAKE + i].split(";"));

            for (int i = 0; i < this.playerCount; i++)
                if (this.snakeListPositions[i].size() < 100)
                    this.snakeShaperSmall(i);
                else
                    this.snakeShaper(i);

            this.CONTROLLER.updateView();
        }
    }

    private void setItemPositioning(String[] itemPos)
    {
        this.resizeArrayList(itemPos.length, this.itemListPositions);

        for (int i = 0; i < itemPos.length; i++)
        {
            if (this.itemListPositions.get(i) == null)
                this.itemListPositions.set(i, new Circle(ITEM_SIZE, ITEM_SIZE, ITEM_SIZE, Color.RED));

            String[] xAndY = itemPos[i].split(",");
            this.itemListPositions.get(i).setTranslateX(Double.parseDouble(xAndY[X_POSITION]));
            this.itemListPositions.get(i).setTranslateY(Double.parseDouble(xAndY[Y_POSITION]));
            setItemType(i, Integer.parseInt(xAndY[ITEM_TYPE]));
        }
    }

    private void setItemType(int index, int type)
    {
        switch(type)
        {
            case FOOD:
                this.itemListPositions.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/foodSprite.png")));
                break;
            case POTION:
                this.itemListPositions.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/potionSprite.png")));
                break;
            case POISON:
                this.itemListPositions.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/poisonSprite.png")));
                break;
        }
    }

    private void setSnakePositioning(int i, String[] snakePos)
    {
        this.resizeArrayList(snakePos.length, this.snakeListPositions[i]);

        boolean colorSwap = true;

        for (int j = 0; j < snakePos.length; j++)
        {
            String[] xYAndRot = snakePos[j].split(",");
            if (this.snakeListPositions[i].get(j) == null)
                this.snakeListPositions[i].set(j, new Circle(15, 15, 15, getColor(i, colorSwap)));

            this.snakeListPositions[i].get(j).setTranslateX(Double.parseDouble(xYAndRot[X_POSITION]));
            this.snakeListPositions[i].get(j).setTranslateY(Double.parseDouble(xYAndRot[Y_POSITION]));
            this.snakeListPositions[i].get(j).setRotate(Double.parseDouble(xYAndRot[ROTATION]));

            colorSwap = !colorSwap;
        }
    }

    private Color getColor(int player, boolean swap)
    {
        switch (player)
        {
            case PLAYER_ONE: return (swap) ? Color.DARKRED : Color.RED;
            case PLAYER_TWO: return (swap) ?  Color.DARKGREEN : Color.GREEN;
            case PLAYER_THREE: return (swap) ?  Color.DARKBLUE : Color.BLUE;
            case PLAYER_FOUR: return (swap) ?  Color.DARKGOLDENROD : Color.GOLDENROD;
            default:
                return Color.BLACK;
        }
    }

    private void snakeShaperSmall(int snake)
    {
        double normalSize = this.snakeDefaultCircle.getScaleX();

        int partitionSize = (this.snakeListPositions[snake].size() / 2);
        int partitionIndex = 0;

        double count = 0;
        double middleIndexSize = 0;

        for (int i = 0; i < this.snakeListPositions[snake].size(); i++)
        {
            switch (partitionIndex)
            {
                case 0:
                    if (count < partitionSize * .2)
                        middleIndexSize = this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                (normalSize - (normalSize * (count/ partitionSize))));
                    else
                        this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                middleIndexSize);
                    break;
                case 2:
                    if (count < partitionSize * .4)
                        this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                middleIndexSize);
                    else
                        this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                Math.max((normalSize - (normalSize * (1 - (count / partitionSize)))), middleIndexSize));
                    break;
                case 1:
                    this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                            Math.min((normalSize - normalSize * (count / (partitionSize))), middleIndexSize));
                    break;
                default:
                    this.snakeListPositions[snake].get(i).setFill(Color.BLACK);
            }

            if (count++ == partitionSize)
            {
                count = 0;
                partitionIndex++;
            }

            setHead(snake, i);
        }
    }

    private void snakeShaper(int snake)
    {
        double normalSize = this.snakeDefaultCircle.getScaleX();

        int partitionSize = (this.snakeListPositions[snake].size() / 9);
        int partitionIndex = 0;
        double count = 0;

        double middleIndexSize = 0;
        double middleSectionIndex = 0;
        double lastSectionIndex = 0;

        for (int i = 0; i < this.snakeListPositions[snake].size(); i++)
        {
            switch (partitionIndex)
            {
                case 0:
                    if (count < (partitionSize * .1))
                        middleIndexSize = this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                (normalSize - (normalSize * (count/ partitionSize))));
                    else
                        this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                middleIndexSize);
                    break;
                case 1:
                        this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                                Math.max((normalSize - (normalSize * (1 - (count / partitionSize)))), middleIndexSize));
                    break;
                case 2:
                case 3:
                case 4:
                    this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                            normalSize + (normalSize * (.1 * (middleSectionIndex++ / partitionSize))));
                    break;
                case 5:
                    this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                            normalSize + (normalSize * (.3 * (1 - count / partitionSize))));
                    break;
                case 6:
                case 7:
                case 8:
                    this.setSnakePartScale(this.snakeListPositions[snake].get(i),
                            normalSize - normalSize * (lastSectionIndex++ / (partitionSize * 3)));
                    break;
                default:
                    this.snakeListPositions[snake].get(i).setFill(Color.BLACK);
            }

            if (count++ == partitionSize)
            {
                count = 0;
                partitionIndex++;
            }

            setHead(snake, i);
        }
    }

    private void setHead(int snake, int i)
    {
        if (i == 0)
        {
            this.snakeListPositions[snake].get(i).setScaleX(1.3);
            this.snakeListPositions[snake].get(i).setScaleY(1.3);
            this.snakeListPositions[snake].get(i).setFill(new ImagePattern(new Image("file:src/main/sprites/player" + (snake + 1) + "Head.png")));
        }
    }

    private double setSnakePartScale(Circle part, double scale)
    {
        part.setScaleX(scale);
        part.setScaleY(scale);
        return scale;
    }

    public void sendDirection(String protocol, boolean onOff)
    {
        networkOut.println(protocol + " " + onOff);
    }

    public synchronized void resizeArrayList(int size, List<Circle> list)
    {
        if (list.size() < size)
            while (list.size() < size)
                list.add(null);
        else if (list.size() > size)
            while (list.size() > size)
                this.scrapNodes.add(list.remove(0));
    }

    public void createNetwork(String port,String players,String width,String height) throws NumberFormatException, SnakeException
    {
        this.CONTROLLER.displayMenuMessage("Starting NetWork...");
        int thisPort = Integer.parseInt(port);
        this.playerCount = Integer.parseInt(players);
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);

        checkPlayerCount();
        checkPort(thisPort);

        this.startHostServer(thisPort);
    }

    private void checkPlayerCount() throws SnakeException
    {
        if (this.playerCount < 1)
            throw new SnakeException("Must Be At Least One Player");
        else if (this.playerCount > 4)
            throw new SnakeException("4 Players Max");
    }

    private void checkPort(int thisPort) throws SnakeException
    {
        if (thisPort < 1000)
            throw new SnakeException("Port Must Be Greater Then 1000");
        else if (thisPort > 65535)
            throw new SnakeException("Port Max 65535");
    }

    private void startHostServer(int thisPort)
    {
        new Thread(() ->
        {
            try
            {
                new SnakeNetwork().init(thisPort, this.playerCount, this.width, this.height);
            }
            catch (IOException e)
            {
                this.CONTROLLER.displayMenuMessage("Server Failed To Start");
                System.exit(-1);
            }
        }).start();
    }

    public void startModel()
    {
        this.CONTROLLER.displayMenuMessage("Waiting For Other Players");
        this.CONTROLLER.getVIEW().getStartMenu().deactivateMenu();

        //game is about to start we can get rid of the menu now
        this.scrapNodes.addAll(this.CONTROLLER.getVIEW().getStartMenu().getChildren());
        this.scrapNodes.add(this.CONTROLLER.getVIEW().getStartMenu());

        new Thread(this::listener).start();
    }

    public ArrayList<Circle> getItemListPositions()
    {
        return itemListPositions;
    }

    public ArrayList<Circle>[] getSnakeListPositions()
    {
        return snakeListPositions;
    }

    public ArrayList<Node> getScrapNodes()
    {
        return scrapNodes;
    }

}