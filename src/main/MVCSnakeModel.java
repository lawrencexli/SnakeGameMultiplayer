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
    //controller
    private Socket socket;
    private Scanner networkIn;
    private PrintStream networkOut;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    public MVCSnakeModel()
    {
        try
        {
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
        while (true)
        {
            String input = this.networkIn.nextLine();
            String protocol = input.split(" ")[0];

            switch (protocol)
            {
                default:
                    this.updateSnake("PROTOCOL " + input.substring(protocol.length()));
            }
        }
    }

    private void updateSnake(String snakeInfo)
    {
        this.networkOut.println(snakeInfo);
    }


    private void run()
    {
        new Thread(this::listener).start();

        while (true)
        {
            //run forever for now...
        }
    }

    public static void main(String[] args)
    {
        MVCSnakeModel model = new MVCSnakeModel();
        model.run();
    }
}
