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

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import network.SnakeUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MVCSnakeModel
{
    private MVCSnakeController controller;
    private Socket socket;
    private Scanner networkIn;
    private PrintStream networkOut;

    protected boolean gameRunning;

    public MVCSnakeModel(MVCSnakeController controller)
    {
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
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0 && !positions[0].equals(""))
            {
                String[] itemPos = positions[0].split(";");

                controller.resizeArrayList(itemPos.length, this.controller.getItemListPositions());

                for (int i = 0; i < itemPos.length; i++)
                {
                    if (this.controller.getItemListPositions().get(i) == null)
                        this.controller.getItemListPositions().set(i, new Circle(5,5,5, Color.RED));

                    String[] xAndy = itemPos[i].split(",");
                    this.controller.getItemListPositions().get(i).setTranslateX(Double.parseDouble(xAndy[0]));
                    this.controller.getItemListPositions().get(i).setTranslateY(Double.parseDouble(xAndy[1]));
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

                    controller.resizeArrayList(snakePos.length, this.controller.getSnakeListPositions());

                    for (int i = 0; i < snakePos.length; i++)
                    {
                        String[] xYAndRot = snakePos[i].split(",");
                        if (this.controller.getSnakeListPositions().get(i) == null)
                            this.controller.getSnakeListPositions().set(i, new Circle(15,15,15,Color.GOLD));

                        this.controller.getSnakeListPositions().get(i).setTranslateX(Double.parseDouble(xYAndRot[0]));
                        this.controller.getSnakeListPositions().get(i).setTranslateY(Double.parseDouble(xYAndRot[1]));
                        this.controller.getSnakeListPositions().get(i).setRotate(Double.parseDouble(xYAndRot[2]));
                    }
                }
            }

            this.controller.updateView();
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
}