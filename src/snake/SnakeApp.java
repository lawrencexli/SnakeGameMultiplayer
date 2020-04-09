package snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;

public class SnakeApp extends Application
{
    private Pane root;

    private ArrayList<GameObject> foodList = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();

    private GameObject player;

    @Override
    public void start(Stage stage)
    {
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e ->
        {
            if (e.getCode() == KeyCode.RIGHT)
                player.rotateLeft();
            else if (e.getCode() == KeyCode.LEFT)
                player.rotateRight();
        });
        stage.show();
    }

    private Parent createContent()
    {
        root = new Pane();
        root.setPrefSize(800, 600);

        Wall wall = new Wall(new Rectangle(30, root.getPrefHeight(), Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,0);

        wall = new Wall(new Rectangle(30, root.getPrefHeight(), Color.GRAY));
        walls.add(wall);
        addGameObject(wall, root.getPrefWidth() - 30 ,0);

        wall = new Wall(new Rectangle(root.getPrefWidth(), 30, Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,0);

        wall = new Wall(new Rectangle(root.getPrefWidth(), 30, Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,root.getPrefHeight() - 30);


        player = new Player();
        player.setVelocity(new Point2D(1,0));
        addGameObject(player, root.getPrefWidth()/2, root.getPrefHeight()/2);


        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                updateView();
            }
        };

        timer.start();

        return root;
    }


    private void addFood(GameObject food, double x, double y)
    {
        int randomNum =  new Random().nextInt(3);
        switch (randomNum)
        {
            case 0: ((Circle) food.view).setFill(new ImagePattern(new Image("apple.png")));
                break;
            case 1: ((Circle) food.view).setFill(new ImagePattern(new Image("hamburger.png")));
                break;
            case 2: ((Circle) food.view).setFill(new ImagePattern(new Image("fries.png")));
                break;
        }
        foodList.add(food);
        addGameObject(food, x, y);
    }

    private void addGameObject(GameObject object, double x, double y)
    {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void updateView()
    {
        for (GameObject food : foodList)
        {
            if (player.isColliding(food))
            {
                food.setAlive(false);
                root.getChildren().removeAll(food.getView());

                Tail newTail = new Tail(((Player)player).getLastTail(), player);
                ((Player) player).setLastTail(newTail);
                addGameObject(newTail , player.getView().getTranslateX(), player.getView().getTranslateY());

            }
        }

        for (GameObject wall : walls)
        {
            if (player.isColliding(wall))
            {
                player.setAlive(false);
            }
        }

        if (((Player) player).checkForTail())
        {
            player.setAlive(false);
        }

        foodList.removeIf(GameObject::isEaten);

        walls.forEach(GameObject::update);

        foodList.forEach(GameObject::update);
        player.update();
        ((Player) player).stackForEach(GameObject::update);

        if (Math.random() < 0.01)
        {
            addFood(new Food(),
                    30 + ((Math.random() * (root.getPrefWidth() - 60))),
                    30 + ((Math.random() * (root.getPrefHeight() - 60))));
        }
    }

    private class Wall extends GameObject
    {
        Stack<GameObject> tails;

        Wall(Rectangle shape)
        {
            super(shape);
            tails = new Stack<>();
        }
    }

    private class Player extends GameObject
    {
        int timer = 0;
        Stack<GameObject> tails;

        Player()
        {
            super(new Rectangle(30, 30, Color.BLUE));
            tails = new Stack<>();

            Rectangle rectangle = new Rectangle(30,30);
            rectangle.setFill(new ImagePattern(new Image("test.jpg")));
            this.view = rectangle;
            this.previousX = view.getTranslateX();
            this.previousY = view.getTranslateY();
            this.previousAngle = view.getRotate();
        }

        void setLastTail(GameObject lastTail)
        {
            System.out.println(this.tails.size());
            if (lastTail instanceof Tail)
                ((Tail) lastTail).tailNumber = this.tails.size();

            this.tails.add(lastTail);
        }

        GameObject getLastTail()
        {
            if (tails.empty())
                return this;

            return tails.lastElement();
        }

        void stackForEach(Consumer<? super GameObject> action)
        {
            this.tails.forEach(action);
        }

        boolean checkForTail()
        {
            return false;
        }

        @Override
        public void update()
        {
            if (this.isAlive())
            {
                super.update();
                timer++;

                int i = 0;
                for (GameObject tail : tails)
                {
                    if (i++ != tails.size()- 1)
                    ((Rectangle)tail.view).setFill(new ImagePattern(new Image("snakeBody.png")));
                }

                if (timer == 10)
                {
                    this.updatePreviousView();
                    this.timer = 0;
                }
            }
            else
            {
                view.setOpacity(.3);
                for(GameObject tail : tails)
                    tail.view.setOpacity(.3);

                view.setTranslateX(view.getTranslateX());
                view.setTranslateY(view.getTranslateY());

                timer++;

                if (timer > 5)
                {
                    if (!this.tails.empty())
                        root.getChildren().removeAll(this.tails.pop().getView());
                    else
                        root.getChildren().removeAll(this.view);

                    this.timer = 0;
                }
            }
        }
    }

    private class Tail extends GameObject
    {
        int tailNumber;
        int timer = 0;
        GameObject parent;
        GameObject head;

        Tail(GameObject parent, GameObject head)
        {
            super(new Rectangle(30, 30, Color.BLUE));

            Rectangle rectangle = new Rectangle(30,30);
            rectangle.setFill(new ImagePattern(new Image("snakeTail.png")));
            this.view = rectangle;
            this.previousX = view.getTranslateX();
            this.previousY = view.getTranslateY();
            this.previousAngle = view.getRotate();


            this.parent = parent;
            this.head = head;
            this.tailNumber = 0;
        }


        @Override
        public void update()
        {
            if (head.isAlive())
            {
                view.setTranslateX(parent.previousX);
                view.setTranslateY(parent.previousY);
                view.setRotate(parent.previousAngle);

                timer++;

                if (timer == 10)
                {
                    this.updatePreviousView();
                    this.timer = 0;
                }
            }
        }
    }

    private static class Food extends GameObject
    {
        Food()
        {
            super(new Circle(10, 10, 10, Color.RED));
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
