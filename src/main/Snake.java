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

import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Snake extends GameAsset{

    /** The x,y velocity and rotation angle of the Snake */
    private double velocityX;
    private double velocityY;
    private double angle;

    /** SnakeTails with references to its parents */
    private LinkedList<SnakeTail> snakeTails;

    /** Number of tails */
    private int numTails;

    public Snake() {
        super(15, Color.RED);
        snakeTails = new LinkedList<>();
        numTails = 0;
    }

    public LinkedList<SnakeTail> getSnakeTails() { return snakeTails; }

    public int getNumTails() { return numTails; }

    /** Add a tail */
    public void addTail() {
        if (snakeTails.size() == 0) {
            // The first SnakeTail attaches to the head
            SnakeTail snakeTail = new SnakeTail(this);
            snakeTails.add(snakeTail);
        } else {
            // Other SnakeTail attach to its most recent SnakeTail
            SnakeTail snakeTail = new SnakeTail(snakeTails.getLast());
            snakeTails.add(snakeTail);
        }
    }

    /** Remove a tail */
    public void removeTail() {
        if (!(snakeTails.size() == 0 || snakeTails.size() == 1)) {
            snakeTails.remove();
        }
    }

    @Override
    public void updateAsset() {
        super.updateAsset();
        for (SnakeTail tail : snakeTails) {
            tail.updateAsset();
        }
    }
}
