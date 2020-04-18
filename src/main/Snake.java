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

import javafx.scene.shape.Circle;

import java.util.Collections;
import java.util.LinkedList;

public class Snake extends GameAsset {

    /**the x velocity this object is moving in, 0 if stationary*/
    private double velocityX;

    /**the y velocity this object is moving in, 0 if stationary*/
    private double velocityY;

    private LinkedList<Circle> snakeBody;

    public Snake() {
        super();
        snakeBody = new LinkedList<>(Collections.singletonList(new Circle()));
    }

    public double getVelocityX() { return velocityX; }

    public double getVelocityY() { return velocityY; }

    public LinkedList<Circle> getSnakeBody() { return snakeBody; }

    public void addBodyLength() {
        this.snakeBody.addFirst(new Circle());
    }

    public void removeBodyLength() {
        if (snakeBody.size() == 1) {
            System.out.println("The snake size is already 1!");
            return;
        }
        this.snakeBody.removeLast();
    }

    public Circle getHead() {
        return this.snakeBody.getFirst();
    }

    public Circle getTail() {
        return this.snakeBody.getLast();
    }

    /**
     * sets the x and y values in the obejct velocity
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
     * run by the games animation timer 60 times a second to update the object position
     * based on its velocity
     *
     * @author Christopher Asbrock
     */
    @Override
    public void updateAsset() {
        super.updateAsset();
    }

    /**
     * adjusts the velocity to be directed based on rotation
     *
     * @param direction - RIGHT or LEFT
     * @author Christopher Asbrock
     */
    @Override
    public void rotate(int direction) {
        super.rotate(direction);
    }
}
