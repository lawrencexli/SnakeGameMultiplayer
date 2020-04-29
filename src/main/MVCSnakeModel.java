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
package main;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MVCSnakeModel implements Protocol
{
    private final MVCSnakeController CONTROLLER;
    private Socket socket;
    private Scanner networkIn;
    private PrintStream networkOut;

    protected int height;
    protected int width;
    protected int playerCount;

    private final ArrayList<Circle> itemListPositions;
    private final ArrayList<ArrayList<Circle>> snakeListPositions;
    private final ArrayList<Node> scrapNodes;

    protected volatile boolean gameRunning;

    public MVCSnakeModel(MVCSnakeController controller)
    {
        this.CONTROLLER = controller;
        this.itemListPositions = new ArrayList<>();
        this.snakeListPositions = new ArrayList<>();
        for (int i = 0; i < 4;i++)
            this.snakeListPositions.add(new ArrayList<>());

        this.scrapNodes = new ArrayList<>();
    }

    protected void modelInit(String host, String port)
    {
        try
        {
            this.gameRunning = true;

            this.CONTROLLER.displayMessage("Waiting For More Players...");
            this.socket = new Socket(host, Integer.parseInt(port));
            System.out.println("Connected");

            this.networkIn = new Scanner(this.socket.getInputStream());
            this.networkOut = new PrintStream(this.socket.getOutputStream());

            this.startModel();
        }
        catch (IOException e)
        {
            this.CONTROLLER.displayMessage(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            this.CONTROLLER.displayMessage("Port, Players, Width, And Height Can Only Be Integers");
        }
    }

    private void listener()
    {
        while (gameRunning)
        {
            try
            {
                String input = this.networkIn.nextLine();
                String protocol = input.split(" ")[0];

                System.out.println(input);
                switch (protocol)
                {
                    case DATA:
                        this.updateSnake(input.substring(protocol.length() + 1));
                        break;
                    case START_GAME:
                        this.CONTROLLER.gameGoing = true;
                        this.playerCount = Integer.parseInt(input.substring(protocol.length() + 1));
                        break;
                    default:
                        System.out.println("problem");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.close();
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

    private synchronized void updateSnake(String snakeInfo)
    {
        if (!this.CONTROLLER.dataWrite)
        {
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0 && !positions[0].equals(""))
            {
                setItemPositioning(positions[0].split(";"));
            }

            for (int i = 0; i < this.playerCount; i++)
            {
                if (!(positions[1 + i].equalsIgnoreCase("null")
                        || positions[1 + i].equalsIgnoreCase("")))
                {
                    setSnakePositioning(i, positions[1 + i].split(";"));
                }
            }

            this.CONTROLLER.updateView();
        }
    }

    private void setItemPositioning(String[] itemPos) {
        this.resizeArrayList(itemPos.length, this.itemListPositions);

        for (int i = 0; i < itemPos.length; i++)
        {
            if (this.itemListPositions.get(i) == null)
                this.itemListPositions.set(i, new Circle(10, 10, 10, Color.RED));

            String[] xAndY = itemPos[i].split(",");
            this.itemListPositions.get(i).setTranslateX(Double.parseDouble(xAndY[0]));
            this.itemListPositions.get(i).setTranslateY(Double.parseDouble(xAndY[1]));
            setItemType(i, xAndY);
        }
    }

    private void setItemType(int i, String[] xAndy) {
        switch(Integer.parseInt(xAndy[2]))
        {
            case 0:
                this.itemListPositions.get(i).setFill(Color.RED);
                break;
            case 1:
                this.itemListPositions.get(i).setFill(Color.YELLOWGREEN);
                break;
            case 2:
                this.itemListPositions.get(i).setFill(Color.GREEN);
                break;
        }
    }

    private void setSnakePositioning(int i, String[] snakePos)
    {
        this.resizeArrayList(snakePos.length, this.snakeListPositions.get(i));

        for (int j = 0; j < snakePos.length; j++)
        {
            String[] xYAndRot = snakePos[j].split(",");
            if (this.snakeListPositions.get(i).get(j) == null)
                this.snakeListPositions.get(i).set(j, new Circle(15, 15, 15, getColor(i)));

            this.snakeListPositions.get(i).get(j).setTranslateX(Double.parseDouble(xYAndRot[0]));
            this.snakeListPositions.get(i).get(j).setTranslateY(Double.parseDouble(xYAndRot[1]));
            this.snakeListPositions.get(i).get(j).setRotate(Double.parseDouble(xYAndRot[2]));
        }
    }

    private Color getColor(int player)
    {
        switch (player)
        {
            case 0: return Color.FIREBRICK;
            case 1: return Color.OLDLACE;
            case 2: return Color.GOLD;
            case 3: return Color.PURPLE;
            default:
                return Color.BLACK;
        }
    }

    public void sendDirection(String turn_left, boolean b)
    {
        networkOut.println(turn_left + " " + b);
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

    public void createNetwork(String port,String players,String width,String height)
    {
        try
        {
            this.CONTROLLER.displayMessage("Starting NetWork...");
            int thisPort = Integer.parseInt(port);
            this.playerCount = Integer.parseInt(players);
            this.width = Integer.parseInt(width);
            this.height = Integer.parseInt(height);

            new Thread(() -> new SnakeNetwork(4).init(thisPort, this.playerCount, this.width, this.height)).start();
        }
        catch (NumberFormatException e)
        {
            this.CONTROLLER.displayMessage("Port, Player, Width, and Height Must Be An Integers");
        }
    }

    public void startModel()
    {
        this.CONTROLLER.displayMessage("Waiting For Other Players");
        this.CONTROLLER.getVIEW().getStartMenu().deactivateMenu();

        //game is about to start we can get rid of the menu now
        this.scrapNodes.addAll(this.CONTROLLER.getVIEW().getStartMenu().getChildren());
        this.scrapNodes.add(this.CONTROLLER.getVIEW().getStartMenu());

        new Thread(this::listener).start();
    }

    public ArrayList<Circle> getItemListPositions() {
        return itemListPositions;
    }

    public ArrayList<ArrayList<Circle>> getSnakeListPositions() {
        return snakeListPositions;
    }

    public ArrayList<Node> getScrapNodes() {
        return scrapNodes;
    }

}