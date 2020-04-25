/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/25/20
 * Time: 3:25 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeMain
 *
 * Description:
 *
 * ****************************************
 */
package
        main;

/** Main class that runs the Snake application */
public class MVCSnakeMain {

    /** MVC Snake Controller */
    private MVCSnakeController theController;

    /** MVC Snake View */
    private MVCSnakeView theView;

    /** MVC Snake Model */
    private MVCSnakeModel theModel;

    public static void main(String [] args)
    {
        MVCSnakeController controller = new MVCSnakeController();
        controller.run();

        while (true)
        {
            //needs to keep r6unning so the thread can function properly
        }
    }
}
