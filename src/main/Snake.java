/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/16/20
 * Time: 11:44 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: Snake
 *
 * Description:
 * The Snake class
 * ****************************************
 */
package
        main;

public class Snake extends GameAsset{

    /**the x velocity this object is moving in, 0 if stationary*/
    private double velocityX;

    /**the y velocity this object is moving in, 0 if stationary*/
    private double velocityY;

    public Snake() {
        super();
    }

    /**
     * Override methods for snake: sets the x and y values in the obejct velocity
     *
     * @param velocityX - a double representing the objects x velocity
     * @param velocityY - a double representing the objects y velocity
     * @author Christopher Asbrock
     */
    @Override
    public void setVelocity(double velocityX, double velocityY) {
        super.setVelocity(velocityX, velocityY);
    }

    /**
     * Override methods for snake: run by the games animation timer 60 times a second to update the object position
     * based on its velocity
     *
     * @author Christopher Asbrock
     */
    @Override
    public void updateAsset() {
        super.updateAsset();
    }

    /**
     * Override methods for snake: adjusts the velocity to be directed based on rotation
     *
     * @param direction - RIGHT or LEFT
     * @author Christopher Asbrock
     */
    @Override
    public void rotate(int direction) {
        super.rotate(direction);
    }


}
