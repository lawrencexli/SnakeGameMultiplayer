/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/21/20
 * Time: 11:19 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main.network
 * Class: NetworkTest
 *
 * Description:
 *
 * ****************************************
 */
package main.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkTest
{
    public static void main(String[] args)
    {
        Socket socket = null;
        Scanner scanner;
        PrintStream printStream;



        try
        {
            socket = new Socket("localhost", 1111);

            scanner = new Scanner(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());

            while (true)
            {
                System.out.println(scanner.nextLine());
            }






        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}