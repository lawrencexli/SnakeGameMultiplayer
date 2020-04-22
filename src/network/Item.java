/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/16/20
 * Time: 12:01 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: Item
 *
 * Description:
 * Item class for the snake game
 * ****************************************
 */
package
        network;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import network.GameAsset;

/**
 * A class that represent the items in the snake game
 */
public class Item extends GameAsset {

    /** Location as 2D coordinates */
    Point2D location;

    /** Name of the item */
    String name;

    public Item(int x, Color color, String name) {
        super(x,color);
        this.location = location;
        this.name = name;
    }

    public Point2D getLocation() { return location; }
    public String getName() { return name; }
}

/** Food inherited from Item */
class Food extends Item {

    public Food(int x, Color color) {
        super(x,color,"Food");
    }
}

/** Potion inherited from Item */
class Potion extends Item {

    public Potion(int x,  Color color) {
        super(x,color,"Potion");
    }
}

/** Poison inherited from Item */
class Poison extends Item {

    public Poison(int x, Color color) {
        super(x,color,"Poison");
    }
}


