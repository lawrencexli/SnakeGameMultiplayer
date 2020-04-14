/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/11/20
 * Time: 7:23 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: snake
 * Class: Player
 *
 * Description:
 *
 * ****************************************
 */
package snake;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.function.Consumer;

public interface SnakeDriver
{
    class Snake extends GameObject
    {
        private final Pane root;

        int timer = 0;
        LinkedList<GameObject> bodyParts;

        Snake(Pane root)
        {
            super(new Circle(15, 15,15, Color.BLUE));
            this.root = root;

            bodyParts = new LinkedList<>();
            bodyParts.add(new SnakeApp.SnakePart(null, this));

        }

        void addPart(GameObject lastTail)
        {
            ((Circle)lastTail.view).setFill(new ImagePattern(new Image("snakeHead.png")));

            if (this.bodyParts.size() > 0)
            {
                ((SnakeApp.SnakePart)this.bodyParts.getFirst()).parent = lastTail;

                if (this.bodyParts.size() == 2 || this.bodyParts.size() == 1 || this.bodyParts.size() == 3 )
                    ((Circle) ((SnakeApp.SnakePart) this.bodyParts.getFirst()).view).setFill(new ImagePattern(new Image("snakeTail.png")));
                else
                    ((Circle) ((SnakeApp.SnakePart) this.bodyParts.getFirst()).view).setFill(new ImagePattern(new Image("snakeBody.png")));
            }

            this.bodyParts.addFirst(lastTail);
        }

        void stackForEach(Consumer<? super GameObject> action)
        {
            this.bodyParts.forEach(action);
        }

        boolean checkForTail()
        {
            int count = 0;

            for (int i = 0 ; i < this.bodyParts.size(); i++)
            {
                if (i > 30)// && i % 10 == 0)
                {
                    //this.bodyParts.get(i).getView().setScaleX(2);
                    //this.bodyParts.get(i).getView().setScaleY(2);
                    if (this.isColliding(this.bodyParts.get(i)))
                        count++;
                        //this.bodyParts.get(i).getView().setScaleY(1.5);
                        //this.setAlive(false);
                }
            }

            return count > 6;
        }

        @Override
        public void update()
        {
            if (this.isAlive())
            {
                super.update();
            }
            else
            {
                view.setOpacity(.3);
                for(GameObject tail : bodyParts)
                    tail.view.setOpacity(.3);

                view.setTranslateX(view.getTranslateX());
                view.setTranslateY(view.getTranslateY());

                timer++;

                if (timer > 1)
                {
                    if (this.bodyParts.size() != 0)
                        root.getChildren().removeAll(this.bodyParts.pop().getView());
                    else
                        root.getChildren().removeAll(this.view);

                    this.timer = 0;
                }
            }
        }
    }

    class SnakePart extends GameObject
    {
        int tailNumber;
        int timer = 0;
        GameObject parent;
        GameObject head;

        double previousX;
        double previousY;
        double previousAngle;

        SnakePart(GameObject parent, GameObject head)
        {
            super(new Circle(15, 15,15, Color.BLUE));
            //super(new Rectangle(30,30));
            ((Circle)this.view).setFill(new ImagePattern(new Image("snakeTail.png")));

            this.previousX = -60;
            this.previousY = -60;
            this.previousAngle = 0;
            this.parent = parent;
            this.head = head;
            this.tailNumber = 0;
        }

        public void updatePreviousView()
        {
            this.previousX = view.getTranslateX();
            this.previousY = view.getTranslateY();
            this.previousAngle = view.getRotate();
            //this.previousView = this.view;
        }

        @Override
        public void update()
        {
            if (head.isAlive())
            {
                if (parent == null)
                {
                    view.setTranslateX(head.view.getTranslateX());
                    view.setTranslateY(head.view.getTranslateY());
                    view.setRotate(head.view.getRotate());

                    timer++;

                    //if (timer == 10)
                    {
                        this.updatePreviousView();
                        this.timer = 0;
                    }
                }
                else
                {
                    view.setTranslateX(((SnakePart)parent).previousX);
                    view.setTranslateY(((SnakePart)parent).previousY);
                    view.setRotate(((SnakePart)parent).previousAngle);

                    timer++;

                    if (timer > 3)
                    {
                        this.updatePreviousView();
                        this.timer = 0;
                    }
                }
            }
        }
    }


}