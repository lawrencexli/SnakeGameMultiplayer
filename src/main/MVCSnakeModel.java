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

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MVCSnakeModel
{
    private MVCSnakeController controller;
    private Socket socket;
    private Scanner networkIn;
    private PrintStream networkOut;

    public ArrayList<Circle> getItemListPositions() {
        return itemListPositions;
    }

    public ArrayList<Circle> getSnakeListPositions() {
        return snakeListPositions;
    }

    public ArrayList<Node> getScrapNodes() {
        return scrapNodes;
    }

    private ArrayList<Circle> itemListPositions;
    private ArrayList<Circle> snakeListPositions;
    private ArrayList<Node> scrapNodes;

    protected boolean gameRunning;

    public MVCSnakeModel(MVCSnakeController controller)
    {
        this.itemListPositions = new ArrayList<>();
        this.snakeListPositions = new ArrayList<>();
        this.scrapNodes = new ArrayList<>();
        try
        {
            this.gameRunning = true;
            this.controller = controller;

            this.socket = new Socket("localhost", 1111);
            System.out.println("Connected");

            this.networkIn = new Scanner(this.socket.getInputStream());
            this.networkOut = new PrintStream(this.socket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void listener()
    {
        int itemPrev = 0;
        int snakePrev = 0;

        while (gameRunning)
        {
            try
            {
                String input = this.networkIn.nextLine();
                String protocol = input.split(" ")[0];

                switch (protocol)
                {
                    default:
                        this.updateSnake(input.substring(protocol.length() + 1));
                }
/*
                if (itemPrev != this.controller.getItemListPositions().size())
                {
                    System.out.println("ITEM -> " + this.controller.getItemListPositions());
                    itemPrev = this.controller.getItemListPositions().size();
                }

                if (snakePrev != this.controller.getSnakeListPositions().size())
                {
                    System.out.println("SNAKE -> " + this.controller.getSnakeListPositions());
                    snakePrev = this.controller.getSnakeListPositions().size();
                }

 */
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private synchronized void updateSnake(String snakeInfo)
    {
        if (!this.controller.dataWrite)
        {
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0 && !positions[0].equals("")) {
                String[] itemPos = positions[0].split(";");

                this.resizeArrayList(itemPos.length, this.itemListPositions);

                for (int i = 0; i < itemPos.length; i++) {
                    if (this.itemListPositions.get(i) == null)
                        this.itemListPositions.set(i, new Circle(10, 10, 10, Color.RED));

                    String[] xAndy = itemPos[i].split(",");
                    this.itemListPositions.get(i).setTranslateX(Double.parseDouble(xAndy[0]));
                    this.itemListPositions.get(i).setTranslateY(Double.parseDouble(xAndy[1]));
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
            }

            if (positions.length > 1)
            {
                if (positions[1].equalsIgnoreCase("null"))
                {
                    this.gameRunning = false;
                }
                else
                {
                    String[] snakePos = positions[1].split(";");

                    this.resizeArrayList(snakePos.length, this.snakeListPositions);

                    for (int i = 0; i < snakePos.length; i++) {
                        String[] xYAndRot = snakePos[i].split(",");
                        if (this.snakeListPositions.get(i) == null)
                            this.snakeListPositions.set(i, new Circle(15, 15, 15, Color.GOLD));

                        this.snakeListPositions.get(i).setTranslateX(Double.parseDouble(xYAndRot[0]));
                        this.snakeListPositions.get(i).setTranslateY(Double.parseDouble(xYAndRot[1]));
                        this.snakeListPositions.get(i).setRotate(Double.parseDouble(xYAndRot[2]));
                    }
                }
            }

            this.controller.updateView();
        }
    }

    public void sendDirection(String turn_left, boolean b)
    {
        networkOut.println(turn_left + " " + b);
    }


    void runListener()
    {
        new Thread(this::listener).start();

        while (gameRunning)
        {
            //run forever for now...
        }

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

    public static void main(String[] args)
    {
        MVCSnakeModel model = new MVCSnakeModel(null);
        model.runListener();
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
}