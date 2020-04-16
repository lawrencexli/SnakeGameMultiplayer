package snake;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

import java.util.Random;

public class GameObject
{
    Node view;
    Point2D velocity = new Point2D(0,0);

    boolean turnRight = false;
    boolean turnLeft = false;

    private boolean alive = true;

    public GameObject(Node view)
    {
        this.view = view;
        //this.previousView = this.view;
    }

    public void setVelocity(Point2D velocity)
    {
        this.velocity = velocity;
    }

    public Node getView()
    {
        return view;
    }

    public boolean isAlive()
    {
        return this.alive;
    }

    public boolean isEaten()
    {
        return !alive;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    public void update()
    {
        if (turnRight)
            this.rotateRight();
        else if(turnLeft)
            this.rotateLeft();

        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public void reverse()
    {
        view.setRotate(view.getRotate() + rand.nextInt(30) + 75);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))).multiply(2));
    }

    public double getRotate()
    {
        return view.getRotate();
    }

    Random rand = new Random();

    public void rotateRight()
    {
        if (alive)
        {

            view.setRotate(view.getRotate() + 3);
            //view.setRotate(view.getRotate() + rand.nextInt(3) + 1);
            setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))).multiply(1.6));
        }
    }

    public void rotateLeft()
    {
        if (alive)
        {
            view.setRotate(view.getRotate() - 3);
            //view.setRotate(view.getRotate() - rand.nextInt(3) + 1);
            setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))).multiply(1.6));
        }
    }

//    public boolean isColliding(GameObject other)
//    {
//        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
//    }

    public boolean isColliding(GameObject other)
    {
        return Shape.intersect((Shape) this.getView(), (Shape) other.getView()).getBoundsInParent().getWidth() > 0;
    }

    public boolean collidingWithPoint(GameObject other)
    {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
