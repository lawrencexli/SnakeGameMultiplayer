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

import javafx.scene.shape.Rectangle;

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



    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    public MVCSnakeModel(MVCSnakeController controller)
    {
        try
        {
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

        while (true)
        {

            try
            {
                String input = this.networkIn.nextLine();
                String protocol = input.split(" ")[0];

                //System.out.println(input);
                switch (protocol)
                {
                    default:
                        this.updateSnake(input.substring(protocol.length() + 1));
                }

                if (itemPrev != this.controller.getItemListPositions().size())
                {
                    System.out.println("ITEM -> " + this.controller.getItemListPositions().size());
                    itemPrev = this.controller.getItemListPositions().size();
                }

                if (snakePrev != this.controller.getSnakeListPositions().size())
                {
                    System.out.println("SNAKE -> " + this.controller.getSnakeListPositions().size());
                    snakePrev = this.controller.getSnakeListPositions().size();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private void updateSnake(String snakeInfo)
    {

            //this.networkOut.println("PROTOCOL " + snakeInfo);
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0)
            {
                String[] itemPos = positions[0].split(";");

                if (itemPos.length < this.controller.getItemListPositions().size())
                    while (itemPos.length < this.controller.getItemListPositions().size())
                        this.controller.getItemListPositions().remove(0);
                else if (itemPos.length > this.controller.getItemListPositions().size())
                    while (itemPos.length > this.controller.getItemListPositions().size())
                        this.controller.getItemListPositions().add(new double[2]);

                for (int i = 0; i < itemPos.length; i++) {
                    String[] xAndy = itemPos[i].split(",");
                    this.controller.getItemListPositions().get(i)[0] = Double.parseDouble(xAndy[0]);
                    this.controller.getItemListPositions().get(i)[0] = Double.parseDouble(xAndy[1]);
                }
            }

            if (positions.length > 1)
            {
                String[] snakePos = positions[1].split(";");

                if (snakePos.length < this.controller.getSnakeListPositions().size())
                    while (snakePos.length < this.controller.getSnakeListPositions().size())
                        this.controller.getSnakeListPositions().remove(0);
                else if (snakePos.length > this.controller.getSnakeListPositions().size())
                    while (snakePos.length > this.controller.getSnakeListPositions().size())
                        this.controller.getSnakeListPositions().add(new double[2]);

                for (int i = 0; i < snakePos.length; i++) {
                    String[] xYAndRot = snakePos[i].split(",");
                    this.controller.getItemListPositions().get(i)[0] = Double.parseDouble(xYAndRot[0]);
                    this.controller.getItemListPositions().get(i)[0] = Double.parseDouble(xYAndRot[1]);
                    this.controller.getItemListPositions().get(i)[0] = Double.parseDouble(xYAndRot[2]);
                }
            }
    }


    void runListener()
    {
        new Thread(this::listener).start();

        while (true)
        {
            //run forever for now...
        }
    }

    public static void main(String[] args)
    {
        MVCSnakeModel model = new MVCSnakeModel(null);
        model.runListener();
    }
}
