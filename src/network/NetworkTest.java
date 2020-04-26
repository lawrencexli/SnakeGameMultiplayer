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
 * Package: network
 * Class: NetworkTest
 *
 * Description:
 *
 * ****************************************
 */
package network;

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
                String[]test = scanner.nextLine().split("%");
                for (int i = 0; i < test.length; i++)
                {
                    if (i > 0)
                        System.out.print("\t");
                    System.out.println(test[i]);
                }
                System.out.println(scanner.nextLine());

                printStream.println("PROTOCOL some type of message here");
            }








        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}