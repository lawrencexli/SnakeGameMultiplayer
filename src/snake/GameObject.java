package snake;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObject
{
    Node view;
    Point2D velocity = new Point2D(0,0);

    //protected Node previousView;
    double previousX = 0;
    double previousY = 0;
    double previousAngle = 0;


    boolean turnRight = false;
    boolean turnLeft = false;

    private boolean alive = true;

    public GameObject(Node view)
    {
        this.view = view;
        this.previousX = view.getTranslateX();
        this.previousY = view.getTranslateY();
        this.previousAngle = view.getRotate();
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

    public Point2D getVelocity()
    {
        return velocity;
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

    public void updatePreviousView()
    {
        this.previousX = view.getTranslateX();
        this.previousY = view.getTranslateY();
        this.previousAngle = view.getRotate();
        //this.previousView = this.view;
    }

    public double getRotate()
    {
        return view.getRotate();
    }

    public void rotateRight()
    {
        if (alive)
        {
            view.setRotate(view.getRotate() + 3);
            setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))).multiply(2));
        }
    }

    public void rotateLeft()
    {
        if (alive)
        {
            view.setRotate(view.getRotate() - 3);
            setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))).multiply(2));
        }
    }

    public boolean isColliding(GameObject other)
    {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
