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
        main;

/**
 * A class that represent the items in the snake game
 *
 * @author: Lawrence
 */
public class Item {

    /** Name of the item */
    String name;

    /** Location */
    Double[] location;

    public Item(String name, Double[] location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }

    public Double[] getLocation() { return location; }
}

/** Food inherited from Item */
class Food extends Item {

    public Food(Double[] location) {
        super("Food",location);
    }
}

/** Potion inherited from Item */
class Potion extends Item {

    public Potion(Double[] location) {
        super("Potion",location);
    }
}

/** Poison inherited from Item */
class Poison extends Item {

    public Poison(Double[] location) {
        super("Poison",location);
    }
}


