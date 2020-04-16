/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/14/20
 * Time: 8:00 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: snake
 * Class: SnakeAppController
 *
 * Description:
 *
 * ****************************************
 */
package snake;

public class SnakeAppController
{
    boolean leftTurn;
    boolean rightTurn;

    String snakePartPositions;

    SnakeAppModel model;
    SnakeAppView view;

    public SnakeAppController(SnakeAppView view)
    {
        leftTurn = false;
        rightTurn = false;

        snakePartPositions = "";

        this.view = view;

        model = new SnakeAppModel();
    }

    public void launch(String[] args)
    {
        this.model.launchMain(args);
    }
    /*
    public static void main(String [] args)
    {
        SnakeAppController controller = new SnakeAppController(null);
        //new Thread(() -> controller.displayed()).start();
        controller.launch(args);
    }
     */

    public void controllerUpdate()
    {
        if (this.model != null && this.model.snakeTest != null)
        {
            this.snakePartPositions = "";

            for (int i =0; i < ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.size(); i++)
            {
                this.snakePartPositions +=
                        ((((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateX())
                + "," + (((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateY())
                + "," +  (((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getRotate()));
                this.snakePartPositions += (i == ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.size() - 1) ? "" :  ";";
            }
                /*
                if (this.snakePartPositions.size() < i)
                {
                    this.snakePartPositions.get(i)[0] = ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateX();
                    this.snakePartPositions.get(i)[1] = ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateY();
                    this.snakePartPositions.get(i)[2] = ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getRotate();
                }
                else
                {
                    this.snakePartPositions.add(new double[]
                            {
                                    ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateX(),
                                    ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getTranslateY(),
                                    ((SnakeDriver.Snake) this.model.snakeTest).bodyParts.get(i).getView().getRotate()
                            });
                }
            }*/
        }

        //System.out.println(this.snakePartPositions);
    }
}